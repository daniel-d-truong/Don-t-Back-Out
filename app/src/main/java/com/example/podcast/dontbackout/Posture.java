package com.example.podcast.dontbackout;
import android.util.Log;

public class Posture {
    int slouchSize = 0;
    int straightSize = 0;

    Double[] slouchMean;
    Double[] straightMean;

    double criticalAngle;

    // constructor
    public Posture(){
        this.reset();
//        slouchMean = findMean(totalSlouch);
//        straightMean = findMean(totalStraight);
    }

    public void reset(){
        straightSize = 0;
        slouchSize = 0;
        slouchMean = new Double[]{0.0, 0.0, 0.0};
        straightMean = new Double[]{0.0, 0.0, 0.0};
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
            findMean(false, input);
            Log.d("added to posture", "SLOUCH - x: " + input[0] + " y: " + input[1] + " z: " + input[2]);
        }

    }

    // add straight data and updates straight mean
    public void addStraight(Double[] input){
        if (input.length != 3){
            throw new IllegalArgumentException("The vector must only have 3 coordinates: x, y, and z");
        }
        if (input[0] != null && input[1] != null && input[2] != null) {
            findMean(true, input);
            Log.d("added to posture", "STRAIGHT - x: " + input[0] + " y: " + input[1] + " z: " + input[2]);
        }
    }

    // returns vector for means of x, y, and z
    private void findMean(boolean backStatus, Double[] newInput){
        // total x, y, and z
        if (newInput == null){
            return;
        }

        Double[] dataset;
        int size;
        if (backStatus) {
            dataset = straightMean;
            this.straightSize++;
            size = this.straightSize;
        }
        else{
            dataset = slouchMean;
            this.slouchSize++;
            size = this.slouchSize;
        }

        for (int i = 0; i < 3; i++){
            double avg = dataset[i];
            dataset[i] += (newInput[i] - avg) / size;
        }
    }

    // called from MainActivity to check whether the user's current angle is straight or slouching
    // true means straight, false means sloucu
//    public String findPostureStatus(Double[] input){
//        Double[] slouchSub = new Double[3];
//        Double[] straightSub = new Double[3];
//
//        if (input == null || input[0] == null || input[1] == null || input[2] == null || input.length != 3){
//            return null;
//        }
//
//        for (int i = 0; i < 3; i++){
//            slouchSub[i] = input[i] - slouchMean[i];
//            straightSub[i] = input[i] - straightMean[i];
//        }
//        Log.i ("Means: ", "Slouch Mean: " + slouchMean[0] + " " + slouchMean[1] + " " + slouchMean[2] +
//                "\nStraightMean: " + straightMean[0] + " " + straightMean[1] + " " + straightMean[2]);
//        if (slouchSub == null || straightSub == null){
//            throw new NullPointerException("slouchsub or straightsub are null");
//        }
//        if(matrixMultiplication(straightSub) < matrixMultiplication(slouchSub)) {
//            if (input[0] != null && input[1] != null && input[2] != null){
//                addStraight(input);
//            }
//            return "STRAIGHT";
//        }
//        else {
//            if ((input[0] != null && input[1] != null && input[2] != null)){
//                addSlouch(input);
//            }
//            return "SLOUCHED";
//        }
//
//    }

    public String findPostureStatus(Double[] input){
        criticalAngle = (straightMean[1] + slouchMean[1]) / 2;
        if (input[1] > criticalAngle){
            addStraight(input);
            return "STRAIGHT";
        }
        else{
            addSlouch(input);
            return "SLOUCHED";
        }
    }

    // helper method to multiply the two vectors
    private double matrixMultiplication(Double[] input){
        double sum = input[0]*input[0] + input[1]*input[1] + input[2]*input[2];
        return sum;
    }

    public int getStraightSize(){
        return straightSize;
    }

    public int getSlouchSize(){
        return slouchSize;
    }

    public double getCriticalAngle(){ return this.criticalAngle; }

    @Override
    public String toString() {
        return "\nMean Slouch:" + slouchMean.toString() + "\nMean Straight: " + straightMean.toString();
    }
}
