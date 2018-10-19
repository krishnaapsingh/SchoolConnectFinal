package com.trieffects.ConnecttwoSchool.Services;

import android.app.IntentService;
import android.content.Intent;


import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.DriverServiceModel;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoTracker2 extends IntentService {
      public AutoTracker2(){
        super("");
      }

    @Override
    protected void onHandleIntent(Intent intent) {

       String lat=String.valueOf(PrefrencesUtils.getLattu());
       String log=String.valueOf(PrefrencesUtils.getLogude());
      final ApiInterface mApiservice= ApiUtils.getInterfaceService();
      Call<DriverServiceModel> mService=mApiservice.sendLatLog(lat,log);
      mService.enqueue(new Callback<DriverServiceModel>() {
          @Override
          public void onResponse(Call<DriverServiceModel> call, Response<DriverServiceModel> response) {

          }

          @Override
          public void onFailure(Call<DriverServiceModel> call, Throwable t) {

          }
      });
    }
}
