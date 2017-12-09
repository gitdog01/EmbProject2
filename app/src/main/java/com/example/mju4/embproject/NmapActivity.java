package com.example.mju4.embproject;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import java.util.Date;

import static android.content.ContentValues.TAG;

public class NmapActivity extends NMapActivity {
    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "bBF0eHJZ5nmfeYRVz47s";// 애플리케이션 클라이언트 아이디 값
    NGeoPoint mGeoPoint;//위치
    NGeoPoint bufGeoPoint;//위치 버프
    NMapLocationManager mapLocationManager;//위치 관리 매니저
    NMapController mMapController;
    int markerId = NMapPOIflagType.PIN;
    long now = System.currentTimeMillis();
    Date date = new Date(now);//시간



    //Map 위에 오버레이 객체
    NMapResourceProvider mMapViewResourceProvider;
    NMapCompassManager mMapCompassManager;
    NMapMyLocationOverlay mMyLocationOverlay;
    NMapOverlayManager mOverlayManager;
    NMapPOIdata poiData;
    NMapPOIdataOverlay PoiOverlay;

    private LocationManager lm;
    private LocationListener ll;
    private Context mContext;
    private String CHAT_NAME;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);

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
        ll = new MyLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, ll);

        CHAT_NAME = intent.getStringExtra("chatName");


        mGeoPoint = new NGeoPoint();



        mMapView = new NMapView(this);
        setContentView(mMapView);
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
        mMapView.setBuiltInZoomControls(true, null);


        mMapController = mMapView.getMapController();
        mMapController.setMapCenter(mGeoPoint,11);


        // 오버래이 리소스 관리객체 할당
        mMapViewResourceProvider = new NMapViewerResourceProvider(this);


        // 오버래이 관리자 추가
        mOverlayManager = new NMapOverlayManager(this, mMapView,
                mMapViewResourceProvider);


        // 표시할 위치 데이터를 지정한다. -- 마지막 인자가 오버래이를 인식하기 위한 id값



        SetPin();




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
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
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


    private void SetPin(){



        databaseReference.child("event").child(CHAT_NAME).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                EventDTO eventDTO = dataSnapshot.getValue(EventDTO.class);  // eventDTO를 가져오고

                if((eventDTO.getEventTime().getHours()-date.getHours())>0){

                    poiData = new NMapPOIdata(1, mMapViewResourceProvider);
                    poiData.beginPOIdata(1);
                    Log.d(TAG, "Username=" + eventDTO.getUserName() + ", Eventname=" + eventDTO.getEventName() + "ChildAdd" + eventDTO.getLongitude() + "/" + eventDTO.getLatitude());
                    poiData.addPOIitem(eventDTO.getLongitude(), eventDTO.getLatitude(), eventDTO.getEventName(), markerId, 0);
                    poiData.endPOIdata();
                    // 위치 데이터를 사용하여 오버래이 생성
                    NMapPOIdataOverlay poiDataOverlay = mOverlayManager
                            .createPOIdataOverlay(poiData, null);
                    poiDataOverlay.showAllPOIdata(0);

                }else if(eventDTO.getEventTime().getHours()-date.getHours()==0){
                    if((eventDTO.getEventTime().getMinutes()-date.getMinutes())>0){
                        poiData = new NMapPOIdata(1, mMapViewResourceProvider);
                        poiData.beginPOIdata(1);
                        Log.d(TAG, "Username=" + eventDTO.getUserName() + ", Eventname=" + eventDTO.getEventName() + "ChildAdd" + eventDTO.getLongitude() + "/" + eventDTO.getLatitude());
                        poiData.addPOIitem(eventDTO.getLongitude(), eventDTO.getLatitude(), eventDTO.getEventName(), markerId, 0);
                        poiData.endPOIdata();
                        // 위치 데이터를 사용하여 오버래이 생성
                        NMapPOIdataOverlay poiDataOverlay = mOverlayManager
                                .createPOIdataOverlay(poiData, null);
                        poiDataOverlay.showAllPOIdata(0);
                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                EventDTO eventDTO = dataSnapshot.getValue(EventDTO.class);  // eventDTO를 가져오고
                Log.d(TAG, "Username=" + eventDTO.getUserName() + ", Eventname=" + eventDTO.getEventName() + "ChildChange");

                poiData.beginPOIdata(10);
                poiData.addPOIitem(eventDTO.getLongitude(), eventDTO.getLatitude(), eventDTO.getEventName(), markerId, 0);
                poiData.endPOIdata();
                // 위치 데이터를 사용하여 오버래이 생성
                NMapPOIdataOverlay poiDataOverlay = mOverlayManager
                        .createPOIdataOverlay(poiData, null);
                poiDataOverlay.showAllPOIdata(0);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}