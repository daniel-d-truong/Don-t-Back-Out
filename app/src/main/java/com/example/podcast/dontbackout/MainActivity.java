package com.example.podcast.dontbackout;

import android.content.Intent;
import android.os.Bundle;
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

    // false --> not calibrating true --> calibrating
    private boolean calibrateFlag;

    // false if not calibrated yet, true if is calibrated
    private boolean calibrated;

    private boolean straightCalibrate;
    private boolean slouchCalibrate;
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
        }
    }

    class calibrateEvent implements View.OnClickListener{
        TextView calibrateText = findViewById(R.id.textCalibrate);

        boolean calibrating;
        calibrateEvent(){
            calibrating = false;
        }
        public void onClick(View v){
             calibrating = !calibrating;
             if (calibrating){
                 straightCalibrate = true;
                 calibrateText.setText("Calibrating straight..."); }
             else{
                 calibrateText.setText("Done calibrating straight.");
                 straightCalibrate = false;
                 MainActivity.this.buttonCalibrate.setVisibility(View.INVISIBLE);
                 if (buttonCalibrateSlouch.getVisibility() == View.INVISIBLE){
                     calibrated = true;
                 }
             }
        }
    }

    class calibrateSlouchEvent implements View.OnClickListener{
        boolean calibrating;
        public calibrateSlouchEvent(){ calibrating = false; }
        public void onClick(View v){
            calibrating = !calibrating;
            if (calibrating){
                slouchCalibrate = true;
                calibrateText.setText("Calibrating slouch...");
            }
            else{
                calibrateText.setText("Done calibrating slouch.");
                slouchCalibrate = false;
                MainActivity.this.buttonCalibrateSlouch.setVisibility(View.INVISIBLE);
                if (buttonCalibrate.getVisibility() == View.INVISIBLE){
                    calibrated = true;
                }
            }
        }
    }

    class reCalibrate implements View.OnClickListener{
        public reCalibrate(){}

        @Override
        public void onClick(View v) {
            calibrated = false;
            buttonCalibrate.setVisibility(View.VISIBLE);
            buttonCalibrateSlouch.setVisibility(View.VISIBLE);
            posture.reset();
        }
    }

    // runs first
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreateProcess();
        serialBegin(115200);

        // initializes the buttons and text in UI
//        this.serialReceivedText = (TextView) findViewById(R.id.serialReveicedText);
//        this.serialSendText = (EditText) findViewById(R.id.serialSendText);
        this.buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);
        this.buttonSerialSend.setOnClickListener(new C01441());
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

        posture = new Posture();
        this.calibrateFlag = false;
        this.calibrated = false;
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
        }
        else if (this.straightCalibrate){
            posture.addStraightData(angles);
            Log.i("Calibrating", "adding straight calibrated data");
        }
        else if (this.slouchCalibrate){
            posture.addSlouchData(angles);
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
}
