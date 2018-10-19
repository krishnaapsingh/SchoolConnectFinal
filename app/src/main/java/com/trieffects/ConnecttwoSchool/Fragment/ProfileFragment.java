package com.trieffects.ConnecttwoSchool.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.trieffects.ConnecttwoSchool.Activity.HomeActivity;
import com.trieffects.ConnecttwoSchool.Activity.SchoolCodeActivity;
import com.trieffects.ConnecttwoSchool.Activity.SplashActivity;
import com.trieffects.ConnecttwoSchool.Adapter.ProfileAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Model.Login;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    EditText name_txt,email_txt,mobile_txt;
    ProfileAdapter adapter;
    private ShimmerRecyclerView shimmerRecycler;
    CircularImageView user_image;

    private PopupWindowHelper popupWindowHelper;
    private View popView;
    ImageView back_img;

    EditText stdname_txt,father_txt,mothers_txt,stdemail_txt,class_txt,section_txt,roll_txt;
    CircularImageView userstd_image;
    ArrayList<ChildData> list;
    ImageView refresh_data;
    AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profile_fragment,container,false);
        shimmerRecycler = (ShimmerRecyclerView)view.findViewById(R.id.recycler_view);
        user_image=(CircularImageView)view.findViewById(R.id.user_image);
        name_txt=(EditText) view.findViewById(R.id.name_txt);
        email_txt=(EditText)view.findViewById(R.id.email_txt);
        mobile_txt=(EditText)view.findViewById(R.id.mobile_txt);
        refresh_data=view.findViewById(R.id.refresh_data);

        dialog = new SpotsDialog(getActivity(),"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        name_txt.setText(PrefrencesUtils.getUserName());
        if(!ApiUtils.isEmptyString(PrefrencesUtils.getEmail())){
            email_txt.setText(PrefrencesUtils.getEmail());
        }
        if(!ApiUtils.isEmptyString(PrefrencesUtils.getMobile())){
            mobile_txt.setText(PrefrencesUtils.getMobile());
        }

        if(!ApiUtils.isEmptyString(PrefrencesUtils.getImage())){
            Glide.with(this).load(ApiUtils.ImageBaseUrl+PrefrencesUtils.getImage()).error(R.drawable.mypic).into(user_image);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        shimmerRecycler.setLayoutManager(mLayoutManager);
        shimmerRecycler.setItemAnimator(new DefaultItemAnimator());

        if(PrefrencesUtils.getUserMode().equalsIgnoreCase("3")) {
            shimmerRecycler.showShimmerAdapter();
            if(PrefrencesUtils.getChildInfo()!=null){
                 list=PrefrencesUtils.getChildInfo();
                adapter = new ProfileAdapter(getActivity(),list,ProfileFragment.this);
                shimmerRecycler.showShimmerAdapter();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    shimmerRecycler.setAdapter(adapter);
                    shimmerRecycler.hideShimmerAdapter();
                }
            }, 3000);
        }else  {
           shimmerRecycler.hideShimmerAdapter();
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

    public void showHistor(int pos,View v){

        popView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_student_profile, null);
        popupWindowHelper = new PopupWindowHelper(popView);
        shimmerRecycler=(ShimmerRecyclerView)popView.findViewById(R.id.reclerview_std);
        back_img=(ImageView)popView.findViewById(R.id.back_img);

        stdname_txt=(EditText) popView.findViewById(R.id.stdname_txt);
        father_txt=(EditText)popView.findViewById(R.id.father_txt);
        mothers_txt=(EditText) popView.findViewById(R.id.mothers_txt);
        stdemail_txt=(EditText)popView.findViewById(R.id.stdemail_txt);
        class_txt=(EditText) popView.findViewById(R.id.class_txt);
        section_txt=(EditText)popView.findViewById(R.id.section_txt);
        roll_txt=(EditText) popView.findViewById(R.id.roll_txt);
        userstd_image=(CircularImageView)popView.findViewById(R.id.userstd_image);

        if(ApiUtils.isEmptyString(list.get(pos).lastname)){
            stdname_txt.setText(list.get(pos).firstname);
        }else {
            stdname_txt.setText(list.get(pos).firstname + " " + list.get(pos).lastname);
        }
        if(!ApiUtils.isEmptyString(list.get(pos).father_name)){
            father_txt.setText(list.get(pos).father_name);
        }
        if(!ApiUtils.isEmptyString(list.get(pos).mother_name)){
            mothers_txt.setText(list.get(pos).mother_name);
        }
        if(!ApiUtils.isEmptyString(list.get(pos).email)){
            stdemail_txt.setText(list.get(pos).email);
        }
        if(!ApiUtils.isEmptyString(list.get(pos).class_id)){
            class_txt.setText(list.get(pos).my_class);
        }
        if(!ApiUtils.isEmptyString(list.get(pos).section_id)){
            section_txt.setText(list.get(pos).section);
        }
        if(!ApiUtils.isEmptyString(list.get(pos).roll_no)){
            roll_txt.setText(list.get(pos).roll_no);
        }

        if(!ApiUtils.isEmptyString(list.get(pos).image)){
            Glide.with(getActivity()).load(ApiUtils.ImageBaseUrl+list.get(pos).image).error(R.drawable.mypic).into(userstd_image);
        }


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowHelper.dismiss();
            }
        });
        popupWindowHelper.showFromBottom(v);
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
