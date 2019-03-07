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
import android.support.annotation.ColorRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BlunoLibrary implements HomeFragment.HomeFragmentListener {
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
    private HomeFragment homeFragment;

    public boolean isCalibrated() {
        return calibrated;
    }

    public void setCalibrated(boolean calibrated) {
        this.calibrated = calibrated;
    }

    public boolean isStraightCalibrate() {
        return straightCalibrate;
    }

    public void setStraightCalibrate(boolean straightCalibrate) {
        this.straightCalibrate = straightCalibrate;
    }

    public boolean isSlouchCalibrate() {
        return slouchCalibrate;
    }

    public void setSlouchCalibrate(boolean slouchCalibrate) {
        this.slouchCalibrate = slouchCalibrate;
    }

    // false if not calibrated yet, true if is calibrated
    private boolean calibrated;
    private boolean straightCalibrate;
    private boolean slouchCalibrate;

    // slouch counter to indicate when to notify the user
    private int counter;

    @Override
    public void straightCalibrate(final TextView calibrateText) {
        straightCalibrate = true;
        calibrateText.setText("Calibrating straight...");

        new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
            @Override
            public void onTick(long millisUntilFinished) {
                calibrateText.setText("Stay straight for another: " + (millisUntilFinished/1000) + "seconds");
            }

            @Override
            public void onFinish() {
                calibrateText.setText("Done calibrating straight. " + posture.getStraightSize() + " entries added.");
                straightCalibrate = false;

            }
        }.start();
    }

    @Override
    public void slouchCalibrate(final TextView calibrateText) {
        slouchCalibrate = true;
        calibrateText.setText("Calibrating slouch...");

        new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
            @Override
            public void onTick(long millisUntilFinished) {
                calibrateText.setText("Stay slouched for another: " + (millisUntilFinished/1000) + "seconds");
            }

            @Override
            public void onFinish() {
                calibrateText.setText("Done calibrating slouch. " + posture.getSlouchSize() + " entries added.");
                slouchCalibrate = false;

                calibrated = true;
            }
        }.start();
    }

    @Override
    public void reCalibrate(final TextView calibrateText) {
        calibrated = false;

        posture.reset();
        calibrateText.setText("Click calibrate button when you are ready.");

    }

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }


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
//    class calibrateEvent implements View.OnClickListener{
//        calibrateEvent(){
//        }
//        public void onClick(View v){
//            straightCalibrate = true;
//            calibrateText.setText("Calibrating straight...");
//            MainActivity.this.buttonCalibrate.setVisibility(View.INVISIBLE);
//            new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
//                @Override
//                public void             slouchCalibrate = true;
//            calibrateText.setText("Calibrating slouch...");
//            MainActivity.this.buttonCalibrateSlouch.setVisibility(View.INVISIBLE);
//            new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    calibrateText.setText("Stay slouched for another: " + (millisUntilFinished/1000) + "seconds");
//                }
//
//                @Override
//                public void onFinish() {
//                    calibrateText.setText("Done calibrating slouch. " + posture.getSlouchSize() + " entries added.");
//                    slouchCalibrate = false;
//                    MainActivity.this.buttonRecalibrate.setVisibility(View.VISIBLE);
//                    calibrated = true;
//                }
//            }.start();onTick(long millisUntilFinished) {
//                    calibrateText.setText("Stay straight for another: " + (millisUntilFinished/1000) + "seconds");
//                }
//
//                @Override
//                public void onFinish() {
//                    calibrateText.setText("Done calibrating straight. " + posture.getStraightSize() + " entries added.");
//                    straightCalibrate = false;
//                    MainActivity.this.buttonCalibrateSlouch.setVisibility(View.VISIBLE);
//                }
//            }.start();
//
//
//
//        }
//    }
//
//    class calibrateSlouchEvent implements View.OnClickListener{
//
//        public calibrateSlouchEvent(){ }
//        public void onClick(View v){
//            slouchCalibrate = true;
//            calibrateText.setText("Calibrating slouch...");
//            MainActivity.this.buttonCalibrateSlouch.setVisibility(View.INVISIBLE);
//            new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    calibrateText.setText("Stay slouched for another: " + (millisUntilFinished/1000) + "seconds");
//                }
//
//                @Override
//                public void onFinish() {
//                    calibrateText.setText("Done calibrating slouch. " + posture.getSlouchSize() + " entries added.");
//                    slouchCalibrate = false;
//                    MainActivity.this.buttonRecalibrate.setVisibility(View.VISIBLE);
//                    calibrated = true;
//                }
//            }.start();
//        }
//    }
//
//
//    class reCalibrate implements View.OnClickListener{
//        public reCalibrate(){}
//
//        @Override
//        public void onClick(View v) {
//            calibrated = false;
//            buttonCalibrate.setVisibility(View.VISIBLE);
//            posture.reset();
//            calibrateText.setText("Click calibrate button when you are ready.");
//            buttonRecalibrate.setVisibility(View.INVISIBLE);
//        }
//    }

    class tabTouchListener implements AHBottomNavigation.OnTabSelectedListener{
        ViewPager viewPager;
        public tabTouchListener(ViewPager viewPager){
            this.viewPager = viewPager;
        }
        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            if (!wasSelected)
                viewPager.setCurrentItem(position);

            return true;
        }
    }

    /*
    Extension of FragmentStatePagerAdapter which intelligently caches
    all active fragments and manages the fragment lifecycles.
    Usage involves extending from SmartFragmentStatePagerAdapter as you would any other PagerAdapter.
    */
    public abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        // Sparse array to keep track of registered fragments in memory
        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Register the fragment when the item is instantiated
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        // Unregister when the item is inactive
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        // Returns the fragment for the position (if instantiated)
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    public class BottomBarAdapter extends SmartFragmentStatePagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        public BottomBarAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        // Our custom method that populates this Adapter with Fragments
        public void addFragments(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public class NoSwipePager extends ViewPager {
        private boolean enabled;

        public NoSwipePager(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.enabled = true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (this.enabled) {
                return super.onTouchEvent(event);
            }
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            if (this.enabled) {
                return super.onInterceptTouchEvent(event);
            }
            return false;
        }

        public void setPagingEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    private void initializeGUI(){
        ViewPager pager = findViewById(R.id.frame);
//        pager.setPagingEnabled(false);
        PagerAdapter pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        // idk about the color for this line
        bundle.putInt("color", getTitleColor());

        homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        ((BottomBarAdapter) pagerAdapter).addFragments(homeFragment);
        pager.setAdapter(pagerAdapter);


        // creating navigation bar
        AHBottomNavigationItem homeItem =
                new AHBottomNavigationItem("Home", R.drawable.ic_launcher_background);

        AHBottomNavigationItem statsItem =
                new AHBottomNavigationItem("Stats", R.drawable.item_background);

        AHBottomNavigationItem aboutItem =
                new AHBottomNavigationItem("About", R.drawable.notification_background);

        AHBottomNavigation navigation = findViewById(R.id.bottom_navigation);

        navigation.addItem(homeItem);
        navigation.addItem(statsItem);
        navigation.addItem(aboutItem);

        navigation.setOnTabSelectedListener(new tabTouchListener(pager));
        navigation.setCurrentItem(0);

//        this.buttonScan.setOnClickListener(new MainActivity.C01452());
//        this.statusText = (TextView) findViewById(R.id.textBackStatus);
//        this.buttonCalibrate = (Button) findViewById(R.id.buttonCalibrate);
//        this.buttonCalibrate.setOnClickListener(new MainActivity.calibrateEvent());
//        this.calibrateText = (TextView) findViewById(R.id.textCalibrate);
//        this.buttonCalibrateSlouch = (Button) findViewById(R.id.buttonCalibrateSlouch);
//        this.buttonCalibrateSlouch.setOnClickListener(new MainActivity.calibrateSlouchEvent());
//        this.buttonRecalibrate = (Button) findViewById(R.id.buttonRecalibrate);
//        this.buttonRecalibrate.setOnClickListener(new MainActivity.reCalibrate());


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
        Toolbar toolbar = findViewById(R.id.my_app_toolbar);
        setSupportActionBar(toolbar);
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
                homeFragment.setButtonScanText("Connected");
                return;
            case isConnecting:
                homeFragment.setButtonScanText("Connecting");
                return;
            case isToScan:
                homeFragment.setButtonScanText("Scan");
                return;
            case isScanning:
                homeFragment.setButtonScanText("Scanning");
                return;
            case isDisconnecting:
                homeFragment.setButtonScanText("isDisconnecting");
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
