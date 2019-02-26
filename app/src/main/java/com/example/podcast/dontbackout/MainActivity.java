package com.example.podcast.dontbackout;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends BlunoLibrary {
    private Button buttonScan;
    private Button buttonSerialSend;
    private Button buttonCalibrate;
    private Button buttonCalibrateSlouch;
    private Button buttonRecalibrate;
    private TextView serialReceivedText;
    private TextView statusText;
    public TextView calibrateText;
    private EditText serialSendText;
    private Posture posture;
    private final long CALIBRATE_TIME = 10500;
    private final long INCREMENT_TIME = 1000;

    // false if not calibrated yet, true if is calibrated
    private boolean calibrated;

    private boolean straightCalibrate;
    private boolean slouchCalibrate;

    // slouch counter to indicate when to notify the user
    private int counter;

    // sending data to beetle
    class C01441 implements View.OnClickListener {
        C01441() {
        }

        public void onClick(View v) {
//            MainActivity.this.serialSend(MainActivity.this.serialSendText.getText().toString());
//            Log.i("testing", MainActivity.this.serialSendText.getText().toString());
//            MainActivity.this.serialSendText.setText("");
        }
    }

    // scanner
    class C01452 implements View.OnClickListener {
        C01452() {
        }

        public void onClick(View v) {
            MainActivity.this.buttonScanOnClickProcess();
            posture.reset();
            straightCalibrate = false;
            slouchCalibrate = false;
            calibrated = false;
        }
    }

    // calibrating straight back
    class calibrateEvent implements View.OnClickListener{
        calibrateEvent(){
        }
        public void onClick(View v){
            straightCalibrate = true;
            calibrateText.setText("Calibrating straight...");
            MainActivity.this.buttonCalibrate.setVisibility(View.INVISIBLE);
            new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
                @Override
                public void onTick(long millisUntilFinished) {
                    calibrateText.setText("Stay straight for another: " + (millisUntilFinished/1000) + "seconds");
                }

                @Override
                public void onFinish() {
                    calibrateText.setText("Done calibrating straight. " + posture.getStraightSize() + " entries added.");
                    straightCalibrate = false;
                    MainActivity.this.buttonCalibrateSlouch.setVisibility(View.VISIBLE);
                }
            }.start();



        }
    }

    class calibrateSlouchEvent implements View.OnClickListener{

        public calibrateSlouchEvent(){ }
        public void onClick(View v){
            slouchCalibrate = true;
            calibrateText.setText("Calibrating slouch...");
            MainActivity.this.buttonCalibrateSlouch.setVisibility(View.INVISIBLE);
            new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
                @Override
                public void onTick(long millisUntilFinished) {
                    calibrateText.setText("Stay slouched for another: " + (millisUntilFinished/1000) + "seconds");
                }

                @Override
                public void onFinish() {
                    calibrateText.setText("Done calibrating slouch. " + posture.getSlouchSize() + " entries added.");
                    slouchCalibrate = false;
                    MainActivity.this.buttonRecalibrate.setVisibility(View.VISIBLE);
                    calibrated = true;
                }
            }.start();
        }
    }


    class reCalibrate implements View.OnClickListener{
        public reCalibrate(){}

        @Override
        public void onClick(View v) {
            calibrated = false;
            buttonCalibrate.setVisibility(View.VISIBLE);
            posture.reset();
            calibrateText.setText("Click calibrate button when you are ready.");
            buttonRecalibrate.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeGUI(){
        this.buttonScan = (Button) findViewById(R.id.buttonScan);
        this.buttonScan.setOnClickListener(new C01452());
        this.statusText = findViewById(R.id.textBackStatus);
        this.buttonCalibrate = findViewById(R.id.buttonCalibrate);
        this.buttonCalibrate.setOnClickListener(new calibrateEvent());
        this.calibrateText = findViewById(R.id.textCalibrate);
        this.buttonCalibrateSlouch = findViewById(R.id.buttonCalibrateSlouch);
        this.buttonCalibrateSlouch.setOnClickListener(new calibrateSlouchEvent());
        this.buttonRecalibrate = findViewById(R.id.buttonRecalibrate);
        this.buttonRecalibrate.setOnClickListener(new reCalibrate());

        this.buttonCalibrateSlouch.setVisibility(View.INVISIBLE);
        this.buttonRecalibrate.setVisibility(View.INVISIBLE);

        posture = new Posture();
        this.calibrated = false;
        this.counter = 0;
    }

    // runs first
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreateProcess();
        serialBegin(115200);
        this.initializeGUI();
        // initializes the buttons and text in UI
//        this.serialReceivedText = (TextView) findViewById(R.id.serialReveicedText);
//        this.serialSendText = (EditText) findViewById(R.id.serialSendText);
//        this.buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);
//        this.buttonSerialSend.setOnClickListener(new C01441());


    }

    protected void onResume() {
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onPause() {
        super.onPause();
        onPauseProcess();
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();
    }

    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();
    }

    // figures out the status when scanning bluetooth
    public void onConectionStateChange(connectionStateEnum theConnectionState) {
        switch (theConnectionState) {
            case isConnected:
                this.buttonScan.setText("Connected");
                return;
            case isConnecting:
                this.buttonScan.setText("Connecting");
                return;
            case isToScan:
                this.buttonScan.setText("Scan");
                return;
            case isScanning:
                this.buttonScan.setText("Scanning");
                return;
            case isDisconnecting:
                this.buttonScan.setText("isDisconnecting");
                return;
            default:
                return;
        }
    }

    // gets what beetle sends to the device
    public void onSerialReceived(String theString) {
//        this.serialReceivedText.append(theString);
        Double[] angles = turnStringToDoubleArray(theString);
        if (angles == null){
            return;
        }
        if (angles[0] == null || angles[1] == null || angles[2] == null || angles.length != 3){
            return;
        }
        if (this.calibrated) {
            String straightFlag = posture.findPostureStatus(angles);
            Log.i("Straight/Slouch: ", straightFlag);
            if (straightFlag == null){ return; }
            statusText.setText(straightFlag);
            if (straightFlag.equals("SLOUCHED")){
                counter++;
                if (counter % 10 == 0){
                    addNotification();
                }
            }
            else{
                counter = 0;
            }
        }
        else if (this.straightCalibrate){
            for (int i = 0; i < 10; i++) {
                posture.addStraightData(angles);
            }
            Log.i("Calibrating", "adding straight calibrated data");
        }
        else if (this.slouchCalibrate){
            for (int i = 0; i < 10; i++){
                posture.addSlouchData(angles);
            }
            Log.i("Calibrating", "adding slouch calibrated data");
        }
        // 130 represents Transport.KEYCODE_MEDIA_RECORD
//        ((ScrollView) this.serialReceivedText.getParent()).fullScroll(130);

    }

    public Double[] turnStringToDoubleArray(String x){
        // ...
        String[] split = x.split(" ");
        Double[] angles = new Double[3];
        for (int i = 0; i < split.length; i++){
            try{
                // Log.i("SPLIT STRING: ", split[i]);
                angles[i] = Double.parseDouble(split[i]);
            }
            catch (NumberFormatException e){
                return null;
            }
        }
        return angles;
    }

    private void addNotification(){
        // notificatoin ringtone
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // builds notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Bad Back!")
                .setContentText("Make sure to straighten out your back.")
                .setAutoCancel(true)
                .setSound(soundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX);

        // Create the intent needed to show the notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Creating a pendingIntent for the notification to know how to act
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
