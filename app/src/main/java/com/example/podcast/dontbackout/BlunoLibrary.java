package com.example.podcast.dontbackout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.podcast.dontbackout.BluetoothLeService.LocalBinder;

import java.util.ArrayList;
import java.util.List;

public abstract class BlunoLibrary extends Activity {
    public static final String CommandUUID = "0000dfb2-0000-1000-8000-00805f9b34fb";
    public static final String ModelNumberStringUUID = "00002a24-0000-1000-8000-00805f9b34fb";
    private static final int REQUEST_ENABLE_BT = 1;
    public static final String SerialPortUUID = "0000dfb1-0000-1000-8000-00805f9b34fb";
    private static final String TAG = BlunoLibrary.class.getSimpleName();
    private static BluetoothGattCharacteristic mCommandCharacteristic;
    private static BluetoothGattCharacteristic mModelNumberCharacteristic;
    private static BluetoothGattCharacteristic mSCharacteristic;
    private static BluetoothGattCharacteristic mSerialPortCharacteristic;
    private int mBaudrate = 115200;
    private String mBaudrateBuffer = ("AT+CURRUART=" + this.mBaudrate + "\r\n");
    private BluetoothAdapter mBluetoothAdapter;
    BluetoothLeService mBluetoothLeService;
    public boolean mConnected = false;
    private Runnable mConnectingOverTimeRunnable = new C01351();
    public connectionStateEnum mConnectionState = connectionStateEnum.isNull;
    private String mDeviceAddress;
    private String mDeviceName;
    private Runnable mDisonnectingOverTimeRunnable = new C01362();
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList();
    private final BroadcastReceiver mGattUpdateReceiver = new C01395();
    private Handler mHandler = new Handler();
    private LeDeviceListAdapter mLeDeviceListAdapter = null;
    private LeScanCallback mLeScanCallback = new C01427();
    private String mPassword = "AT+PASSWOR=DFRobot\r\n";
    AlertDialog mScanDeviceDialog;
    private boolean mScanning = false;
    ServiceConnection mServiceConnection = new C01406();
    private Context mainContext = this;

    /* renamed from: com.dfrobot.angelo.blunobasicdemo.BlunoLibrary$1 */
    class C01351 implements Runnable {
        C01351() {
        }

        public void run() {
            if (BlunoLibrary.this.mConnectionState == connectionStateEnum.isConnecting) {
                BlunoLibrary.this.mConnectionState = connectionStateEnum.isToScan;
            }
            BlunoLibrary.this.onConectionStateChange(BlunoLibrary.this.mConnectionState);
            BlunoLibrary.this.mBluetoothLeService.close();
        }
    }

    /* renamed from: com.dfrobot.angelo.blunobasicdemo.BlunoLibrary$2 */
    class C01362 implements Runnable {
        C01362() {
        }

        public void run() {
            if (BlunoLibrary.this.mConnectionState == connectionStateEnum.isDisconnecting) {
                BlunoLibrary.this.mConnectionState = connectionStateEnum.isToScan;
            }
            BlunoLibrary.this.onConectionStateChange(BlunoLibrary.this.mConnectionState);
            BlunoLibrary.this.mBluetoothLeService.close();
        }
    }

    /* renamed from: com.dfrobot.angelo.blunobasicdemo.BlunoLibrary$3 */
    class C01373 implements OnCancelListener {
        C01373() {
        }

        public void onCancel(DialogInterface arg0) {
            System.out.println("mBluetoothAdapter.stopLeScan");
            BlunoLibrary.this.mConnectionState = connectionStateEnum.isToScan;
            BlunoLibrary.this.onConectionStateChange(BlunoLibrary.this.mConnectionState);
            BlunoLibrary.this.mScanDeviceDialog.dismiss();
            BlunoLibrary.this.scanLeDevice(false);
        }
    }

    /* renamed from: com.dfrobot.angelo.blunobasicdemo.BlunoLibrary$4 */
    class C01384 implements OnClickListener {
        C01384() {
        }

        public void onClick(DialogInterface dialog, int which) {
            BluetoothDevice device = BlunoLibrary.this.mLeDeviceListAdapter.getDevice(which);
            if (device != null) {
                BlunoLibrary.this.scanLeDevice(false);
                if (device.getName() == null || device.getAddress() == null) {
                    BlunoLibrary.this.mConnectionState = connectionStateEnum.isToScan;
                    BlunoLibrary.this.onConectionStateChange(BlunoLibrary.this.mConnectionState);
                    return;
                }
                System.out.println("onListItemClick " + device.getName().toString());
                System.out.println("Device Name:" + device.getName() + "   " + "Device Name:" + device.getAddress());
                BlunoLibrary.this.mDeviceName = device.getName().toString();
                BlunoLibrary.this.mDeviceAddress = device.getAddress().toString();
                if (BlunoLibrary.this.mBluetoothLeService.connect(BlunoLibrary.this.mDeviceAddress)) {
                    Log.d(BlunoLibrary.TAG, "Connect request success");
                    BlunoLibrary.this.mConnectionState = connectionStateEnum.isConnecting;
                    BlunoLibrary.this.onConectionStateChange(BlunoLibrary.this.mConnectionState);
                    BlunoLibrary.this.mHandler.postDelayed(BlunoLibrary.this.mConnectingOverTimeRunnable, 10000);
                    return;
                }
                Log.d(BlunoLibrary.TAG, "Connect request fail");
                BlunoLibrary.this.mConnectionState = connectionStateEnum.isToScan;
                BlunoLibrary.this.onConectionStateChange(BlunoLibrary.this.mConnectionState);
            }
        }
    }

    /* renamed from: com.dfrobot.angelo.blunobasicdemo.BlunoLibrary$5 */
    class C01395 extends BroadcastReceiver {
        C01395() {
        }

        @SuppressLint({"DefaultLocale"})
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("mGattUpdateReceiver->onReceive->action=" + action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                BlunoLibrary.this.mConnected = true;
                BlunoLibrary.this.mHandler.removeCallbacks(BlunoLibrary.this.mConnectingOverTimeRunnable);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                BlunoLibrary.this.mConnected = false;
                BlunoLibrary.this.mConnectionState = connectionStateEnum.isToScan;
                BlunoLibrary.this.onConectionStateChange(BlunoLibrary.this.mConnectionState);
                BlunoLibrary.this.mHandler.removeCallbacks(BlunoLibrary.this.mDisonnectingOverTimeRunnable);
                BlunoLibrary.this.mBluetoothLeService.close();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                for (BluetoothGattService gattService : BlunoLibrary.this.mBluetoothLeService.getSupportedGattServices()) {
                    System.out.println("ACTION_GATT_SERVICES_DISCOVERED  " + gattService.getUuid().toString());
                }
                BlunoLibrary.this.getGattServices(BlunoLibrary.this.mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                if (BlunoLibrary.mSCharacteristic == BlunoLibrary.mModelNumberCharacteristic) {
                    if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA).toUpperCase().startsWith("DF BLUNO")) {
                        BlunoLibrary.this.mBluetoothLeService.setCharacteristicNotification(BlunoLibrary.mSCharacteristic, false);
                        BlunoLibrary.mSCharacteristic = BlunoLibrary.mCommandCharacteristic;
                        BlunoLibrary.mSCharacteristic.setValue(BlunoLibrary.this.mPassword);
                        BlunoLibrary.this.mBluetoothLeService.writeCharacteristic(BlunoLibrary.mSCharacteristic);
                        BlunoLibrary.mSCharacteristic.setValue(BlunoLibrary.this.mBaudrateBuffer);
                        BlunoLibrary.this.mBluetoothLeService.writeCharacteristic(BlunoLibrary.mSCharacteristic);
                        BlunoLibrary.mSCharacteristic = BlunoLibrary.mSerialPortCharacteristic;
                        BlunoLibrary.this.mBluetoothLeService.setCharacteristicNotification(BlunoLibrary.mSCharacteristic, true);
                        BlunoLibrary.this.mConnectionState = connectionStateEnum.isConnected;
                        BlunoLibrary.this.onConectionStateChange(BlunoLibrary.this.mConnectionState);
                    } else {
                        Toast.makeText(BlunoLibrary.this.mainContext, "Please select DFRobot devices", 0).show();
                        BlunoLibrary.this.mConnectionState = connectionStateEnum.isToScan;
                        BlunoLibrary.this.onConectionStateChange(BlunoLibrary.this.mConnectionState);
                    }
                } else if (BlunoLibrary.mSCharacteristic == BlunoLibrary.mSerialPortCharacteristic) {
                    BlunoLibrary.this.onSerialReceived(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                }
                System.out.println("displayData " + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    }

    /* renamed from: com.dfrobot.angelo.blunobasicdemo.BlunoLibrary$6 */
    class C01406 implements ServiceConnection {
        C01406() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder service) {
            System.out.println("mServiceConnection onServiceConnected");
            BlunoLibrary.this.mBluetoothLeService = ((LocalBinder) service).getService();
            if (!BlunoLibrary.this.mBluetoothLeService.initialize()) {
                Log.e(BlunoLibrary.TAG, "Unable to initialize Bluetooth");
                ((Activity) BlunoLibrary.this.mainContext).finish();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            System.out.println("mServiceConnection onServiceDisconnected");
            BlunoLibrary.this.mBluetoothLeService = null;
        }
    }

    /* renamed from: com.dfrobot.angelo.blunobasicdemo.BlunoLibrary$7 */
    class C01427 implements LeScanCallback {
        C01427() {
        }

        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            ((Activity) BlunoLibrary.this.mainContext).runOnUiThread(new Runnable() {
                public void run() {
                    System.out.println("mLeScanCallback onLeScan run ");
                    BlunoLibrary.this.mLeDeviceListAdapter.addDevice(device);
                    BlunoLibrary.this.mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class LeDeviceListAdapter extends BaseAdapter {
        private LayoutInflater mInflator;
        private ArrayList<BluetoothDevice> mLeDevices = new ArrayList();

        public LeDeviceListAdapter() {
            this.mInflator = ((Activity) BlunoLibrary.this.mainContext).getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!this.mLeDevices.contains(device)) {
                this.mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return (BluetoothDevice) this.mLeDevices.get(position);
        }

        public void clear() {
            this.mLeDevices.clear();
        }

        public int getCount() {
            return this.mLeDevices.size();
        }

        public Object getItem(int i) {
            return this.mLeDevices.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                System.out.println("mInflator.inflate  getView");
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BluetoothDevice device = (BluetoothDevice) this.mLeDevices.get(i);
            String deviceName = device.getName();
            if (deviceName == null || deviceName.length() <= 0) {
                viewHolder.deviceName.setText(R.string.unknown_device);
            } else {
                viewHolder.deviceName.setText(deviceName);
            }
            viewHolder.deviceAddress.setText(device.getAddress());
            return view;
        }
    }

    static class ViewHolder {
        TextView deviceAddress;
        TextView deviceName;

        ViewHolder() {
        }
    }

    public enum connectionStateEnum {
        isNull,
        isScanning,
        isToScan,
        isConnecting,
        isConnected,
        isDisconnecting
    }

    public abstract void onConectionStateChange(connectionStateEnum connectionstateenum);

    public abstract void onSerialReceived(String str);

    public void serialSend(String theString) {
        if (this.mConnectionState == connectionStateEnum.isConnected) {
            mSCharacteristic.setValue(theString);
            this.mBluetoothLeService.writeCharacteristic(mSCharacteristic);
        }
    }

    public void serialBegin(int baud) {
        this.mBaudrate = baud;
        this.mBaudrateBuffer = "AT+CURRUART=" + this.mBaudrate + "\r\n";
    }

    public void onCreateProcess() {
        if (!initiate()) {
            Toast.makeText(this.mainContext, R.string.error_bluetooth_not_supported, 0).show();
            ((Activity) this.mainContext).finish();
        }
        bindService(new Intent(this, BluetoothLeService.class), this.mServiceConnection, 1);
        this.mLeDeviceListAdapter = new LeDeviceListAdapter();
        this.mScanDeviceDialog = new Builder(this.mainContext).setTitle("BLE Device Scan...").setAdapter(this.mLeDeviceListAdapter, new C01384()).setOnCancelListener(new C01373()).create();
    }

    public void onResumeProcess() {
        System.out.println("BlUNOActivity onResume");
        if (!(this.mBluetoothAdapter.isEnabled() || this.mBluetoothAdapter.isEnabled())) {
            ((Activity) this.mainContext).startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
        this.mainContext.registerReceiver(this.mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    public void onPauseProcess() {
        System.out.println("BLUNOActivity onPause");
        scanLeDevice(false);
        this.mainContext.unregisterReceiver(this.mGattUpdateReceiver);
        this.mLeDeviceListAdapter.clear();
        this.mConnectionState = connectionStateEnum.isToScan;
        onConectionStateChange(this.mConnectionState);
        this.mScanDeviceDialog.dismiss();
        if (this.mBluetoothLeService != null) {
            this.mBluetoothLeService.disconnect();
            this.mHandler.postDelayed(this.mDisonnectingOverTimeRunnable, 10000);
        }
        mSCharacteristic = null;
    }

    public void onStopProcess() {
        System.out.println("MiUnoActivity onStop");
        if (this.mBluetoothLeService != null) {
            this.mHandler.removeCallbacks(this.mDisonnectingOverTimeRunnable);
            this.mBluetoothLeService.close();
        }
        mSCharacteristic = null;
    }

    public void onDestroyProcess() {
        this.mainContext.unbindService(this.mServiceConnection);
        this.mBluetoothLeService = null;
    }

    public void onActivityResultProcess(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 0) {
            ((Activity) this.mainContext).finish();
        }
    }

    boolean initiate() {
        if (!this.mainContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            return false;
        }
        this.mBluetoothAdapter = ((BluetoothManager) this.mainContext.getSystemService("bluetooth")).getAdapter();
        if (this.mBluetoothAdapter != null) {
            return true;
        }
        return false;
    }

    void buttonScanOnClickProcess() {
        switch (this.mConnectionState) {
            case isNull:
                this.mConnectionState = connectionStateEnum.isScanning;
                onConectionStateChange(this.mConnectionState);
                scanLeDevice(true);
                this.mScanDeviceDialog.show();
                return;
            case isToScan:
                this.mConnectionState = connectionStateEnum.isScanning;
                onConectionStateChange(this.mConnectionState);
                scanLeDevice(true);
                this.mScanDeviceDialog.show();
                return;
            case isConnected:
                this.mBluetoothLeService.disconnect();
                this.mHandler.postDelayed(this.mDisonnectingOverTimeRunnable, 10000);
                this.mConnectionState = connectionStateEnum.isDisconnecting;
                onConectionStateChange(this.mConnectionState);
                return;
            default:
                return;
        }
    }

    void scanLeDevice(boolean enable) {
        if (enable) {
            System.out.println("mBluetoothAdapter.startLeScan");
            if (this.mLeDeviceListAdapter != null) {
                this.mLeDeviceListAdapter.clear();
                this.mLeDeviceListAdapter.notifyDataSetChanged();
            }
            if (!this.mScanning) {
                this.mScanning = true;
                this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
            }
        } else if (this.mScanning) {
            this.mScanning = false;
            this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
        }
    }

    private void getGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices != null) {
            mModelNumberCharacteristic = null;
            mSerialPortCharacteristic = null;
            mCommandCharacteristic = null;
            this.mGattCharacteristics = new ArrayList();
            for (BluetoothGattService gattService : gattServices) {
                System.out.println("displayGattServices + uuid=" + gattService.getUuid().toString());
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                ArrayList<BluetoothGattCharacteristic> charas = new ArrayList();
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    charas.add(gattCharacteristic);
                    String uuid = gattCharacteristic.getUuid().toString();
                    if (uuid.equals(ModelNumberStringUUID)) {
                        mModelNumberCharacteristic = gattCharacteristic;
                        System.out.println("mModelNumberCharacteristic  " + mModelNumberCharacteristic.getUuid().toString());
                    } else if (uuid.equals(SerialPortUUID)) {
                        mSerialPortCharacteristic = gattCharacteristic;
                        System.out.println("mSerialPortCharacteristic  " + mSerialPortCharacteristic.getUuid().toString());
                    } else if (uuid.equals(CommandUUID)) {
                        mCommandCharacteristic = gattCharacteristic;
                        System.out.println("mSerialPortCharacteristic  " + mSerialPortCharacteristic.getUuid().toString());
                    }
                }
                this.mGattCharacteristics.add(charas);
            }
            if (mModelNumberCharacteristic == null || mSerialPortCharacteristic == null || mCommandCharacteristic == null) {
                Toast.makeText(this.mainContext, "Please select DFRobot devices", 0).show();
                this.mConnectionState = connectionStateEnum.isToScan;
                onConectionStateChange(this.mConnectionState);
                return;
            }
            mSCharacteristic = mModelNumberCharacteristic;
            this.mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
            this.mBluetoothLeService.readCharacteristic(mSCharacteristic);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
