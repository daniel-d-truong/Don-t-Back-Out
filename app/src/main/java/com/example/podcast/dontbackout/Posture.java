package com.example.podcast.dontbackout;
import android.util.Log;

import java.util.ArrayList;

public class Posture {
    ArrayList<String> beetleInfo = new ArrayList<>();
    public Posture(){
    }
    public void add(String info){
        beetleInfo.add(info);
        Log.d("added to posture", info);
    }

}
