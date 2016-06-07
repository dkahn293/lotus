package edu.stanford.cs247.stanfordmindfulnessapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.tiles.BandIcon;
import com.microsoft.band.tiles.BandTile;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by peterwashington on 2/27/16.
 */
public class UserStatus {

    private static String TAG = "bla";

    public static int heartRate;

    public static boolean bandStatus = false;

    public static ArrayList<MindfulnessExercise> exercises = new ArrayList<MindfulnessExercise>();

    public static UUID tileUUID;

    public static boolean startGame = false;

    public static boolean inGame = false;

    public static double longitude;
    public static double latitude;


}
