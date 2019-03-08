package com.example.podcast.dontbackout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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

//// calibrating straight back
//class calibrateEvent implements View.OnClickListener{
//    calibrateEvent(){
//    }
//    public void onClick(View v){
//        straightCalibrate = true;
//        calibrateText.setText("Calibrating straight...");
//        MainActivity.this.buttonCalibrate.setVisibility(View.INVISIBLE);
//        new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                calibrateText.setText("Stay straight for another: " + (millisUntilFinished/1000) + "seconds");
//            }
//
//            @Override
//            public void onFinish() {
//                calibrateText.setText("Done calibrating straight. " + posture.getStraightSize() + " entries added.");
//                straightCalibrate = false;
//                MainActivity.this.buttonCalibrateSlouch.setVisibility(View.VISIBLE);
//            }
//        }.start();
//
//
//
//    }
//}
//
//class calibrateSlouchEvent implements View.OnClickListener{
//
//    public calibrateSlouchEvent(){ }
//    public void onClick(View v){
//        slouchCalibrate = true;
//        calibrateText.setText("Calibrating slouch...");
//        MainActivity.this.buttonCalibrateSlouch.setVisibility(View.INVISIBLE);
//        new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                calibrateText.setText("Stay slouched for another: " + (millisUntilFinished/1000) + "seconds");
//            }
//
//            @Override
//            public void onFinish() {
//                calibrateText.setText("Done calibrating slouch. " + posture.getSlouchSize() + " entries added.");
//                slouchCalibrate = false;
//                MainActivity.this.buttonRecalibrate.setVisibility(View.VISIBLE);
//                calibrated = true;
//            }
//        }.start();
//    }
//}
//
//
//class reCalibrate implements View.OnClickListener{
//    public reCalibrate(){}
//    // calibrating straight back
//    class calibrateEvent implements View.OnClickListener{
//        calibrateEvent(){
//        }
//        public void onClick(View v){
//            straightCalibrate = true;
//            calibrateText.setText("Calibrating straight...");
//            MainActivity.this.buttonCalibrate.setVisibility(View.INVISIBLE);
//            new CountDownTimer(CALIBRATE_TIME, INCREMENT_TIME) {
//                @Override
//                public void onTick(long millisUntilFinished) {
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
//}

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button buttonScan;
    private Button buttonCalibrate;
    private Button buttonCalibrateSlouch;
    private Button buttonRecalibrate;
    private TextView statusText;
    public TextView calibrateText;

    private HomeFragmentListener homeListener;

    private Typeface boldFont;
    private ImageView leftButton;
    private ImageView centerButton;
    private ImageView rightButton;
    private TextView topText;
    private TextView startStopText;
    private RelativeLayout progressBar;
    private Typeface regularFont;

    public HomeFragment() {
        // Required empty public constructor
    }

    public interface HomeFragmentListener{
        public void loadBluetooth(TextView t, View start, View progress);
        public void straightCalibrate(TextView t, View start, View progress);
        public void slouchCalibrate(TextView t, View start, View progress);
        public void stopCalibrate(TextView t, View start, View progress);
        public void changeVisibility(View v, int visibility);
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
        this.startStopText = (TextView) view.findViewById(R.id.startStopText);
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
            private int step = 0;
            private String[] words = new String[]{"start", "calibrate", "calibrate", "stop"};

            @Override
            public void onClick(View v) {
                startStopText.setVisibility(View.GONE);
                if (step == 0){
                    homeListener.loadBluetooth(topText, startStopText, progressBar);

                }
                else if (step == 1) {
                    homeListener.straightCalibrate(topText, startStopText, progressBar);
                }
                else if (step == 2){
                    homeListener.slouchCalibrate(topText, startStopText, progressBar);
                }
                else{
                    homeListener.stopCalibrate(topText, startStopText, progressBar);
                }
                step = (step + 1) % 4;
                startStopText.setVisibility(View.VISIBLE);
                startStopText.setText(words[step]);
            }
        });


    }

    private void makeVisible(View v){
        v.setVisibility(View.VISIBLE);
    }

    private void makeGone(View v){
        v.setVisibility(View.GONE);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private View findViewById(int id){
        return getView().findViewById(id);
    }

    public void setButtonScanText(String x){
//        buttonScan.setText(x);
    }

    public void changeImage(){

    }
}
