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
import android.widget.Toast;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.trieffects.ConnecttwoSchool.Adapter.TeacherResultAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ResultModelFull;
import com.trieffects.ConnecttwoSchool.Model.TeacherResultModel;
import com.trieffects.ConnecttwoSchool.R;
import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.SearchView.OnQueryTextListener;

public class ClassResultActivity extends AppCompatActivity implements OnQueryTextListener {
    ActionBar actionBar;
    AlertDialog dialog;
    List<ResultModelFull> listdata=new ArrayList<>();
    private ShimmerRecyclerView shimmerRecycler;
    TeacherResultAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Exam. Result");
       // toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Drawable drawable=toolbar.getNavigationIcon();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);

        shimmerRecycler = (ShimmerRecyclerView)findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        shimmerRecycler.setLayoutManager(mLayoutManager);
        shimmerRecycler.setItemAnimator(new DefaultItemAnimator());
        shimmerRecycler.showShimmerAdapter();
        dialog = new SpotsDialog(this,"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        classResult_get(getIntent().getStringExtra("exam_id"),getIntent().getStringExtra("class_id"),getIntent().getStringExtra("section_id"));

    }




    public void classResult_get(String exam_id,String class_id,String section_id){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<TeacherResultModel> mService=mApiService.getClassResult(exam_id,class_id,section_id);
        mService.enqueue(new Callback<TeacherResultModel>() {
            @Override
            public void onResponse(Call<TeacherResultModel> call, Response<TeacherResultModel> response) {
                TeacherResultModel object=response.body();
                if(object.data.examSchedule.status.equalsIgnoreCase("yes")){

                    for(int i=0;i<object.data.examSchedule.result.size();i++){
                        ResultModelFull modelFull=new ResultModelFull();
                        modelFull.setName(object.data.examSchedule.result.get(i).firstname+" "+object.data.examSchedule.result.get(i).lastname);
                        modelFull.setRollno(object.data.examSchedule.result.get(i).roll_no);
                        modelFull.setDob(object.data.examSchedule.result.get(i).dob);
                        modelFull.setFatherName(object.data.examSchedule.result.get(i).father_name);
                        modelFull.setExam_array(object.data.examSchedule.result.get(i).exam_array);
                        modelFull.setImage(object.data.examSchedule.result.get(i).image);
                        listdata.add(modelFull);
                    }
                     adapter=new TeacherResultAdapter(ClassResultActivity.this,listdata);
                    shimmerRecycler.setAdapter(adapter);
                    shimmerRecycler.hideShimmerAdapter();
                }else {
                    Toast.makeText(ClassResultActivity.this,"No Data available",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<TeacherResultModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ClassResultActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Enter name, father's name,Roll no.");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
                finish();
                ClassResultActivity.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }






    @Override
    public void onBackPressed() {
        finish();
            ClassResultActivity.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

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

}
