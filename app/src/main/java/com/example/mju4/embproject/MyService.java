package com.example.mju4.embproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        Context mContext = getApplicationContext();
        Toast.makeText(mContext, "servie on", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Context mContext = getApplicationContext();
        Toast.makeText(mContext, "servie off", Toast.LENGTH_SHORT).show();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
