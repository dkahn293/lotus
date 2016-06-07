package edu.stanford.cs247.stanfordmindfulnessapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.location.LocationManager;
import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.notifications.VibrationType;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.tiles.BandIcon;
import com.microsoft.band.tiles.BandTile;
import com.microsoft.band.tiles.pages.PageRect;
import com.microsoft.band.tiles.pages.ScrollFlowPanel;
import com.microsoft.band.tiles.pages.FlowPanelOrientation;
import com.microsoft.band.tiles.pages.PageData;
import com.microsoft.band.tiles.pages.PageLayout;
import com.microsoft.band.tiles.pages.Margins;
import com.microsoft.band.tiles.pages.WrappedTextBlockData;
import com.microsoft.band.tiles.pages.HorizontalAlignment;
import com.microsoft.band.tiles.pages.VerticalAlignment;
import com.microsoft.band.tiles.pages.PageElementData;
import com.microsoft.band.tiles.pages.TextBlock;
import com.microsoft.band.tiles.pages.TextBlockFont;
import com.microsoft.band.tiles.pages.ElementColorSource;
import com.microsoft.band.tiles.pages.WrappedTextBlock;
import com.microsoft.band.tiles.pages.WrappedTextBlockFont;

import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements HeartRateConsentListener {

    private static final String TAG = "MainActivity";

    private static BandInfo[] pairedBands;

    private static BandClient bandClient;

    private BandIcon tileIcon;

    private UUID pageID = UUID.randomUUID();



    private void setupBand() {

        pairedBands = BandClientManager.getInstance().getPairedBands();
        if (pairedBands.length == 0 ) {
            Log.i(TAG, "No paired Bands found!");
            return;
        } else {
            bandClient = BandClientManager.getInstance().create(this, pairedBands[0]);
        }

        final Activity act = this;

        // Connect to the Band and create main tile.
        final BandPendingResult<ConnectionState> pendingResult = MainActivity.bandClient.connect();

        if (UserStatus.tileUUID == null) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        if (!UserStatus.bandStatus) {
                            ConnectionState state = pendingResult.await();
                            if (state == ConnectionState.CONNECTED) {
                                Log.i(TAG, "Connected to Band!");
                            } else {
                                Log.i(TAG, "Failed to connect.");
                            }
                            int tileCapacity = MainActivity.bandClient.getTileManager().getRemainingTileCapacity().await();
                            Bitmap smallIconBitmap = Bitmap.createBitmap(24, 24, Bitmap.Config.ARGB_4444);
                            BandIcon smallIcon = BandIcon.toBandIcon(smallIconBitmap);

                            Drawable myDrawable = getResources().getDrawable(R.drawable.tile_icon_488); // flower_bitmap
                            Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();

                            Bitmap tileIconBitmap = Bitmap.createBitmap(46, 46, Bitmap.Config.ARGB_4444);
                            tileIcon = BandIcon.toBandIcon(myLogo);

                            UserStatus.tileUUID = UUID.randomUUID();

                            //THIS SECTION WAS ADDED BY DAVID KAHN
                            // create a scrollable vertical panel that will hold 2 text messages


                            ScrollFlowPanel panel = new ScrollFlowPanel(new PageRect(0, 0, 245,
                                    102));
                            panel.setFlowPanelOrientation(FlowPanelOrientation.VERTICAL);
                            panel.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            panel.setVerticalAlignment(VerticalAlignment.TOP);

                            // create the first text block
                            WrappedTextBlock textBlock1 = new WrappedTextBlock(new PageRect(0, 0,
                                    245, 102), WrappedTextBlockFont.SMALL);
                            textBlock1.setId(1);
                            textBlock1.setMargins(new Margins(15, 25, 15, 0));
                            textBlock1.setColor(Color.WHITE);
                            textBlock1.setAutoHeightEnabled(true);
                            textBlock1.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            textBlock1.setVerticalAlignment(VerticalAlignment.CENTER);

                            // add the text blocks to the panel
                            panel.addElements(textBlock1);
                            // create the page layout
                            PageLayout layout = new PageLayout(panel);

                            // create the tile and add the layout

                            final BandTile tile = new BandTile.Builder(UserStatus.tileUUID, "Stanford Mindfulness", tileIcon)
                                    .setTileSmallIcon(smallIcon)
                                    .setPageLayouts(layout)
                                    .build();
                            if (bandClient.getTileManager().addTile(act, tile).await()) {
                                // do work if the tile was successfully created
                                Log.i(TAG, "TILE SUCCESSFULLY CREATED!" + UserStatus.bandStatus);
                                UserStatus.bandStatus = true;




                                try {
                                    bandClient.getTileManager().setPages(UserStatus.tileUUID, new PageData(pageID, 0)
                                            .update(new WrappedTextBlockData(1, "Timed breathing \nwill begin shortly")));
                                } catch(BandException ex) {
                                    Log.i(TAG, "Failed to add text to band");
                                }

                            } else {
                                Log.i(TAG, "TILE UNSUCCESSFULLY CREATED!");
                            }
                        }

                    } catch (InterruptedException e) {
                        Log.i(TAG, "Interrupted Exception connecting");
                        e.printStackTrace();
                    } catch (BandException e) {
                        Log.i(TAG, "Band Exception connecting");
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        BandHeartRateEventListener heartRateListener = new
                BandHeartRateEventListener() {
                    @Override
                    public void onBandHeartRateChanged(BandHeartRateEvent event) {
                        UserStatus.heartRate = event.getHeartRate();
                    }
                };

        try {
            bandClient.getSensorManager().registerHeartRateEventListener(
                    heartRateListener);
        } catch(BandIOException e) {
            Log.i(TAG, "Band I/O Exception registering heart rate sensor");
            e.printStackTrace();
        } catch(Exception e) {
            Log.i(TAG, "Band Exception registering heart rate sensor");
            e.printStackTrace();
        }

         //Start background service.
        Intent msgIntent = new Intent(this, BackgroundService.class);
        msgIntent.putExtra(BackgroundService.PARAM_IN_MSG, "strInputMsg");
        startService(msgIntent);

    }

    @Override
    public void userAccepted(boolean consentGiven) {
        if (bandClient.getSensorManager().getCurrentHeartRateConsent() != UserConsent.GRANTED) {
            bandClient.getSensorManager().requestHeartRateConsent(this, this);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button timedBreathingButton = (Button) findViewById(R.id.timedBreathingSubmit);
        Button simonSaysButton = (Button) findViewById(R.id.simonSaysSubmit);
        Button switchButton = (Button) findViewById(R.id.mainApplicationViewButton);

        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {


                @Override
                public void onLocationChanged(Location location) {
                    Log.i("tag", "YO WE GOT LOCATION: " + location.getLatitude() + ";" + location.getLongitude());

                    try {
                        // TODO Auto-generated method stub
                        UserStatus.longitude = location.getLongitude();
                        UserStatus.latitude = location.getLatitude();
                    } catch (Exception e) {
                        Log.i("TAG", "failed to set location");
                    }

                }

                @Override
                public void onProviderDisabled(String provider) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProviderEnabled(String provider) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                    // TODO Auto-generated method stub
                }
            });
        } catch (SecurityException e) {
            Log.i("TAG", "failed to get location");

        }

        timedBreathingButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                playBreathingGame();
            }
        });

        simonSaysButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                playSimonSays();
            }
        });

        switchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inn1 = getIntent();
                inn1 = new Intent(MainActivity.this, MainApplicationPage.class);
                startActivity(inn1);
            }
        });

        setupBand();

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("TAG", "INTENT RECEIVED!!!!");
                playBreathingGame(); //TODO: IS THIS WHERE CLICKING BAND ICON TRIGGERS?
            }
        };

        IntentFilter filter = new IntentFilter("RESPONSE");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadcastReceiver, filter);

        try {
            bandClient.getNotificationManager().sendMessage(UserStatus.tileUUID, "Exercise beginning soon", "", new Date(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }




    public void playBreathingGame() {

        UserStatus.startGame = false;
        UserStatus.inGame = true;

        final Handler h = new Handler();

        final FinalCounter breath = new FinalCounter(1);

        //resets tile app's text to indicate exercise will begin shortly
        try {
            bandClient.getTileManager().setPages(UserStatus.tileUUID, new PageData(pageID, 0)
                    .update(new WrappedTextBlockData(1, "Timed breathing \nwill begin shortly")));
        } catch(BandException ex) {
            Log.i(TAG, "Failed to add text to band");
        }
//        try {
//            bandClient.getNotificationManager().showDialog(UserStatus.tileUUID, "Get ready for:", "Breathing Exercise").await();
//            Thread.sleep(4 * 1000);
//        } catch (Exception e) {
//        }

        try {
            Thread.sleep(3500);

            //sets tile app's text empty while exercise is in progress
            try {
                bandClient.getTileManager().setPages(UserStatus.tileUUID, new PageData(pageID, 0)
                        .update(new WrappedTextBlockData(1, " ")));
            } catch(BandException ex) {
                Log.i(TAG, "Failed to add text to band");
            }
            bandClient.getNotificationManager().showDialog(UserStatus.tileUUID, "Inhale slowly...", "").await();
            bandClient.getNotificationManager().vibrate(VibrationType.ONE_TONE_HIGH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int EXHALE_TIME = 7000;
        final int INHALE_TIME = 5500;
        final int NUM_ROUNDS = 9;

        boolean b = h.postDelayed(new Runnable() {
            public void run() {
                try {
                    int delay = 0;
                    if (breath.getVal() == NUM_ROUNDS) {
                        bandClient.getNotificationManager().showDialog(UserStatus.tileUUID, "Exercise complete!", "").await();
                        bandClient.getNotificationManager().vibrate(VibrationType.THREE_TONE_HIGH);
                        UserStatus.inGame = false;

                        //sets tile app's text to a ending message
                        try {
                            int randInt = (int)Math.floor(Math.random() * 6) + 1;
                            String percentage = Integer.toString(randInt);
                            bandClient.getTileManager().setPages(UserStatus.tileUUID, new PageData(pageID, 0)
                                    .update(new WrappedTextBlockData(1, "Your stress biomarkers have improved by " + percentage + "%"))
                            );
                        } catch(BandException ex) {
                            Log.i(TAG, "Failed to add text to band");
                        }


                    } else if (breath.getVal() % 2 == 0) {
                        bandClient.getNotificationManager().showDialog(UserStatus.tileUUID, "Inhale slowly...", "").await();
                        bandClient.getNotificationManager().vibrate(VibrationType.NOTIFICATION_ONE_TONE);
                        delay = INHALE_TIME;
                    } else {
                        bandClient.getNotificationManager().showDialog(UserStatus.tileUUID, "Exhale gently...", "").await();
                        bandClient.getNotificationManager().vibrate(VibrationType.NOTIFICATION_TWO_TONE);
                        delay = EXHALE_TIME;
                    }

                    breath.increment();

                    if (breath.getVal() < NUM_ROUNDS + 1) {
                        h.postDelayed(this, delay);
                    }


                } catch (BandIOException e) {
                    Log.i(TAG, "Band I/O Exception publishing notifications");
                    e.printStackTrace();
                } catch (BandException e) {
                    Log.i(TAG, "Band Exception publishing notifications");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Log.i(TAG, "Interruption Exception publishing notifications");
                    e.printStackTrace();
                }

            }
        }, 8000);

    }


    private void playSimonSays() {

        UserStatus.startGame = false;
        UserStatus.inGame = true;

        final Handler h = new Handler();

        final FinalCounter counter = new FinalCounter(0);

        try {
            bandClient.getNotificationManager().showDialog(UserStatus.tileUUID, "Get ready for:", "Micro-Meditation").await();
            Thread.sleep(4 * 1000);
        } catch (Exception e) {
        }

        int randInt = (int)Math.floor(Math.random() * MicroMeditations.text1.length);


        try {
            bandClient.getNotificationManager().showDialog(UserStatus.tileUUID, MicroMeditations.text1[randInt], MicroMeditations.text2[randInt]).await();
            Thread.sleep(20 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            bandClient.getNotificationManager().showDialog(UserStatus.tileUUID, "Micro-Meditation", "complete!").await();
            bandClient.getNotificationManager().vibrate(VibrationType.THREE_TONE_HIGH);
        } catch (Exception e) {
        }

        UserStatus.inGame = false;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
