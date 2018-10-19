package com.trieffects.ConnecttwoSchool.Activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Adapter.ExamLIstAdapter1;
import com.trieffects.ConnecttwoSchool.Adapter.ExamTimeTableAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Model.ExamData;
import com.trieffects.ConnecttwoSchool.Model.ExamModel;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamTimeTable extends AppCompatActivity {

    RecyclerView subject_recycle;
     ExamTimeTableAdapter adapter;
    AlertDialog dialog;
    ActionBar actionBar;

    List<ChildData> list;
    Spinner spinner_std,spinner_exam;
    LinearLayout select_student_layout,select_anual_layout;
    TextView std_name,exam_name;
    ArrayList<ExamData> result_array=new ArrayList<>();
    String classId,sectionId;
    float size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_time_table);
        std_name=findViewById(R.id.std_name);
        exam_name=findViewById(R.id.exam_name);
        dialog = new SpotsDialog(this,"Please wait",R.style.Custom);
        spinner_std=(Spinner)findViewById(R.id.spinner_std);
        spinner_exam=(Spinner)findViewById(R.id.spinner_exam);

        select_student_layout=(LinearLayout)findViewById(R.id.select_student_layout);
        select_student_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner_std.performClick();
            }
        });
        select_anual_layout=(LinearLayout)findViewById(R.id.select_anual_layout);
        select_anual_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner_exam.performClick();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Exam. Schedule");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Drawable drawable=toolbar.getNavigationIcon();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        subject_recycle=(RecyclerView)findViewById(R.id.recycler_subject);

        final StaggeredGridLayoutManager layoutManagerdate = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        subject_recycle.setLayoutManager(layoutManagerdate);


         size= (float) 1;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        if(height<=900&&width<=500){
            size= (float) 0.5;
        }
        dialog.show();
        if(PrefrencesUtils.getUserMode().equalsIgnoreCase("3")) {
            childData();
            spinner_std.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    std_name.setText(adapterView.getSelectedItem().toString());
                       dialog.show();
                    classId=list.get(i).class_id;
                    sectionId= list.get(i).section_id;
                       getCurrentNotice(list.get(i).class_id, list.get(i).section_id);


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            select_student_layout.setVisibility(View.GONE);
            dialog.show();
            classId=PrefrencesUtils.getClassId();
            sectionId= PrefrencesUtils.getSectionId();
            getCurrentNotice(PrefrencesUtils.getClassId(),PrefrencesUtils.getSectionId());
        }

        spinner_exam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if(result_array.size()>0) {
                   exam_name.setText(adapterView.getSelectedItem().toString());
                   dialog.show();
                   getTimeTable(classId, sectionId, result_array.get(i).exam_id, size);
               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void getCurrentNotice(String id,String sectionid){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<ExamModel> mService=mApiService.getExamList(id,sectionid);
        mService.enqueue(new Callback<ExamModel>(){
            @Override
            public void onResponse(Call<ExamModel> call, Response<ExamModel> response) {
                ExamModel object=response.body();
                if(object.status==true){
                    result_array= object.data.result_array;
                    ArrayAdapter<ExamData> dataAdapter = new ArrayAdapter<ExamData>(ExamTimeTable.this, android.R.layout.simple_spinner_item, object.data.result_array);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_exam.setAdapter(dataAdapter);
                }else {
                    dialog.dismiss();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ExamModel> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public void childData(){
        if(PrefrencesUtils.getChildInfo()!=null) {
            list = PrefrencesUtils.getChildInfo();
            ArrayAdapter<ChildData> dataAdapter = new ArrayAdapter<ChildData>(ExamTimeTable.this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_std.setAdapter(dataAdapter);
            if (list.size()==1) {
                classId=list.get(0).class_id;
                sectionId= list.get(0).section_id;
                getCurrentNotice(list.get(0).class_id,list.get(0).section_id);
                select_student_layout.setVisibility(View.GONE);
            }
        }
    }

    public void getTimeTable(String class_id, String section_id,String exam_id,final float size){
       final ApiInterface mApiService=ApiUtils.getInterfaceService();
       Call<ExamModel> mServices=mApiService.getExamTimeTable(class_id,section_id,exam_id);
       mServices.enqueue(new Callback<ExamModel>() {
           @Override
           public void onResponse(Call<ExamModel> call, Response<ExamModel> response) {
               ExamModel object=response.body();
               if(object.status==true){
                   adapter=new ExamTimeTableAdapter(object.data.result_array,size);
                   subject_recycle.setAdapter(adapter);
                   adapter.notifyDataSetChanged();
               }else {
                   Toast.makeText(ExamTimeTable.this,"No Data available",Toast.LENGTH_SHORT).show();
               }
               dialog.dismiss();
           }

           @Override
           public void onFailure(Call<ExamModel> call, Throwable t) {
               dialog.dismiss();
               Toast.makeText(ExamTimeTable.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
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
            ExamTimeTable.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ExamTimeTable.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
