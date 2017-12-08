package com.example.mju4.embproject;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Enroll_locationActivit extends AppCompatActivity implements View.OnClickListener {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "bBF0eHJZ5nmfeYRVz47s";// 애플리케이션 클라이언트 아이디 값
    NGeoPoint mGeoPoint;//위치
    NMapLocationManager mapLocationManager;//위치 관리 매니저
    NMapPlacemark mapPlacemark;//동읍주소로


    long now = System.currentTimeMillis();
    Date date = new Date(now);//시간
    TimePickerDialog dialog;//시간정하기 쓸때 쓸 위젯다이얼로그
    SimpleDateFormat mFormat;//시간 문자 포맷


    private EditText editTextEventName;
    private EditText editTextEventCo;
    private EditText editTextEventOpenTime;
    private EditText editTextEventCloseTime;
    private EditText editTextEventLocation;
    private Button buttonFindLocation;
    private Button buttonEnroll;
    private Button buttonSelectTime;


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
        editTextEventLocation = (EditText) findViewById(R.id.editTextEventLocation);
        //Text
        buttonFindLocation = (Button) findViewById(R.id.buttonFindLocation);
        buttonEnroll = (Button) findViewById(R.id.buttonEnroll);
        buttonSelectTime = (Button) findViewById(R.id.buttonSelectTime);
        //button
        mFormat = new SimpleDateFormat("hh:mm");

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();


        mGeoPoint = new NGeoPoint();
        //mGeoPoint = mapLocationManager.getMyLocation();//현재위치 저장

        dialog = new TimePickerDialog(this, listener, 15, 24, false);
        //dialog 만들기

        editTextEventOpenTime.setText(mFormat.format(date));
        editTextEventLocation.setText(firebaseAuth.getCurrentUser().getEmail());
        //Text 초기화

        buttonFindLocation.setOnClickListener(this);
        buttonEnroll.setOnClickListener(this);
        buttonSelectTime.setOnClickListener(this);
        //ClickListener
    }

    @Override
    public void onClick(View view) {
        if (view == buttonFindLocation) {
            progressDialog.setMessage("지도 불러오는 중. 잠시 기다려 주세요...");
            progressDialog.show();
            //지도를 띄워주어서 거기를 클릭하는 형식??

        }
        if(view == buttonSelectTime){
            dialog.show();
            //Timepicker을 띄워서 선택
        }
        if(view == buttonEnroll){
            progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요...");
            progressDialog.show();
            //Firebase의 등록하기
        }
    }

    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            editTextEventOpenTime.setText(hourOfDay + ":" + minute);
            Toast.makeText(getApplicationContext(),"시간이 등록되었습니다." , Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
