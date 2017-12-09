package com.example.mju4.embproject;

import android.*;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhn.android.maps.NMapController;
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

    private String CHAT_NAME;
    private String USER_NAME;

    private LocationManager lm;
    private LocationListener ll;
    private Context mContext;


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
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_location);

        mContext = getApplicationContext();
        String[] permissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int result = PermissionChecker.checkSelfPermission(this, permission);
                if (result == PermissionChecker.PERMISSION_GRANTED);
                else ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
        if (Build.VERSION.SDK_INT>=23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            return;
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new Enroll_locationActivit.MyLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, ll);


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

        Intent intent = getIntent();
        CHAT_NAME = intent.getStringExtra("chatName");
        USER_NAME = intent.getStringExtra("userName");

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        mGeoPoint = new NGeoPoint();
        //mapLocationManager = new NMapLocationManager(this);
        //mapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
        //mGeoPoint = mapLocationManager.getMyLocation();//현재위치 저장

        dialog = new TimePickerDialog(this, listener, date.getHours(), date.getMinutes(), false);
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
            ChatDTO chat = new ChatDTO(USER_NAME, USER_NAME + "님이 새로운 박스를 등록했습니다 !!"); //선물등록하면서 메세지를 띄운다
            databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬
            EventDTO event = new EventDTO(editTextEventName.getText().toString(),editTextEventCo.getText().toString(),USER_NAME,date,mGeoPoint.getLatitude(),mGeoPoint.getLongitude());
            databaseReference.child("event").child(CHAT_NAME).push().setValue(event); // 데이터 푸쉬
            //Firebase의 등록하기

            Intent intent = new Intent(this, ChatRoomActivity.class);
            intent.putExtra("chatName", CHAT_NAME);
            intent.putExtra("userName", USER_NAME);
            startActivity(intent);
        }
    }


    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            date.setHours(hourOfDay);
            date.setMinutes(minute);
            editTextEventOpenTime.setText(hourOfDay + ":" + minute);
            Toast.makeText(getApplicationContext(),"시간이 등록되었습니다." , Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        String[] permissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int result = PermissionChecker.checkSelfPermission(this, permission);
                if (result == PermissionChecker.PERMISSION_GRANTED);
                else ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
        if (Build.VERSION.SDK_INT>=23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            return;
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.removeUpdates(ll);
    }
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location l) {
            mGeoPoint.set(l.getLongitude(),l.getLatitude());
            Toast.makeText(mContext, l.getLatitude()+","+l.getLongitude(), Toast.LENGTH_SHORT).show();
        }
        @Override public void onProviderDisabled(String p) {}
        @Override public void onProviderEnabled(String p) {}
        @Override public void onStatusChanged(String p, int s, Bundle e) {}
    }
}
