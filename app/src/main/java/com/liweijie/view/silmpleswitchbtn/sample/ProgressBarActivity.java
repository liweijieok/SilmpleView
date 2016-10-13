package com.liweijie.view.silmpleswitchbtn.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.liweijie.view.progress.DynamicProgressBar;

public class ProgressBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        DynamicProgressBar progressBar = (DynamicProgressBar) findViewById(R.id.progressbar);
        progressBar.setProgress(80);
    }



}
