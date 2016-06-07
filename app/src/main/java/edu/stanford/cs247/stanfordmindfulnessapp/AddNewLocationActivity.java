package edu.stanford.cs247.stanfordmindfulnessapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.view.View;
import android.util.Log;
import android.widget.Button;

public class AddNewLocationActivity extends AppCompatActivity {

    private Activity activity;

    private String selectedExercise;

    private LocationManager locationManager;
    private String provider;
    private Criteria criteria;

//    LocationManager lm;
//    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location);

        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        activity = this;

//        try {
//            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//        } catch (SecurityException e) {}

        final EditText locationText = (EditText) findViewById(R.id.editText);

        final Button addButton = (Button) findViewById(R.id.imageButtonSave);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {
                    Log.i("tag", "HERE");

                   // double longitude = location.getLongitude();
                    //double latitude = location.getLatitude();

                    Log.i("tag", "WE GET HERE IN THE FIRST PLACE?" + UserStatus.longitude + ";" + UserStatus.latitude);

                    MindfulnessExercise newExcercise = new MindfulnessExercise(selectedExercise, locationText.getText().toString(), UserStatus.longitude, UserStatus.latitude);
                    UserStatus.exercises.add(newExcercise);

                    //Log.i("tag", "LONGITUDE AND LATITUDE: " + location.getLongitude() + " " + location.getLatitude());

                    Intent myIntent = new Intent(activity, MainApplicationPage.class);
                    //myIntent.putExtra("key", value); //Optional parameters
                    activity.startActivity(myIntent);
                } catch (SecurityException e) {}
            }
        });


    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_breathing:
                if (checked)
                    selectedExercise = "Deep Breathing";
                    Log.i("TAG", "BREATHING PRESSED");
                    break;
            case R.id.radio_location:
                if (checked)
                    selectedExercise = "Location Activity";
                    Log.i("TAG", "PEACE PRESSED");
                    break;

            case R.id.radio_classic:
                if (checked)
                    selectedExercise = "Classic Activity";
                Log.i("TAG", "CLASSIC PRESSED");
                break;
        }
    }

}
