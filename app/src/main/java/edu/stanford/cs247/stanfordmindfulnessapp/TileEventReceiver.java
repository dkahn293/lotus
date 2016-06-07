package edu.stanford.cs247.stanfordmindfulnessapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by peterwashington on 2/28/16.
 */
public class TileEventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.microsoft.band.action.ACTION_TILE_OPENED")) {
            // handle tile opened event
            Log.i("TAG", "TILE WAS OPENED!!!" + UserStatus.inGame + " " + UserStatus.startGame);
            if (!UserStatus.inGame)
                UserStatus.startGame = true;
        } else if (intent.getAction().equals("com.microsoft.band.action.ACTION_TILE_BUTTON_PRESSED")) {
            // handle button pressed event
            Log.i("TAG", "TILE BUTTON WAS OPENED!!!");
        } else if (intent.getAction().equals("com.microsoft.band.action.ACTION_TILE_CLOSED")) {
            // handle tile closed event
            Log.i("TAG", "TILE WAS CLOSED!!!");
        }
    }
}