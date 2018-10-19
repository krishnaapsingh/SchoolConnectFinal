package com.trieffects.ConnecttwoSchool.Other;

import android.app.IntentService;
import android.content.Intent;

import com.trieffects.ConnecttwoSchool.Notification.GPSTracker;

/**
 * Created by Trieffects on 30-Nov-17.
 */

public class DriverServiceLoction  extends IntentService {

    String mobile;

    public DriverServiceLoction(){
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GPSTracker gpsTracker=new GPSTracker(this);


    }
}
