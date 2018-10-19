package com.trieffects.ConnecttwoSchool.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.Login;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.Other.Utility;
import com.trieffects.ConnecttwoSchool.R;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolCodeActivity extends AppCompatActivity {

    EditText input_password,input_email;
    Button loginbtn;
    AlertDialog dialog;
    public static String device_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_code);
        Utility.checkPermission(this);
        input_password=(EditText)findViewById(R.id.input_password);
        input_email=(EditText)findViewById(R.id.input_email);
        loginbtn=(Button)findViewById(R.id.loginbtn);

        dialog = new SpotsDialog(this,"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation()==1){
                    dialog.show();
                    loginUser(input_email.getText().toString().trim(),input_password.getText().toString().trim(),device_id);
                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    public int validation(){
        if(input_email.getText().toString().trim().equalsIgnoreCase("")&&input_password.getText().toString().trim().equalsIgnoreCase("")){
            Toast.makeText(SchoolCodeActivity.this,"All fields are mandatory",Toast.LENGTH_SHORT).show();
        }else if(input_email.getText().toString().trim().equalsIgnoreCase("")){
            input_email.setError("Please enter user name");
        }else if(input_password.getText().toString().trim().equalsIgnoreCase("")){
            input_password.setError("Please enter password");
        }else {
            return 1;
        }
        return -1;
    }

    private void loginUser(final String username, String password,String device_id) {
        final ApiInterface mApiInterface= ApiUtils.getInterfaceService();
        Call<Login> mService=mApiInterface.authenticate(username,password,device_id);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login object=response.body();
                if(object.status==true){
                    PrefrencesUtils.saveLoginUserName(username);
                    dataStore(object);
                    Intent intent;
                    if(PrefrencesUtils.getUserMode().equalsIgnoreCase("4")){
                        intent = new Intent(SchoolCodeActivity.this, DriverHomeScreen.class);
                    }else {
                        intent = new Intent(SchoolCodeActivity.this, HomeActivity.class);
                    }

                   startActivity(intent);
                    finish();
                    dialog.dismiss();
                }else {
                    Toast.makeText(SchoolCodeActivity.this,"Please check your username and password",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(SchoolCodeActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void dataStore( Login object){
        if(!ApiUtils.isEmptyString(object.data.role)){
            if(object.data.role.equalsIgnoreCase("student")){
                PrefrencesUtils.saveLoginStudentData(object.data);
                PrefrencesUtils.saveUserId(object.data.student_id);
                PrefrencesUtils.saveId(object.data.id);
                PrefrencesUtils.saveUserName(object.data.username);
                PrefrencesUtils.saveMobile(object.data.mobileno);
                PrefrencesUtils.saveEmail(object.data.email);
                PrefrencesUtils.saveRole(object.data.role);
                PrefrencesUtils.saveSectionId(object.data.section_id);
                PrefrencesUtils.saveClassId(object.data.class_id);
                PrefrencesUtils.saveImage(object.data.image);
                PrefrencesUtils.saveCurrencySymbol(object.data.currency_symbol);
                PrefrencesUtils.saveUserMode("1");
            }else if(object.data.role.equalsIgnoreCase("driver")){
                PrefrencesUtils.saveUserId(object.data.driver_id);
                PrefrencesUtils.saveId(object.data.id);
                PrefrencesUtils.saveUserName(object.data.d_name);
                PrefrencesUtils.saveImage(object.data.profile_pic);
                PrefrencesUtils.saveRole(object.data.role);
                PrefrencesUtils.saveMobile(object.data.d_phone);
                PrefrencesUtils.saveDriverLicnce(object.data.d_license_no);
                PrefrencesUtils.saveSchoolName(object.data.sch_name);
                PrefrencesUtils.saveUserMode("4");

            }else {
                PrefrencesUtils.saveUserId(object.data.teacher_id);
                PrefrencesUtils.saveId(object.data.id);
                PrefrencesUtils.saveUserName(object.data.username);
                PrefrencesUtils.saveRole(object.data.role);
                PrefrencesUtils.saveSchoolName(object.data.sch_name);
                PrefrencesUtils.saveCurrencySymbol(object.data.currency_symbol);
                PrefrencesUtils.saveUserMode("2");;
                PrefrencesUtils.saveImage(object.data.image);
                PrefrencesUtils.saveEmail(object.data.email);
                PrefrencesUtils.saveMobile(object.data.phone);
            }
        }else {
            PrefrencesUtils.saveUserId(object.data.parent_info.get(0).user_id);
            PrefrencesUtils.saveId(object.data.parent_info.get(0).id);
            PrefrencesUtils.saveUserName(object.data.parent_info.get(0).father_name);
            PrefrencesUtils.saveRole(object.data.parent_info.get(0).role);
            PrefrencesUtils.saveCurrencySymbol(object.data.parent_info.get(0).currency_symbol);
            PrefrencesUtils.saveChildInfo(object.data.child_info);
            PrefrencesUtils.saveImage(object.data.parent_info.get(0).image);
            PrefrencesUtils.saveEmail(object.data.parent_info.get(0).email);
            PrefrencesUtils.saveMobile(object.data.parent_info.get(0).mobileno);
            PrefrencesUtils.saveUserMode("3");
        }
    }
}
