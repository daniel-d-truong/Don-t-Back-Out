package com.example.podcast.dontbackout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GraphFragmentListener graphListener;

    BarGraphSeries<DataPoint> barPoints;
    private double x;
    private GraphView graph;
    private DataPoint[] defaultDataPoints;
    private DataPoint[] dataPoints;

    public GraphFragment() {
        // Required empty public constructor
        barPoints = new BarGraphSeries<>();
        x = 0;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.DataDataPoint[] dataPoints; Point[] dataPoints;
     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance(String param1, String param2) {
        GraphFragment fragment = new GraphFragment();
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
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle b){
        this.initializeGUI(view);
    }

    private void initializeGUI(View view){
        graph = (GraphView) view.findViewById(R.id.graph);
        defaultDataPoints = new DataPoint[]{
                new DataPoint(0, (3.0 + (1/30))),
                new DataPoint(1, (59.0/60)),
                new DataPoint(2, 20.0/60),
                new DataPoint(3, 1 + (20.0/60)),
                null
        };
        dataPoints = new DataPoint[5];
        this.reAssignData();
        this.dataPoints[4] = new DataPoint(4, 0);
        barPoints = new BarGraphSeries<DataPoint>(
            dataPoints
        );
        barPoints.setSpacing(20);
        graph.addSeries(barPoints);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            String[] dates = new String[]{"03/09", "03/08", "03/07", "03/06", "03/05"};

            @Override
            public String formatLabel (double value, boolean isValueX){
                if (isValueX){
                    return dates[dates.length - 1 - (int) value];
                }
                else{
                    return super.formatLabel(value, isValueX) + " hours";
                }
            }
        });
        graph.setTitle("Time Stayed Stright over Time");
        setBounds();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GraphFragmentListener) {
            graphListener = (GraphFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        graphListener = null;
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
    public interface GraphFragmentListener {
        // TODO: Update argument type and name
    }

    public boolean addDataPoint(double y){
        dataPoints = defaultDataPoints;
        dataPoints[4] = new DataPoint(4, y);
        barPoints.resetData(dataPoints);

        return true;
    }

    public void setBounds(){
        Viewport viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setYAxisBoundsManual(true);

        viewport.setMinX(0);
        viewport.setMaxX(4);

        viewport.setMinY(0);
        viewport.setMaxY(5);
    }

    private void reAssignData(){
        for (int i = 0; i < 5; i ++){
            this.dataPoints[i] = this.defaultDataPoints[i];
        }
    }
}
