package com.example.podcast.dontbackout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.time.format.TextStyle;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private HomeFragmentListener homeListener;

    private Typeface boldFont;
    private ImageView leftButton;
    private ImageView centerButton;
    private ImageView rightButton;
    private TextView topText;
    private Button startStopText;
    private RelativeLayout progressBar;
    private Typeface regularFont;
    private ImageView visualImage;
    private Drawable[] backImages;
    private int imageIndex = 0;
    private boolean notification = true;
    private int step = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    public interface HomeFragmentListener{
        public boolean loadBluetooth(TextView t, View start, View progress);
        public boolean straightCalibrate(TextView t, View start, View progress);
        public boolean slouchCalibrate(TextView t, View start, View progress);
        public void stopCalibrate(TextView t, View start, View progress);
        public void changeNotificationStatus();
        public void stopCalibration();

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private void initializeGUI(View view){
        this.backImages = new Drawable[]{getActivity().getDrawable(R.drawable.straight_with_circle),
                getActivity().getDrawable(R.drawable.slouched_with_circle)};

        this.visualImage = (ImageView) view.findViewById(R.id.straightView);

        this.startStopText = view.findViewById(R.id.startStopText);
        this.boldFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        this.regularFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AvenirNextLTPro-Regular.otf");

        this.startStopText.setTypeface(boldFont);
        this.startStopText.setTextColor(Color.WHITE);

        this.leftButton = (ImageView) view.findViewById(R.id.recalibrateButton);
        this.centerButton = (ImageView) view.findViewById(R.id.status);
        this.rightButton = (ImageView) view.findViewById(R.id.notificationButton);

        this.topText = (TextView) view.findViewById(R.id.topPlaceText);
        this.topText.setTypeface(regularFont);
        this.topText.setTextColor(Color.WHITE);
        
        // trying to replace start with loading progress bar BUT onclick won't hide the Views
        this.progressBar = (RelativeLayout) view.findViewById(R.id.loadingPanel);
        this.progressBar.setVisibility(SurfaceView.GONE);
        this.startStopText.setVisibility(View.VISIBLE);
        this.startStopText.setOnClickListener(new View.OnClickListener() {
            // 0 - needs to connect, 1 - needs to calibrate straight, 2 - needs to calibrate slouch, 3 - stop
            private String[] words = new String[]{"connect", "calibrate", "calibrate", "stop"};

            @Override
            public void onClick(View v) {
                startStopText.setVisibility(View.GONE);
                if (step == 0){
                    topText.setTextSize(24);
                    homeListener.loadBluetooth(topText, startStopText, progressBar);
                }
                else if (step == 1) {
                    homeListener.straightCalibrate(topText, startStopText, progressBar);


//                    if (!status){
//                        step = -1;
//                    }
                }
                else if (step == 2){
                    homeListener.slouchCalibrate(topText, startStopText, progressBar);

//                    if (!status){
//                        step = -1;
//                    }
                }
                else{
                    homeListener.stopCalibrate(topText, startStopText, progressBar);
                }
                step = (step + 1) % 3;
                startStopText.setVisibility(View.VISIBLE);
                startStopText.setText(words[step]);
            }
        });

        this.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification){
                    rightButton.setImageDrawable(getActivity().getDrawable(R.drawable.right_2));
                }
                else{
                    rightButton.setImageDrawable(getActivity().getDrawable(R.drawable.right_1));
                }
                notification = !notification;
                homeListener.changeNotificationStatus();
            }
        });

        this.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeListener.stopCalibration();
                if (!homeListener.straightCalibrate(topText, startStopText, progressBar)){
                    step = 0;
                    startStopText.setText("connect");
                }
                else {
                    step = 2;
                    startStopText.setText("calibrate");
                }
            }
    });


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initializeGUI(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragmentListener) {
            homeListener = (HomeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeListener = null;
    }

    // keep this empty method for now
    public void setButtonScanText(String x){
//        buttonScan.setText(x);
    }

    public void changeImage(int index){
        if (index == imageIndex){
            return;
        }
        visualImage.setImageDrawable(backImages[index]);
        imageIndex = index;
    }

    public void showImageConnected(){
        this.centerButton.setImageDrawable(getActivity().getDrawable(R.drawable.center_check));
    }

    public void showImageNotConnected(){
        this.centerButton.setImageDrawable(getActivity().getDrawable(R.drawable.center_x));
    }

    public void setTopText(String t){
        this.topText.setText(t);
    }

    public String getStartText(){
        return (String) this.startStopText.getText();
    }

    public void resetStep(){
        step = 0;
        startStopText.setText("connect");
    }
}
