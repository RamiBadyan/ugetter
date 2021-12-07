package com.xpero.ugetterexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LowCostVideo xGetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xGetter = new LowCostVideo(this);
        xGetter.onFinish(new LowCostVideo.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<XModel> vidURL, boolean multiple_quality) {
                Log.d("mainActivity", vidURL.get(0).getUrl());
            }

            @Override
            public void onError() {
                Log.d("mainActivity", "Error");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        xGetter.find("https://examole.com/video");
    }
}