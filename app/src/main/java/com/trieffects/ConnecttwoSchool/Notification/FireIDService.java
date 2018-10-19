package com.trieffects.ConnecttwoSchool.Notification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.trieffects.ConnecttwoSchool.Activity.SchoolCodeActivity;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.HomeWorkModel;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        try {
            String tkn = FirebaseInstanceId.getInstance().getToken();
            if(!ApiUtils.isEmptyString(tkn)){
                SchoolCodeActivity.device_id = tkn;
                updateDeviceToken(PrefrencesUtils.getLoginUserName(),tkn);
            }else {
                FirebaseInstanceId.getInstance().deleteInstanceId();
                FirebaseInstanceId.getInstance().getId();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void updateDeviceToken(String username,String token){
        final ApiInterface mApiService=ApiUtils.getInterfaceService();
       Call<HomeWorkModel> mService=mApiService.updatedevicetoken(username,token);
       mService.enqueue(new Callback<HomeWorkModel>() {
           @Override
           public void onResponse(Call<HomeWorkModel> call, Response<HomeWorkModel> response) {
               HomeWorkModel object=response.body();
           }

           @Override
           public void onFailure(Call<HomeWorkModel> call, Throwable t) {

           }
       });
    }
}