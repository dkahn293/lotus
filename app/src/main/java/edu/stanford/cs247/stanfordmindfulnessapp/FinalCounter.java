package edu.stanford.cs247.stanfordmindfulnessapp;

/**
 * Created by peterwashington on 2/28/16.
 */
public class FinalCounter {

    private int val;

    public FinalCounter(int intialVal) {
        val=intialVal;
    }
    public void increment(){
        val++;
    }
    public void decrement(){
        val--;
    }
    public int getVal(){
        return val;
    }

}
