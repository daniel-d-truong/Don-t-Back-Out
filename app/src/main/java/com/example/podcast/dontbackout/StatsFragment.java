package com.example.podcast.dontbackout;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatsFragment.StatsFragmentListener} interface
 * to handle interaction events.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RelativeLayout statsLayout;

    private StatsFragmentListener mListener;
    private Typeface regularFont;

    private double seconds;
    private int minutes;

    private final double INCREMENT_TIME = 0.4;
    private TextView updateText;

    public StatsFragment() {
        // Required empty public constructor
        seconds = 0.0;
        minutes = 0;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initializeGUI(view);
    }

    private void initializeGUI(View view) {
        this.statsLayout = view.findViewById(R.id.statsLayout);
        this.regularFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        for (int i = 0; i < statsLayout.getChildCount(); i++){
            CardView cv = (CardView) statsLayout.getChildAt(i);
            for (int j = 0; j < cv.getChildCount(); j++){
                TextView v = (TextView) cv.getChildAt(j);
                if (v == null){
                    throw new NullPointerException("textview is null/"+ statsLayout.getChildCount());
                }
                v.setTypeface(this.regularFont);
            }
        }
        this.updateText = view.findViewById(R.id.right1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StatsFragmentListener) {
            mListener = (StatsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StatsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface StatsFragmentListener {
        // TODO: Update argument type and name
        public void incrementGraph(int min, int sec);
    }

    public void increaseTimeText(){
        seconds+=INCREMENT_TIME;
        String t = "";

        minutes += (int) seconds/60;
        if (seconds/60 == 1){
            mListener.incrementGraph(minutes, (int) seconds);
        }
        seconds%=60;
        if (minutes != 0){
            t+=minutes + " minutes ";
        }
        t+=(int)seconds + " seconds";
        this.updateText.setText(t);
    }
}
