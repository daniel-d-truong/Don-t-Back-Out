package com.example.podcast.dontbackout;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;

import java.util.ArrayList;
import java.util.List;

import static android.app.Notification.BADGE_ICON_SMALL;

public class MainActivity extends BlunoLibrary implements HomeFragment.HomeFragmentListener, StatsFragment.StatsFragmentListener, GraphFragment.GraphFragmentListener {
    private Posture posture;
    private final long CALIBRATE_TIME = 3500;
    private final long INCREMENT_TIME = 1000;
    private HomeFragment homeFragment;
    private StatsFragment statsFragment;
    private AHBottomNavigation navigation;
    private boolean notifications = true;
    private boolean bluetoothConnection = false;

    // false if not calibrated yet, true if is calibrated
    private boolean calibrated;
    private boolean straightCalibrate;
    private boolean slouchCalibrate;

    // slouch counter to indicate when to notify the user
    private int counter;

    private ImageView imageView;

    private int imageState;
    private GraphFragment graphFragment;
    private ImageView scroll;

    @Override
    public boolean straightCalibrate(final TextView calibrateText, final View v, final View progress) {
        straightCalibrate = true;
        calibrateText.setText(getString(R.string.keep_straight));
        imageState = 0;
        homeFragment.changeImage(imageState);

        new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
            boolean loaded = false;
            @Override
            public void onTick(long millisUntilFinished) {
                if (!loaded){
                    v.setVisibility(View.INVISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    loaded = true;
                }
            }

            @Override
            public void onFinish() {
                if (posture.getStraightSize() == 0) {
                    homeFragment.setTopText(getString(R.string.failed_connection) );
                    homeFragment.resetStep();
                }
                else{
                    homeFragment.setTopText(getString(R.string.calibration_successful)  + "\nGet ready to calibrate slouch.");
                    Log.i("ENTRIES", ""+ posture.getStraightSize());
                }
                straightCalibrate = false;
                v.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                imageState = 1;
                homeFragment.changeImage(imageState);
            }
        }.start();
        Log.i("CALIBRATE SIZE", ""+posture.getStraightSize());
        return true;
    }
    @Override
    public boolean slouchCalibrate(final TextView calibrateText, final View start, final View progress) {
        slouchCalibrate = true;
        calibrateText.setText(getString(R.string.keep_slouched));


        new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
            boolean loaded = false;

            @Override
            public void onTick(long millisUntilFinished) {
                if (!loaded){
                    start.setVisibility(View.INVISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    loaded = true;
                }
            }

            @Override
            public void onFinish() {
                slouchCalibrate = false;
                calibrated = true;
                start.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                if (posture.getSlouchSize() == 0){
                    calibrateText.setText(getString(R.string.failed_connection));
                    homeFragment.resetStep();
                }
                else{
                    calibrateText.setText(getString(R.string.calibration_successful));
                }

            }
        }.start();
        return true;
    }

    @Override
    public void stopCalibrate(final TextView calibrateText, final View start, final View progress) {
        calibrated = false;
        posture.reset();
        calibrateText.setText("Stopped current calibration.");

    }

    @Override
    public boolean loadBluetooth(TextView t, View start, View progress){
        if (!bluetoothConnection){
            t.setText("Connect to \"Bluno\" bluetooth.\nGet ready to calibrate straight.");
            MainActivity.this.buttonScanOnClickProcess();
        }
        else{
            t.setText("Already connected to \"Bluno\" bluetooth");
        }
        posture.reset();
        straightCalibrate = false;
        slouchCalibrate = false;
        calibrated = false;
        imageState = 0;
        homeFragment.changeImage(imageState);
        return true;
    }

    @Override
    public void changeNotificationStatus(){
        this.notifications = !this.notifications;
    }

    @Override
    public void incrementGraph(int min, int sec) {
        graphFragment.addDataPoint(min);
    }

    @Override
    public void stopCalibration(){
        calibrated = false;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeGUI(){
        ViewPager pager = (ViewPager) findViewById(R.id.frame);
        scroll = (ImageView) findViewById(R.id.scrollDisplay);

        PagerAdapter pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
//        // idk about the color for this line
//        bundle.putInt("color", getTitleColor());
//
        homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
//
        statsFragment = new StatsFragment();
        statsFragment.setArguments(bundle);
        
//        graphFragment = new GraphFragment();
//        graphFragment.setArguments(bundle);
//
        ((BottomBarAdapter) pagerAdapter).addFragments(homeFragment);
        ((BottomBarAdapter) pagerAdapter).addFragments(statsFragment);
//        ((BottomBarAdapter) pagerAdapter).addFragments(graphFragment);
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0){
                    scroll.setImageDrawable(getDrawable(R.drawable.circle_1));
                }
                else if (i == 1){
                    scroll.setImageDrawable(getDrawable(R.drawable.circle_2));
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        imageView = findViewById(R.id.imageView);

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
                // homeFragment.setButtonScanText("Connected");
                homeFragment.showImageConnected();
                homeFragment.setTopText("Connected.");
                serialSend("CONNECTED");
                bluetoothConnection = true;
                return;
            case isConnecting:
                // homeFragment.setButtonScanText("Connecting");
                homeFragment.showImageNotConnected();
                bluetoothConnection = false;
                return;
            case isToScan:
                // homeFragment.setButtonScanText("Scan");
                homeFragment.showImageNotConnected();
                bluetoothConnection = false;
                return;
            case isScanning:
                // homeFragment.setButtonScanText("Scanning");
                return;
            case isDisconnecting:
                // homeFragment.setButtonScanText("isDisconnecting");
                homeFragment.showImageNotConnected();
                homeFragment.setTopText("Disconnected");
                bluetoothConnection = false;
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
            serialSend(String.valueOf(posture.getCriticalAngle()));
            if (straightFlag == null){ return; }
            if (straightFlag.equals("SLOUCHED")){
                homeFragment.setTopText("Slouched.");
                counter++;
                if (notifications && counter % 10 == 0){
                    addNotification();
                }
                imageState = 1;
            }
            else{
                homeFragment.setTopText("Straight.");
                counter = 0;
                imageState = 0;
                statsFragment.increaseTimeText();
            }
            homeFragment.changeImage(imageState);
        }
        else if (this.straightCalibrate){
            for (int i = 0; i < 50; i++) {
                posture.addStraightData(angles);
            }
            Log.i("Calibrating", "adding straight calibrated data");
        }
        else if (this.slouchCalibrate){
            for (int i = 0; i < 50; i++){
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
