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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.trieffects.ConnecttwoSchool.Adapter.TeacherAttendanceAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.AttendanceUpload;
import com.trieffects.ConnecttwoSchool.Model.StudentListModel;
import com.trieffects.ConnecttwoSchool.Model.TeacherAttendanceModel;
import com.trieffects.ConnecttwoSchool.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ShimmerRecyclerView shimmerRecycler;
    AlertDialog dialog;
    TeacherAttendanceAdapter adapter;

    TextView attendancespinner_txt;
    Spinner attendance_spinner;
    ImageView spinner_image;

    TextView class_name,section_txt,date_txt,total_std;
    String classname,sectionname,date,item;
    String class_id,section_id;
  Button save_btn;
    List<StudentListModel> stdList=new ArrayList<>();
    public static List<StudentListModel> submitList=new ArrayList<>();
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);

        submitList.clear();

        class_name=(TextView)findViewById(R.id.class_name);
        section_txt=(TextView)findViewById(R.id.section_txt);
        date_txt=(TextView)findViewById(R.id.date_txt);
        total_std=(TextView)findViewById(R.id.total_std);
        save_btn=(Button)findViewById(R.id.save_btn);
        classname=getIntent().getStringExtra("classname");
        sectionname=getIntent().getStringExtra("sectionname");
        date=getIntent().getStringExtra("date");
        item=getIntent().getStringExtra("data");

         if(item.equalsIgnoreCase("1")){
             save_btn.setVisibility(View.GONE);
             actionBar = getSupportActionBar();
             actionBar.setDisplayHomeAsUpEnabled(true);
             actionBar.setHomeButtonEnabled(true);
             Drawable drawable=toolbar.getNavigationIcon();
             drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);
         }
        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");

        class_name.setText(classname);
        section_txt.setText(sectionname);
        date_txt.setText(date);



        shimmerRecycler = (ShimmerRecyclerView)findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        shimmerRecycler.setLayoutManager(mLayoutManager);
        shimmerRecycler.setItemAnimator(new DefaultItemAnimator());
        shimmerRecycler.showShimmerAdapter();
        shimmerRecycler.showShimmerAdapter();
        dialog = new SpotsDialog(this,"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        attendancespinner_txt=(TextView)findViewById(R.id.attendancespinner_txt);
        attendance_spinner=(Spinner)findViewById(R.id.attendance_spinner);
        ArrayAdapter<CharSequence> grade;
        if(item.equalsIgnoreCase("1")) {
            grade=ArrayAdapter.createFromResource(this, R.array.attendance_type1, android.R.layout.simple_spinner_item);
        }else {
            grade=ArrayAdapter.createFromResource(this, R.array.attendance_type, android.R.layout.simple_spinner_item);
        }
        grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attendance_spinner.setAdapter(grade);

        spinner_image=(ImageView)findViewById(R.id.spinner_image);
        spinner_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    attendance_spinner.performClick();

            }
        });


            attendance_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i>0) {
                        String interesting = attendance_spinner.getItemAtPosition(i).toString();
                        attendancespinner_txt.setText(interesting);
                        adapter=new TeacherAttendanceAdapter(AttendanceActivity.this,i,stdList,item);
                        shimmerRecycler.setAdapter(adapter);
                        shimmerRecycler.hideShimmerAdapter();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        loadAttendance(class_id,section_id,date,-1);




        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String data=AttendanceJson();
                UploadAttendance(class_id,section_id,date,data);
            }
        });

    }





    public void loadAttendance(String class_id,String section_id,String date,final int pos){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<TeacherAttendanceModel> mService=mApiService.getStudentList(class_id,section_id,date);
        mService.enqueue(new Callback<TeacherAttendanceModel>() {
            @Override
            public void onResponse(Call<TeacherAttendanceModel> call, Response<TeacherAttendanceModel> response) {
                TeacherAttendanceModel object=response.body();
                if(object.status){
                    stdList.addAll(object.data.resultlist);
                    total_std.setText(String.valueOf(object.data.resultlist.size()));
                    submitList.addAll(object.data.resultlist);
                    adapter=new TeacherAttendanceAdapter(AttendanceActivity.this,pos,stdList,item);
                    shimmerRecycler.setAdapter(adapter);
                    shimmerRecycler.hideShimmerAdapter();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<TeacherAttendanceModel> call, Throwable t) {
                Toast.makeText(AttendanceActivity.this,"Slow interent",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                shimmerRecycler.hideShimmerAdapter();
            }
        });
    }

    public void UploadAttendance(String class_id,String section_id,String date,String jsondata){

        final ApiInterface mApiService=ApiUtils.getInterfaceService();
        Call<AttendanceUpload> mService=mApiService.uploadAttendance(class_id,section_id,date,jsondata);
        mService.enqueue(new Callback<AttendanceUpload>() {
            @Override
            public void onResponse(Call<AttendanceUpload> call, Response<AttendanceUpload> response) {
                AttendanceUpload object=response.body();
                if(object.status){
                    finish();
                }else {
                    Toast.makeText(AttendanceActivity.this,"Try again",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<AttendanceUpload> call, Throwable t) {
              dialog.dismiss();
                Toast.makeText(AttendanceActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
        //
//        RequestParams params = new RequestParams();
//        params.put("class_id", class_id);
//        params.put("section_id", section_id);
//        params.put("date", date);
//        params.put("json_data", jsondata);



//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get("http://demo.connect2school.com/api/example/stuattendence_update", params,new JsonHttpResponseHandler() {
//
//            @Override
//            public void onStart() {
//                // called before request is started
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String response) {
//                try {
//                    JSONObject object=new JSONObject(response);
//                    if(object.getBoolean("status")){
//                        finish();
//                    }else {
//                        Toast.makeText(AttendanceActivity.this,"Try again",Toast.LENGTH_SHORT).show();
//                    }
//                    dialog.dismiss();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
//                dialog.dismiss();
//                Toast.makeText(AttendanceActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onRetry(int retryNo) {
//                dialog.dismiss();
//                Toast.makeText(AttendanceActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
//            }
//        });

    }




    public String AttendanceJson(){
        JSONArray array=new JSONArray();

            try {
                for(int i=0;i<submitList.size();i++){
                    JSONObject object=new JSONObject();
                   object.put("attendence_id",submitList.get(i).attendence_id);
                    object.put("student_session_id",submitList.get(i).student_session_id);
                    object.put("attendence_type_id",submitList.get(i).attendence_type_id);
                    array.put(object);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return array.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Enter name, father's name");
        searchView.setOnQueryTextListener(this);
        return true;
    }


    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return true;
    }

    public boolean      onQueryTextSubmit      (String query) {
        //Toast.makeText(this, "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
        //  mSearchText.setText("Searching for: " + query + "...");
        //mSearchText.setTextColor(Color.RED);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            AttendanceActivity.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
