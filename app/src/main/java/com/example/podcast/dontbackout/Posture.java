package com.example.podcast.dontbackout;
import android.util.Log;

import java.util.ArrayList;

public class Posture {
    ArrayList<Double[]> slouchBack;
    ArrayList<Double[]> straightBack;

    Double[] totalSlouch;
    Double[] totalStraight;

    Double[] slouchMean;
    Double[] straightMean;

    // constructor
    public Posture(){
        this.slouchBack = new ArrayList<>();
        this.straightBack = new ArrayList<>();
        this.totalSlouch = new Double[]{0.0, 0.0, 0.0};
        this.totalStraight = new Double[]{0.0, 0.0, 0.0};
//        slouchMean = findMean(totalSlouch);
//        straightMean = findMean(totalStraight);
    }

    public void addSlouchData(Double[] angles) {
        this.addSlouch(angles);
    }

    public void addStraightData(Double[] angles) {
        this.addStraight(angles);
    }

    // add slouch datas and updates slouch mean
    public void addSlouch(Double[] input){
        if (input.length != 3){
            throw new IllegalArgumentException("The vector must only have 3 coordinates: x, y, and z");
        }
        if (input[0] != null && input[1] != null && input[2] != null){
            slouchBack.add(input);
            double x = input[0];
            double y = input[1];
            double z = input[2];
            totalSlouch[0] += x;
            totalSlouch[1] += y;
            totalSlouch[2] += z;
            slouchMean = findMean(totalSlouch);
            Log.d("added to posture", "SLOUCH - x: " + x + " y: " + y + " z: " + z);
        }

    }

    // add straight data and updates straight mean
    public void addStraight(Double[] input){
        double x = input[0];
        double y = input[1];
        double z = input[2];
        totalStraight[0] += x;
        totalStraight[1] += y;
        totalStraight[2] += z;
        straightMean = findMean(totalStraight);
        Log.d("added to posture", "STRAIGHT - x: " + x + " y: " + y + " z: " + z);
    }

    // returns vector for means of x, y, and z
    private Double[] findMean(Double[] total){
        // total x, y, and z
        if (total == null){
            return new Double[3];
        }
        int size = total.length;
        return new Double[]{total[0]/size, total[1]/size, total[2]/size};
    }

    // called from MainActivity to check whether the user's current angle is straight or slouching
    // true means straight, false means sloucu
    public String findPostureStatus(Double[] input){
        Double[] slouchSub = new Double[3];
        Double[] straightSub = new Double[3];

        if (input == null || input[0] == null || input[1] == null || input[2] == null || input.length != 3){
            return null;
        }

        for (int i = 0; i < 3; i++){
            slouchSub[i] = input[i] - slouchMean[i];
            straightSub[i] = input[i] - straightMean[i];
        }
        Log.i ("Means: ", "Slouch Mean: " + slouchMean[0] + slouchMean[1] + slouchMean[2] + "\nStraightMean: " +
                straightMean[0] + straightMean[1] + straightMean[2]);
        if (slouchSub == null || straightSub == null){
            throw new NullPointerException("slouchsub or straightsub are null");
        }
        if(matrixMultiplication(straightSub) < matrixMultiplication(slouchSub)) {
            if (input[0] != null && input[1] != null && input[2] != null){
                addStraight(input);
            }
            return "STRAIGHT";
        }
        else{
            if ((input[0] != null && input[1] != null && input[2] != null)){
                addSlouch(input);
            }

            return "SLOUCHED";
        }

    }

    public void reset(){
        slouchBack.clear();
        straightBack.clear();
        for (int i = 0; i < 3; i++){
            totalStraight[i] = 0.0;
            totalSlouch[i] = 0.0;
        }
        slouchMean = null;
        straightMean = null;
    }

    // helper method to multiply the two vectors
    private double matrixMultiplication(Double[] input){
        double sum = input[0]*input[0] + input[1]*input[1] + input[2]*input[2];
        return sum;
    }

}
