package com.liweijie.view.silmpleswitchbtn.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OpenProgress(View view) {
        Intent progress = new Intent(this, ProgressBarActivity.class);
        startActivity(progress);
    }

    public void openSwitch(View view) {
        Intent progress = new Intent(this, TransitionSwitchActivity.class);
        startActivity(progress);
    }

    public void openRate(View view) {
        Intent progress = new Intent(this, CustomRatingActivity.class);
        startActivity(progress);
    }
}
