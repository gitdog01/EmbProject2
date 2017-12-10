package com.example.mju4.embproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch switchServie;
    private Button buttonSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //알람 끄기 켜기
        //언어 선택
        //배경 색깔
        switchServie = (Switch)findViewById(R.id.switchService);
        buttonSetting = (Button)findViewById(R.id.buttonSetting);

        switchServie.setChecked(true);

        switchServie.setOnClickListener(this);
        buttonSetting.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view == switchServie) {
            if(switchServie.isChecked()){
                startService(new Intent(this,MyService.class));
            }else {
                stopService(new Intent(this,MyService.class));
            }
        }
        if(view == buttonSetting) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
