package com.trieffects.ConnecttwoSchool.Activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Adapter.TimeTableAdapter;
import com.trieffects.ConnecttwoSchool.Adapter.TimeTableAdapterAdapter1;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Model.TimeTableModel;
import com.trieffects.ConnecttwoSchool.Model.Timemodel;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TimeTableActivity extends AppCompatActivity {
    ActionBar actionBar;
    List<Timemodel> timetablelist=new ArrayList<>();
    RecyclerView subject_recycle,time_recycle;
    String[] subjectName;
    AlertDialog dialog;
    private PopupWindowHelper popupWindowHelper;
    private View popView;

    ArrayList<ChildData> data=new ArrayList<>();
    String std_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("");
        toolbar.setTitle("");
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Drawable drawable=toolbar.getNavigationIcon();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);

        dialog = new SpotsDialog(this,"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        subject_recycle=(RecyclerView)findViewById(R.id.recyclerView1);
        time_recycle=(RecyclerView)findViewById(R.id.recyclerView2);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        subject_recycle.setLayoutManager(layoutManager);

        float size= (float) 0.82;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        if(height<=900&&width<=500){
            size= (float) 0.32;
        }
        dialog.show();
        if(PrefrencesUtils.getUserMode().equalsIgnoreCase("3")) {
            loadChild();
        }else {
            std_Id=PrefrencesUtils.getUserId();
        }

        getTimeTable(std_Id,size);
    }

    public void loadChild(){
        if(PrefrencesUtils.getChildInfo()!=null) {
            data = PrefrencesUtils.getChildInfo();
            if (data.size() > 1) {
                std_Id = data.get(0).student_id;

            } else {
                std_Id = data.get(0).student_id;

            }
        }
    }




    public void getTimeTable(String id, final float txtsize){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<TimeTableModel> mService=mApiService.getTime(id);
        mService.enqueue(new Callback<TimeTableModel>() {
            @Override
            public void onResponse(Call<TimeTableModel> call, Response<TimeTableModel> response) {
                TimeTableModel object=response.body();
                if(object.data.result_array.size()>0){
                    subjectName=new String[object.data.result_array.size()];
                    for(int i=0;i<object.data.result_array.size();i++){
                        subjectName[i]=object.data.result_array.get(i).Monday.subject;
                        Timemodel timemodel=new Timemodel();
                        timemodel.setMob(object.data.result_array.get(i).Monday.start_time+"- "+object.data.result_array.get(i).Monday.end_time+"\n"+object.data.result_array.get(i).Monday.room_no);
                        timemodel.setTus(object.data.result_array.get(i).Tuesday.start_time+"- "+object.data.result_array.get(i).Tuesday.end_time+"\n"+object.data.result_array.get(i).Tuesday.room_no);
                        timemodel.setWed(object.data.result_array.get(i).Wednesday.start_time+"- "+object.data.result_array.get(i).Wednesday.end_time+"\n"+object.data.result_array.get(i).Wednesday.room_no);
                        timemodel.setThes(object.data.result_array.get(i).Thursday.start_time+"- "+object.data.result_array.get(i).Thursday.end_time+"\n"+object.data.result_array.get(i).Thursday.room_no);
                        timemodel.setFir(object.data.result_array.get(i).Friday.start_time+"- "+object.data.result_array.get(i).Friday.end_time+"\n"+object.data.result_array.get(i).Friday.room_no);
                        timemodel.setSat(object.data.result_array.get(i).Saturday.start_time+"- "+object.data.result_array.get(i).Saturday.end_time+"\n"+object.data.result_array.get(i).Saturday.room_no);
                        timetablelist.add(timemodel);
                    }
                    TimeTableAdapter adapter=new TimeTableAdapter(TimeTableActivity.this,subjectName);
                    subject_recycle.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    final StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(object.data.result_array.size(), StaggeredGridLayoutManager.HORIZONTAL);
                    time_recycle.setLayoutManager(layoutManager1);
                    TimeTableAdapterAdapter1 adapter1=new TimeTableAdapterAdapter1(timetablelist,TimeTableActivity.this,object.data.result_array.size(),txtsize);
                    time_recycle.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                    dialog.dismiss();
                }else {
                    Toast.makeText(TimeTableActivity.this,"No data available",Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<TimeTableModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TimeTableActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            TimeTableActivity.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TimeTableActivity.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    // popView = LayoutInflater.from(this).inflate(R.layout.popupview, null);
    // popupWindowHelper = new PopupWindowHelper(popView);
    //popupWindowHelper.showFromTop(view);
    // popupWindowHelper.dismiss();
}
