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
    private TextView serialReceivedText;
    private EditText serialSendText;
    private Posture posture;

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
        posture = new Posture();
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

        // 130 represents Transport.KEYCODE_MEDIA_RECORD
//        ((ScrollView) this.serialReceivedText.getParent()).fullScroll(130);
        posture.add(theString);
    }
}
