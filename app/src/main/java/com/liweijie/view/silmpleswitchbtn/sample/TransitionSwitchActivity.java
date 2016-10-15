package com.liweijie.view.silmpleswitchbtn.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.liweijie.view.progress.DynamicProgressBar;
import com.liweijie.view.silmpleswitchbtn.TransitionSwitchView;

public class TransitionSwitchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        TransitionSwitchView view = (TransitionSwitchView) findViewById(R.id.transition);
        view.setChangeListener(new TransitionSwitchView.OnCheckedListener() {
            @Override
            public void onCheckChange(TransitionSwitchView view, boolean isChecked) {
                Toast.makeText(getApplication(), String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
