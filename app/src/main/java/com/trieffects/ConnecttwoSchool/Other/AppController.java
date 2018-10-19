package com.trieffects.ConnecttwoSchool.Other;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.LogInterceptor;

/**
 * Created by Trieffects on 04-Sep-17.
 */

public class AppController extends Application {
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        timeHawkInit();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void timeHawkInit() {
        long startTime = System.currentTimeMillis();

        Hawk.init(this).setLogInterceptor(new LogInterceptor() {
            @Override
            public void onLog(String message) {
                Log.d("HAWK", message);
            }
        }).build();

        long endTime = System.currentTimeMillis();
        System.out.println("Hawk.init: " + (endTime - startTime) + "ms");
    }

}
