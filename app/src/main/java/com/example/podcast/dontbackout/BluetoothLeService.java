package com.example.podcast.dontbackout;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class BluetoothLeService extends Service {
    public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    private static final int MAX_CHARACTERISTIC_LENGTH = 17;
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 0;
    private static final String TAG = BluetoothLeService.class.getSimpleName();
    private static final int WRITE_NEW_CHARACTERISTIC = -1;
    private final IBinder mBinder = new LocalBinder();
    private BluetoothAdapter mBluetoothAdapter;
    public String mBluetoothDeviceAddress;
    BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    private RingBuffer<BluetoothGattCharacteristicHelper> mCharacteristicRingBuffer = new RingBuffer(8);
    public int mConnectionState = 0;
    private final BluetoothGattCallback mGattCallback = new C01341();
    private boolean mIsWritingCharacteristic = false;

    /* renamed from: com.dfrobot.angelo.blunobasicdemo.BluetoothLeService$1 */
    class C01341 extends BluetoothGattCallback {
        C01341() {
        }

        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            System.out.println("BluetoothGattCallback----onConnectionStateChange" + newState);
            String intentAction;
            if (newState == 2) {
                intentAction = BluetoothLeService.ACTION_GATT_CONNECTED;
                BluetoothLeService.this.mConnectionState = 2;
                BluetoothLeService.this.broadcastUpdate(intentAction);
                Log.i(BluetoothLeService.TAG, "Connected to GATT server.");
                if (BluetoothLeService.this.mBluetoothGatt.discoverServices()) {
                    Log.i(BluetoothLeService.TAG, "Attempting to start service discovery:");
                } else {
                    Log.i(BluetoothLeService.TAG, "Attempting to start service discovery:not success");
                }
            } else if (newState == 0) {
                intentAction = BluetoothLeService.ACTION_GATT_DISCONNECTED;
                BluetoothLeService.this.mConnectionState = 0;
                Log.i(BluetoothLeService.TAG, "Disconnected from GATT server.");
                BluetoothLeService.this.broadcastUpdate(intentAction);
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            System.out.println("onServicesDiscovered " + status);
            if (status == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(BluetoothLeService.TAG, "onServicesDiscovered received: " + status);
            }
        }

        public void onCharacteristicWrite(android.bluetooth.BluetoothGatt r8, android.bluetooth.BluetoothGattCharacteristic r9, int r10) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Exception block dominator not found, method:com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.1.onCharacteristicWrite(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int):void. bs: [B:12:0x004e, B:28:0x00d6, B:48:0x0183, B:63:0x022a]
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:86)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1157058691.run(Unknown Source)
*/
            /*
            r7 = this;
            r6 = 17;
            monitor-enter(r7);
            if (r10 != 0) goto L_0x0158;
        L_0x0005:
            r2 = java.lang.System.out;	 Catch:{ all -> 0x00a2 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
            r3.<init>();	 Catch:{ all -> 0x00a2 }
            r4 = "onCharacteristicWrite success:";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = new java.lang.String;	 Catch:{ all -> 0x00a2 }
            r5 = r9.getValue();	 Catch:{ all -> 0x00a2 }
            r4.<init>(r5);	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r3 = r3.toString();	 Catch:{ all -> 0x00a2 }
            r2.println(r3);	 Catch:{ all -> 0x00a2 }
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mCharacteristicRingBuffer;	 Catch:{ all -> 0x00a2 }
            r2 = r2.isEmpty();	 Catch:{ all -> 0x00a2 }
            if (r2 == 0) goto L_0x003a;	 Catch:{ all -> 0x00a2 }
        L_0x0032:
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r3 = 0;	 Catch:{ all -> 0x00a2 }
            r2.mIsWritingCharacteristic = r3;	 Catch:{ all -> 0x00a2 }
        L_0x0038:
            monitor-exit(r7);	 Catch:{ all -> 0x00a2 }
            return;	 Catch:{ all -> 0x00a2 }
        L_0x003a:
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mCharacteristicRingBuffer;	 Catch:{ all -> 0x00a2 }
            r0 = r2.next();	 Catch:{ all -> 0x00a2 }
            r0 = (com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.BluetoothGattCharacteristicHelper) r0;	 Catch:{ all -> 0x00a2 }
            r2 = r0.mCharacteristicValue;	 Catch:{ all -> 0x00a2 }
            r2 = r2.length();	 Catch:{ all -> 0x00a2 }
            if (r2 <= r6) goto L_0x00d6;
        L_0x004e:
            r2 = r0.mCharacteristic;	 Catch:{ UnsupportedEncodingException -> 0x00a5 }
            r3 = r0.mCharacteristicValue;	 Catch:{ UnsupportedEncodingException -> 0x00a5 }
            r4 = 0;	 Catch:{ UnsupportedEncodingException -> 0x00a5 }
            r5 = 17;	 Catch:{ UnsupportedEncodingException -> 0x00a5 }
            r3 = r3.substring(r4, r5);	 Catch:{ UnsupportedEncodingException -> 0x00a5 }
            r4 = "ISO-8859-1";	 Catch:{ UnsupportedEncodingException -> 0x00a5 }
            r3 = r3.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x00a5 }
            r2.setValue(r3);	 Catch:{ UnsupportedEncodingException -> 0x00a5 }
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mBluetoothGatt;	 Catch:{ all -> 0x00a2 }
            r3 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r2 = r2.writeCharacteristic(r3);	 Catch:{ all -> 0x00a2 }
            if (r2 == 0) goto L_0x00ac;	 Catch:{ all -> 0x00a2 }
        L_0x006e:
            r2 = java.lang.System.out;	 Catch:{ all -> 0x00a2 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
            r3.<init>();	 Catch:{ all -> 0x00a2 }
            r4 = "writeCharacteristic init ";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = new java.lang.String;	 Catch:{ all -> 0x00a2 }
            r5 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r5 = r5.getValue();	 Catch:{ all -> 0x00a2 }
            r4.<init>(r5);	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = ":success";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r3 = r3.toString();	 Catch:{ all -> 0x00a2 }
            r2.println(r3);	 Catch:{ all -> 0x00a2 }
        L_0x0097:
            r2 = r0.mCharacteristicValue;	 Catch:{ all -> 0x00a2 }
            r3 = 17;	 Catch:{ all -> 0x00a2 }
            r2 = r2.substring(r3);	 Catch:{ all -> 0x00a2 }
            r0.mCharacteristicValue = r2;	 Catch:{ all -> 0x00a2 }
            goto L_0x0038;	 Catch:{ all -> 0x00a2 }
        L_0x00a2:
            r2 = move-exception;	 Catch:{ all -> 0x00a2 }
            monitor-exit(r7);	 Catch:{ all -> 0x00a2 }
            throw r2;
        L_0x00a5:
            r1 = move-exception;
            r2 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00a2 }
            r2.<init>(r1);	 Catch:{ all -> 0x00a2 }
            throw r2;	 Catch:{ all -> 0x00a2 }
        L_0x00ac:
            r2 = java.lang.System.out;	 Catch:{ all -> 0x00a2 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
            r3.<init>();	 Catch:{ all -> 0x00a2 }
            r4 = "writeCharacteristic init ";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = new java.lang.String;	 Catch:{ all -> 0x00a2 }
            r5 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r5 = r5.getValue();	 Catch:{ all -> 0x00a2 }
            r4.<init>(r5);	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = ":failure";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r3 = r3.toString();	 Catch:{ all -> 0x00a2 }
            r2.println(r3);	 Catch:{ all -> 0x00a2 }
            goto L_0x0097;
        L_0x00d6:
            r2 = r0.mCharacteristic;	 Catch:{ UnsupportedEncodingException -> 0x0127 }
            r3 = r0.mCharacteristicValue;	 Catch:{ UnsupportedEncodingException -> 0x0127 }
            r4 = "ISO-8859-1";	 Catch:{ UnsupportedEncodingException -> 0x0127 }
            r3 = r3.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x0127 }
            r2.setValue(r3);	 Catch:{ UnsupportedEncodingException -> 0x0127 }
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mBluetoothGatt;	 Catch:{ all -> 0x00a2 }
            r3 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r2 = r2.writeCharacteristic(r3);	 Catch:{ all -> 0x00a2 }
            if (r2 == 0) goto L_0x012e;	 Catch:{ all -> 0x00a2 }
        L_0x00ef:
            r2 = java.lang.System.out;	 Catch:{ all -> 0x00a2 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
            r3.<init>();	 Catch:{ all -> 0x00a2 }
            r4 = "writeCharacteristic init ";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = new java.lang.String;	 Catch:{ all -> 0x00a2 }
            r5 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r5 = r5.getValue();	 Catch:{ all -> 0x00a2 }
            r4.<init>(r5);	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = ":success";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r3 = r3.toString();	 Catch:{ all -> 0x00a2 }
            r2.println(r3);	 Catch:{ all -> 0x00a2 }
        L_0x0118:
            r2 = "";	 Catch:{ all -> 0x00a2 }
            r0.mCharacteristicValue = r2;	 Catch:{ all -> 0x00a2 }
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mCharacteristicRingBuffer;	 Catch:{ all -> 0x00a2 }
            r2.pop();	 Catch:{ all -> 0x00a2 }
            goto L_0x0038;	 Catch:{ all -> 0x00a2 }
        L_0x0127:
            r1 = move-exception;	 Catch:{ all -> 0x00a2 }
            r2 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00a2 }
            r2.<init>(r1);	 Catch:{ all -> 0x00a2 }
            throw r2;	 Catch:{ all -> 0x00a2 }
        L_0x012e:
            r2 = java.lang.System.out;	 Catch:{ all -> 0x00a2 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
            r3.<init>();	 Catch:{ all -> 0x00a2 }
            r4 = "writeCharacteristic init ";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = new java.lang.String;	 Catch:{ all -> 0x00a2 }
            r5 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r5 = r5.getValue();	 Catch:{ all -> 0x00a2 }
            r4.<init>(r5);	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = ":failure";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r3 = r3.toString();	 Catch:{ all -> 0x00a2 }
            r2.println(r3);	 Catch:{ all -> 0x00a2 }
            goto L_0x0118;	 Catch:{ all -> 0x00a2 }
        L_0x0158:
            r2 = -1;	 Catch:{ all -> 0x00a2 }
            if (r10 != r2) goto L_0x02ac;	 Catch:{ all -> 0x00a2 }
        L_0x015b:
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mCharacteristicRingBuffer;	 Catch:{ all -> 0x00a2 }
            r2 = r2.isEmpty();	 Catch:{ all -> 0x00a2 }
            if (r2 != 0) goto L_0x01d6;	 Catch:{ all -> 0x00a2 }
        L_0x0167:
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mIsWritingCharacteristic;	 Catch:{ all -> 0x00a2 }
            if (r2 != 0) goto L_0x01d6;	 Catch:{ all -> 0x00a2 }
        L_0x016f:
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mCharacteristicRingBuffer;	 Catch:{ all -> 0x00a2 }
            r0 = r2.next();	 Catch:{ all -> 0x00a2 }
            r0 = (com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.BluetoothGattCharacteristicHelper) r0;	 Catch:{ all -> 0x00a2 }
            r2 = r0.mCharacteristicValue;	 Catch:{ all -> 0x00a2 }
            r2 = r2.length();	 Catch:{ all -> 0x00a2 }
            if (r2 <= r6) goto L_0x022a;
        L_0x0183:
            r2 = r0.mCharacteristic;	 Catch:{ UnsupportedEncodingException -> 0x01f9 }
            r3 = r0.mCharacteristicValue;	 Catch:{ UnsupportedEncodingException -> 0x01f9 }
            r4 = 0;	 Catch:{ UnsupportedEncodingException -> 0x01f9 }
            r5 = 17;	 Catch:{ UnsupportedEncodingException -> 0x01f9 }
            r3 = r3.substring(r4, r5);	 Catch:{ UnsupportedEncodingException -> 0x01f9 }
            r4 = "ISO-8859-1";	 Catch:{ UnsupportedEncodingException -> 0x01f9 }
            r3 = r3.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x01f9 }
            r2.setValue(r3);	 Catch:{ UnsupportedEncodingException -> 0x01f9 }
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mBluetoothGatt;	 Catch:{ all -> 0x00a2 }
            r3 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r2 = r2.writeCharacteristic(r3);	 Catch:{ all -> 0x00a2 }
            if (r2 == 0) goto L_0x0200;	 Catch:{ all -> 0x00a2 }
        L_0x01a3:
            r2 = java.lang.System.out;	 Catch:{ all -> 0x00a2 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
            r3.<init>();	 Catch:{ all -> 0x00a2 }
            r4 = "writeCharacteristic init ";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = new java.lang.String;	 Catch:{ all -> 0x00a2 }
            r5 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r5 = r5.getValue();	 Catch:{ all -> 0x00a2 }
            r4.<init>(r5);	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = ":success";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r3 = r3.toString();	 Catch:{ all -> 0x00a2 }
            r2.println(r3);	 Catch:{ all -> 0x00a2 }
        L_0x01cc:
            r2 = r0.mCharacteristicValue;	 Catch:{ all -> 0x00a2 }
            r3 = 17;	 Catch:{ all -> 0x00a2 }
            r2 = r2.substring(r3);	 Catch:{ all -> 0x00a2 }
            r0.mCharacteristicValue = r2;	 Catch:{ all -> 0x00a2 }
        L_0x01d6:
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r3 = 1;	 Catch:{ all -> 0x00a2 }
            r2.mIsWritingCharacteristic = r3;	 Catch:{ all -> 0x00a2 }
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mCharacteristicRingBuffer;	 Catch:{ all -> 0x00a2 }
            r2 = r2.isFull();	 Catch:{ all -> 0x00a2 }
            if (r2 == 0) goto L_0x0038;	 Catch:{ all -> 0x00a2 }
        L_0x01e8:
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mCharacteristicRingBuffer;	 Catch:{ all -> 0x00a2 }
            r2.clear();	 Catch:{ all -> 0x00a2 }
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r3 = 0;	 Catch:{ all -> 0x00a2 }
            r2.mIsWritingCharacteristic = r3;	 Catch:{ all -> 0x00a2 }
            goto L_0x0038;	 Catch:{ all -> 0x00a2 }
        L_0x01f9:
            r1 = move-exception;	 Catch:{ all -> 0x00a2 }
            r2 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00a2 }
            r2.<init>(r1);	 Catch:{ all -> 0x00a2 }
            throw r2;	 Catch:{ all -> 0x00a2 }
        L_0x0200:
            r2 = java.lang.System.out;	 Catch:{ all -> 0x00a2 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
            r3.<init>();	 Catch:{ all -> 0x00a2 }
            r4 = "writeCharacteristic init ";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = new java.lang.String;	 Catch:{ all -> 0x00a2 }
            r5 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r5 = r5.getValue();	 Catch:{ all -> 0x00a2 }
            r4.<init>(r5);	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = ":failure";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r3 = r3.toString();	 Catch:{ all -> 0x00a2 }
            r2.println(r3);	 Catch:{ all -> 0x00a2 }
            goto L_0x01cc;
        L_0x022a:
            r2 = r0.mCharacteristic;	 Catch:{ UnsupportedEncodingException -> 0x027b }
            r3 = r0.mCharacteristicValue;	 Catch:{ UnsupportedEncodingException -> 0x027b }
            r4 = "ISO-8859-1";	 Catch:{ UnsupportedEncodingException -> 0x027b }
            r3 = r3.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x027b }
            r2.setValue(r3);	 Catch:{ UnsupportedEncodingException -> 0x027b }
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mBluetoothGatt;	 Catch:{ all -> 0x00a2 }
            r3 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r2 = r2.writeCharacteristic(r3);	 Catch:{ all -> 0x00a2 }
            if (r2 == 0) goto L_0x0282;	 Catch:{ all -> 0x00a2 }
        L_0x0243:
            r2 = java.lang.System.out;	 Catch:{ all -> 0x00a2 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
            r3.<init>();	 Catch:{ all -> 0x00a2 }
            r4 = "writeCharacteristic init ";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = new java.lang.String;	 Catch:{ all -> 0x00a2 }
            r5 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r5 = r5.getValue();	 Catch:{ all -> 0x00a2 }
            r4.<init>(r5);	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = ":success";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r3 = r3.toString();	 Catch:{ all -> 0x00a2 }
            r2.println(r3);	 Catch:{ all -> 0x00a2 }
        L_0x026c:
            r2 = "";	 Catch:{ all -> 0x00a2 }
            r0.mCharacteristicValue = r2;	 Catch:{ all -> 0x00a2 }
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mCharacteristicRingBuffer;	 Catch:{ all -> 0x00a2 }
            r2.pop();	 Catch:{ all -> 0x00a2 }
            goto L_0x01d6;	 Catch:{ all -> 0x00a2 }
        L_0x027b:
            r1 = move-exception;	 Catch:{ all -> 0x00a2 }
            r2 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00a2 }
            r2.<init>(r1);	 Catch:{ all -> 0x00a2 }
            throw r2;	 Catch:{ all -> 0x00a2 }
        L_0x0282:
            r2 = java.lang.System.out;	 Catch:{ all -> 0x00a2 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
            r3.<init>();	 Catch:{ all -> 0x00a2 }
            r4 = "writeCharacteristic init ";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = new java.lang.String;	 Catch:{ all -> 0x00a2 }
            r5 = r0.mCharacteristic;	 Catch:{ all -> 0x00a2 }
            r5 = r5.getValue();	 Catch:{ all -> 0x00a2 }
            r4.<init>(r5);	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = ":failure";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r3 = r3.toString();	 Catch:{ all -> 0x00a2 }
            r2.println(r3);	 Catch:{ all -> 0x00a2 }
            goto L_0x026c;	 Catch:{ all -> 0x00a2 }
        L_0x02ac:
            r2 = com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.this;	 Catch:{ all -> 0x00a2 }
            r2 = r2.mCharacteristicRingBuffer;	 Catch:{ all -> 0x00a2 }
            r2.clear();	 Catch:{ all -> 0x00a2 }
            r2 = java.lang.System.out;	 Catch:{ all -> 0x00a2 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
            r3.<init>();	 Catch:{ all -> 0x00a2 }
            r4 = "onCharacteristicWrite fail:";	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r4 = new java.lang.String;	 Catch:{ all -> 0x00a2 }
            r5 = r9.getValue();	 Catch:{ all -> 0x00a2 }
            r4.<init>(r5);	 Catch:{ all -> 0x00a2 }
            r3 = r3.append(r4);	 Catch:{ all -> 0x00a2 }
            r3 = r3.toString();	 Catch:{ all -> 0x00a2 }
            r2.println(r3);	 Catch:{ all -> 0x00a2 }
            r2 = java.lang.System.out;	 Catch:{ all -> 0x00a2 }
            r2.println(r10);	 Catch:{ all -> 0x00a2 }
            goto L_0x0038;
            */
//            throw new UnsupportedOperationException("Method not decompiled: com.dfrobot.angelo.blunobasicdemo.BluetoothLeService.1.onCharacteristicWrite(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int):void");
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == 0) {
                System.out.println("onCharacteristicRead  " + characteristic.getUuid().toString());
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor characteristic, int status) {
            System.out.println("onDescriptorWrite  " + characteristic.getUuid().toString() + " " + status);
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            System.out.println("onCharacteristicChanged  " + new String(characteristic.getValue()));
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, characteristic);
        }
    }

    private class BluetoothGattCharacteristicHelper {
        BluetoothGattCharacteristic mCharacteristic;
        String mCharacteristicValue;

        BluetoothGattCharacteristicHelper(BluetoothGattCharacteristic characteristic, String characteristicValue) {
            this.mCharacteristic = characteristic;
            this.mCharacteristicValue = characteristicValue;
        }
    }

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    private void broadcastUpdate(String action) {
        sendBroadcast(new Intent(action));
    }

    private void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(action);
        System.out.println("BluetoothLeService broadcastUpdate");
        byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            intent.putExtra(EXTRA_DATA, new String(data));
            sendBroadcast(intent);
        }
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    public boolean initialize() {
        System.out.println("BluetoothLeService initialize" + this.mBluetoothManager);
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
            if (this.mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        if (this.mBluetoothAdapter != null) {
            return true;
        }
        Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
        return false;
    }

    public boolean connect(String address) {
        System.out.println("BluetoothLeService connect" + address + this.mBluetoothGatt);
        if (this.mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        BluetoothDevice device = this.mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        System.out.println("device.connectGatt connect");
        synchronized (this) {
            this.mBluetoothGatt = device.connectGatt(this, false, this.mGattCallback);
        }
        Log.d(TAG, "Trying to create a new connection.");
        this.mBluetoothDeviceAddress = address;
        this.mConnectionState = 1;
        return true;
    }

    public void disconnect() {
        System.out.println("BluetoothLeService disconnect");
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.disconnect();
        }
    }

    public void close() {
        System.out.println("BluetoothLeService close");
        if (this.mBluetoothGatt != null) {
            this.mBluetoothGatt.close();
            this.mBluetoothGatt = null;
        }
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.readCharacteristic(characteristic);
        }
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        try {
            String writeCharacteristicString = new String(characteristic.getValue(), "ISO-8859-1");
            System.out.println("allwriteCharacteristicString:" + writeCharacteristicString);
            this.mCharacteristicRingBuffer.push(new BluetoothGattCharacteristicHelper(characteristic, writeCharacteristicString));
            System.out.println("mCharacteristicRingBufferlength:" + this.mCharacteristicRingBuffer.size());
            this.mGattCallback.onCharacteristicWrite(this.mBluetoothGatt, characteristic, -1);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (this.mBluetoothGatt == null) {
            return null;
        }
        return this.mBluetoothGatt.getServices();
    }
}
