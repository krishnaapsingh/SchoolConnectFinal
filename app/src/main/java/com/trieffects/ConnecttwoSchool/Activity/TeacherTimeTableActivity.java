package com.trieffects.ConnecttwoSchool.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Adapter.TeacherTableAdapterAdapter1;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Model.TeacherTimeModel;
import com.trieffects.ConnecttwoSchool.Model.Timemodel;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherTimeTableActivity extends AppCompatActivity {

    RecyclerView time_recycle;
    String[] subjectName;
    AlertDialog dialog;
    List<Timemodel> timetablelist=new ArrayList<>();
    ArrayList<ChildData> data=new ArrayList<>();
    String std_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.teacher_timetable_fragment);


        dialog = new SpotsDialog(this,"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        time_recycle=(RecyclerView)findViewById(R.id.recyclerView2);

        float size= (float) 0.82;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        if(height<=900&&width<=500){
            size= (float) 0.32;
        }
        dialog.show();
        loadChild();
        getTimeTable("1",size);
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
        Call<TeacherTimeModel> mService=mApiService.getTeacherTime(id);
        mService.enqueue(new Callback<TeacherTimeModel>() {
            @Override
            public void onResponse(Call<TeacherTimeModel> call, Response<TeacherTimeModel> response) {
                TeacherTimeModel object = response.body();
                if (object.status) {
                    for(int i=0;i<object.data.Monday.size();i++){
                        Timemodel timemodel=new Timemodel();
                        timemodel.setMob(object.data.Monday.get(i).start_time+"- "+object.data.Monday.get(i).end_time+"\n"+object.data.Monday.get(i).room_no+"\n("+object.data.Monday.get(i).name+")");
                        timemodel.setTus(object.data.Tuesday.get(i).start_time+"- "+object.data.Tuesday.get(i).end_time+"\n"+object.data.Tuesday.get(i).room_no+"\n("+object.data.Tuesday.get(i).name+")");
                        timemodel.setWed(object.data.Wednesday.get(i).start_time+"- "+object.data.Wednesday.get(i).end_time+"\n"+object.data.Wednesday.get(i).room_no+"\n("+object.data.Wednesday.get(i).name+")");
                        timemodel.setThes(object.data.Thursday.get(i).start_time+"- "+object.data.Thursday.get(i).end_time+"\n"+object.data.Thursday.get(i).room_no+"\n("+object.data.Thursday.get(i).name+")");
                        timemodel.setFir(object.data.Friday.get(i).start_time+"- "+object.data.Friday.get(i).end_time+"\n"+object.data.Friday.get(i).room_no+"\n("+object.data.Friday.get(i).name+")");
                        timemodel.setSat(object.data.Saturday.get(i).start_time+"- "+object.data.Saturday.get(i).end_time+"\n"+object.data.Saturday.get(i).room_no+"\n("+object.data.Saturday.get(i).name+")");
                        timetablelist.add(timemodel);
                    }
                    final StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(timetablelist.size(), StaggeredGridLayoutManager.HORIZONTAL);
                    time_recycle.setLayoutManager(layoutManager1);
                    TeacherTableAdapterAdapter1 adapter1=new TeacherTableAdapterAdapter1(timetablelist,TeacherTimeTableActivity.this,6,txtsize);
                    time_recycle.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();

                }else {
                    Toast.makeText(TeacherTimeTableActivity.this,"No data available",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<TeacherTimeModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TeacherTimeTableActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TeacherTimeTableActivity.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


}
