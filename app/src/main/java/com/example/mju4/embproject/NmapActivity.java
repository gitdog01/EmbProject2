package com.example.mju4.embproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;

public class NmapActivity extends NMapActivity  {
    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "bBF0eHJZ5nmfeYRVz47s";// 애플리케이션 클라이언트 아이디 값
    NGeoPoint mGeoPoint;//위치
    NMapLocationManager mapLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);

        mMapView = new NMapView(this);
        setContentView(mMapView);
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();

        mGeoPoint = new NGeoPoint();
        mGeoPoint = mapLocationManager.getMyLocation();//현재위치 저장

    }
}