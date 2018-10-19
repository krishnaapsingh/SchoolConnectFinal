package com.trieffects.ConnecttwoSchool.TecherFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Activity.HomeActivity;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.Login;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherProfileFragment extends Fragment {

    EditText name_txt,email_txt,mobile_txt,cabin_txt;

    RecyclerView profile_recycler;
    TextView clg_name;
    AlertDialog dialog;
    ImageView refresh_data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.teacher_profile_fragment,container,false);
        name_txt=(EditText)view.findViewById(R.id.name_txt);
        email_txt=(EditText)view.findViewById(R.id.email_txt);
        clg_name=view.findViewById(R.id.clg_name);
        refresh_data=view.findViewById(R.id.refresh_data);
        mobile_txt=(EditText)view.findViewById(R.id.mobile_txt);
        cabin_txt=(EditText)view.findViewById(R.id.cabin_txt);
        dialog = new SpotsDialog(getActivity(),"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        name_txt.setText(PrefrencesUtils.getUserName());
        if(!ApiUtils.isEmptyString(PrefrencesUtils.getEmail())){
            email_txt.setText(PrefrencesUtils.getEmail());
        }

        if(!ApiUtils.isEmptyString(PrefrencesUtils.getEmail())){
            mobile_txt.setText(PrefrencesUtils.getMobile());
        }

        if(!ApiUtils.isEmptyString(PrefrencesUtils.getSchoolName())){
           clg_name.setText(PrefrencesUtils.getSchoolName());
        }
        refresh_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PrefrencesUtils.getUserMode().equalsIgnoreCase("1")){
                    onrefreshData("student",PrefrencesUtils.getId());
                }else if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2")){
                    onrefreshData("teacher",PrefrencesUtils.getUserId());
                }else if(PrefrencesUtils.getUserMode().equalsIgnoreCase("3")){
                    onrefreshData("parent",PrefrencesUtils.getUserId());
                }
            }
        });

        return view;
    }

    public void onrefreshData(String type,String id){
        dialog.show();
        final ApiInterface mApiServices=ApiUtils.getInterfaceService();
        Call<Login> mService=mApiServices.syncData(type,id);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login object=response.body();
                if(object.status==true) {
                    dataStore(object);
                }
                Toast.makeText(getActivity(),"Data Update successfully",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(getActivity(),"Please check your username and password",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
            }else if(object.data.role.equalsIgnoreCase("driver")){
                PrefrencesUtils.saveUserId(object.data.driver_id);
                PrefrencesUtils.saveId(object.data.id);
                PrefrencesUtils.saveUserName(object.data.d_name);
                PrefrencesUtils.saveImage(object.data.profile_pic);
                PrefrencesUtils.saveRole(object.data.role);
                PrefrencesUtils.saveMobile(object.data.d_phone);
                PrefrencesUtils.saveDriverLicnce(object.data.d_license_no);
                PrefrencesUtils.saveSchoolName(object.data.sch_name);

            }else {
                PrefrencesUtils.saveUserId(object.data.teacher_id);
                PrefrencesUtils.saveId(object.data.id);
                PrefrencesUtils.saveUserName(object.data.username);
                PrefrencesUtils.saveRole(object.data.role);
                PrefrencesUtils.saveSchoolName(object.data.sch_name);
                PrefrencesUtils.saveCurrencySymbol(object.data.currency_symbol);
                PrefrencesUtils.saveImage(object.data.parent_info.get(0).image);
                PrefrencesUtils.saveEmail(object.data.parent_info.get(0).email);
                PrefrencesUtils.saveMobile(object.data.parent_info.get(0).mobileno);

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

        }
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
