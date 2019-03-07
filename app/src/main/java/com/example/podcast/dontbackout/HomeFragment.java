package com.example.podcast.dontbackout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


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
    public HomeFragment() {
        // Required empty public constructor
    }

    public interface HomeFragmentListener{
        public void straightCalibrate(TextView t);
        public void slouchCalibrate(TextView t);
        public void reCalibrate(TextView t);
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

    private void initializeGUI(){
        // button scan should go on the top app bar
        this.buttonScan = (Button) findViewById(R.id.buttonScan);
        if (buttonScan == null){
            Log.e("NULL", "BUTTONSCAN IS NULL");
            return;
        }
//        this.buttonScan.setOnClickListener(new MainActivity.C01452());
        this.statusText = (TextView) findViewById(R.id.textBackStatus);
        this.buttonCalibrate = (Button) findViewById(R.id.buttonCalibrate);
//        this.buttonCalibrate.setOnClickListener(new MainActivity.calibrateEvent());
        this.calibrateText = (TextView) findViewById(R.id.textCalibrate);
        this.buttonCalibrateSlouch = (Button) findViewById(R.id.buttonCalibrateSlouch);
//        this.buttonCalibrateSlouch.setOnClickListener(new MainActivity.calibrateSlouchEvent());
        this.buttonRecalibrate = (Button) findViewById(R.id.buttonRecalibrate);
//        this.buttonRecalibrate.setOnClickListener(new MainActivity.reCalibrate());
        this.buttonCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BUTTON PRESS", "buttonCalibrate is being clicked.");
                buttonCalibrate.setVisibility(View.INVISIBLE);
                homeListener.straightCalibrate(calibrateText);
                buttonCalibrateSlouch.setVisibility(View.VISIBLE);
            }
        });
        this.buttonCalibrateSlouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCalibrateSlouch.setVisibility(View.INVISIBLE);
                homeListener.slouchCalibrate(calibrateText);
                buttonRecalibrate.setVisibility(View.VISIBLE);
            }
        });
        this.buttonRecalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCalibrate.setVisibility(View.VISIBLE);
                homeListener.reCalibrate(calibrateText);
                buttonRecalibrate.setVisibility(View.INVISIBLE);
            }
        });

        this.buttonCalibrateSlouch.setVisibility(View.INVISIBLE);
        this.buttonRecalibrate.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initializeGUI();
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
        buttonScan.setText(x);
    }
}
