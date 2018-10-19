package com.trieffects.ConnecttwoSchool.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import java.util.Calendar;

/**
 * Created by Trieffects on 26-Dec-17.
 */

public class DriverServices extends Service {

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    int hr,Seconds, Minutes, MilliSeconds ;

    static int timelast=0;
    Handler handler;


    Calendar c = Calendar.getInstance();
    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler() ;
        context=this;
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        return  super.onStartCommand(intent,flags,startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    public Runnable runnable = new Runnable() {

        public void run() {
        //  if(1==1){  //use when stop service
             //   return;
          //  }
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;
            hr=Minutes/60;
            Seconds = Seconds % 60;
            if(Seconds%30==0){
                timelast=0;
            }

            if(timelast==0){
                timelast=1;
                Intent intent=new Intent(context,AutoTracker2.class);
                startService(intent);
            }
            MilliSeconds = (int) (UpdateTime % 1000);
            handler.postDelayed(this, 0);
        }

    };
}
