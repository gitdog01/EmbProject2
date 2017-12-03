package com.example.mju4.embproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;

import java.util.Date;

public class Enroll_locationActivit extends AppCompatActivity implements View.OnClickListener {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "bBF0eHJZ5nmfeYRVz47s";// 애플리케이션 클라이언트 아이디 값
    NGeoPoint mGeoPoint;//위치

    long now = System.currentTimeMillis();
    Date date = new Date(now);//시간


    private EditText editTextEventName;
    private EditText editTextEventCo;
    private EditText editTextEventOpenTime;
    private EditText editTextEventCloseTime;
    private EditText editTextEventLocation;
    private Button buttonFindLocation;
    private Button buttonEnroll;


    private ProgressDialog progressDialog;
    //define firebase object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_location);


        editTextEventName = (EditText) findViewById(R.id.editTextEventName);
        editTextEventCo = (EditText) findViewById(R.id.editTextEventCo);
        editTextEventOpenTime = (EditText) findViewById(R.id.editTextEventOpenTime);
        editTextEventCloseTime = (EditText) findViewById(R.id.editTextEventCloseTime);
        editTextEventLocation = (EditText) findViewById(R.id.editTextEventLocation);
        buttonFindLocation = (Button) findViewById(R.id.buttonFindLocation);
        buttonEnroll = (Button) findViewById(R.id.buttonEnroll);

        editTextEventOpenTime.setText(date.toString());
        editTextEventCloseTime.setText(date.toString());

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        buttonFindLocation.setOnClickListener(this);
        buttonEnroll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonFindLocation) {
            progressDialog.setMessage("지도 불러오는 중. 잠시 기다려 주세요...");
            progressDialog.show();
            //지도를 띄워주어서 거기를 클릭하는 형식??

        }
        if(view == buttonEnroll){
            progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요...");
            progressDialog.show();
            //Firebase의 등록하기
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
