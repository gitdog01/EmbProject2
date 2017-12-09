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
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

public class NmapActivity extends NMapActivity  {
    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "bBF0eHJZ5nmfeYRVz47s";// 애플리케이션 클라이언트 아이디 값
    NGeoPoint mGeoPoint;//위치
    NMapLocationManager mapLocationManager;//위치 관리 매니저
    NMapController mMapController;

    //Map 위에 오버레이 객체
    NMapResourceProvider mMapViewResourceProvider;
    NMapCompassManager mMapCompassManager;
    NMapMyLocationOverlay mMyLocationOverlay;
    NMapOverlayManager mOverlayManager;

    private LocationManager lm;
    private LocationListener ll;
    private Context mContext;


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

        mGeoPoint = new NGeoPoint();



        mMapView = new NMapView(this);
        setContentView(mMapView);
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();

        //Toast.makeText(mContext, mGeoPoint.getLatitude()+","+mGeoPoint.getLongitude(), Toast.LENGTH_SHORT).show();

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


}