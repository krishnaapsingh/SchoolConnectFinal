package com.trieffects.ConnecttwoSchool.Activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.trieffects.ConnecttwoSchool.Adapter.ResultAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ResultModel;
import com.trieffects.ConnecttwoSchool.Other.CountTotalMark;
import com.trieffects.ConnecttwoSchool.R;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentResultScreen extends AppCompatActivity {

    RecyclerView recycler_view_result;
    AlertDialog dialog;

    TextView std_name,std_roll,std_dob,std_class,total_txt,div_txt;
    ActionBar actionBar;
    CircularImageView user_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Result");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);
        user_image = (CircularImageView) findViewById(R.id.user_image);

        dialog = new SpotsDialog(this, "Please wait", R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        std_name = (TextView) findViewById(R.id.std_name);
        std_roll = (TextView) findViewById(R.id.std_roll);
        std_dob = (TextView) findViewById(R.id.std_dob);
        std_class = (TextView) findViewById(R.id.std_class);
        total_txt= (TextView) findViewById(R.id.total_txt1);

        div_txt= (TextView) findViewById(R.id.div_txt);

        recycler_view_result = (RecyclerView) findViewById(R.id.recycler_view_result);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_result.setLayoutManager(mLayoutManager);
        recycler_view_result.setItemAnimator(new DefaultItemAnimator());
        // recyclerView.setAdapter(mAdapter);
        dialog.show();
        if(!ApiUtils.isEmptyString(getIntent().getStringExtra("image"))){
        Glide.with(this).load(ApiUtils.ImageBaseUrl + getIntent().getStringExtra("image")).error(R.drawable.mypic).into(user_image);
         }
        getResult(getIntent().getStringExtra("exam_id"),getIntent().getStringExtra("classid"),getIntent().getStringExtra("sectionid"),getIntent().getStringExtra("std_id"));
    }

    public void getResult(String exam_id,String class_id,String section_id,String std_sess_id){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<ResultModel> mservice=mApiService.getRes(exam_id,class_id,section_id,std_sess_id);
        mservice.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                ResultModel object=response.body();
                if(object.status){
                    if(object.data.examSchedule.result.size()>0) {
                        std_name.setText(object.data.examSchedule.result.get(0).firstname + " " + object.data.examSchedule.result.get(0).lastname);
                        std_roll.setText(object.data.examSchedule.result.get(0).roll_no);
                        std_dob.setText(object.data.examSchedule.result.get(0).dob);
                        String result = CountTotalMark.count(object.data.examSchedule.result.get(0).exam_array);
                        total_txt.setText(result);
                        if (!result.equalsIgnoreCase("-")) {
                            String[] devision = result.split("/");
                            int dev = Integer.parseInt(devision[0]) / object.data.examSchedule.result.get(0).exam_array.size();
                            if (dev >= 60) {
                                div_txt.setText("First Division");
                            } else if (dev >= 45 && dev < 60) {
                                div_txt.setText("Second Division");
                            } else if (dev > 34 && dev < 45) {
                                div_txt.setText("Third Division");
                            } else {
                                div_txt.setText("Fail");
                            }
                        } else {
                            div_txt.setText("_");
                        }

                        ResultAdapter adapter = new ResultAdapter(StudentResultScreen.this, object.data.examSchedule.result.get(0).exam_array);
                        recycler_view_result.setAdapter(adapter);
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(StudentResultScreen.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            StudentResultScreen.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
