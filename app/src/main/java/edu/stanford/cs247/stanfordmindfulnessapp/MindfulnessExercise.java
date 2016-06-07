package edu.stanford.cs247.stanfordmindfulnessapp;

/**
 * Created by peterwashington on 3/7/16.
 */
public class MindfulnessExercise {

    private String exercise;

    private String location;

    private double longitude;

    private double latitude;

    public MindfulnessExercise(String exercise, String activity, double longitude, double latitude) {
        this.exercise = exercise;
        this.location = activity;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String activity) {
        this.location = activity;
    }
}
