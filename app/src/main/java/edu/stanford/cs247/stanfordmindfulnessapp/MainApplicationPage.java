package edu.stanford.cs247.stanfordmindfulnessapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.app.Activity;

public class MainApplicationPage extends AppCompatActivity {

    private MindfulnessListAdapter adapter;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_application_page);

        this.activity = this;

        final ListView listView = (ListView) findViewById(R.id.listView);

        adapter = new MindfulnessListAdapter(this);

        if (UserStatus.exercises.size() < 1) {
            adapter.addText("                    No locations added yet!");
        } else {
            for (MindfulnessExercise exercise : UserStatus.exercises) {
                adapter.addText(exercise.getExercise() + " at " + exercise.getLocation());
            }
        }


        listView.setAdapter(adapter);

        final Button addButton = (Button) findViewById(R.id.imageButton1);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, AddNewLocationActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                activity.startActivity(myIntent);
            }
        });

        if (UserStatus.exercises.size() >= 4) {
            addButton.setEnabled(false);
        }

        final Button settingsButton = (Button) findViewById(R.id.meditateButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, MainActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                activity.startActivity(myIntent);
            }
        });

    }
}
