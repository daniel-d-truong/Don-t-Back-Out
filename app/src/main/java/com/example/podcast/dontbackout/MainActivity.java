package com.example.podcast.dontbackout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "dab";
    TextView t1;
    String address = null, name = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null; //where we can get our output/input from
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            setup();
        }
        catch(Exception e){}
    }

    private void setup() throws IOException {
        t1 = (TextView)findViewById(R.id.test_text);
        Log.i(TAG, (String) t1.getText());
        bluetoothConnectDevice();
    }


    // method to connect bluetooth devices
    private void bluetoothConnectDevice() throws IOException
    {
        try
        {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size() > 0)
            {
                for (BluetoothDevice bt : pairedDevices)
                {
                    address = bt.getAddress().toString();
                    name = bt.getName().toString();
                    Log.i(TAG, "gotem" + address + " " + name);
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception we){}

        myBluetooth = BluetoothAdapter.getDefaultAdapter(); //get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address); //connects to the device's address and checks if it's available
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID); //create a RFCOMM (SPP) connection
        btSocket.connect();
        try {
            Log.i(TAG, "\"BT Name: \" + name + \"\\nBT Address: \" + address");
            t1.setText("BT Name: " + name + "\nBT Address: " + address);
        }
        catch (Exception e){
            Log.i(TAG, "Text can't be set");
        }

    }
}
