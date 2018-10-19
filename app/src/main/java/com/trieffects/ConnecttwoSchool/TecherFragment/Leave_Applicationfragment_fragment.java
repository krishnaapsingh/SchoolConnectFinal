package com.trieffects.ConnecttwoSchool.TecherFragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.common.api.Api;
import com.trieffects.ConnecttwoSchool.Adapter.CustomAdapter;
import com.trieffects.ConnecttwoSchool.Adapter.NoticeboardTeacherAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.GetClassModel;
import com.trieffects.ConnecttwoSchool.Model.GetSection;
import com.trieffects.ConnecttwoSchool.Model.LeaveModel;
import com.trieffects.ConnecttwoSchool.Model.LeaveModelData;
import com.trieffects.ConnecttwoSchool.Model.SectionAndClassData;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by yarolegovich on 25.03.2017.
 */

public class Leave_Applicationfragment_fragment extends android.support.v4.app.Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    NoticeboardTeacherAdapter adapter;
    private ShimmerRecyclerView shimmerRecycler;

    AlertDialog dialog;
    LinearLayout class_rel,section_rel;
    Spinner section_spinner,class_spinner;
    List<SectionAndClassData> listSection=new ArrayList<>();
    List<SectionAndClassData> listClass=new ArrayList<>();
    TextView txt_section,txt_class;
    public static String classname,sectionName;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.teacher_leave_fragment, container, false);
        txt_section=(TextView)view.findViewById(R.id.txt_section);
        txt_class=(TextView)view.findViewById(R.id.txt_class);
        shimmerRecycler = (ShimmerRecyclerView)view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        shimmerRecycler.setLayoutManager(mLayoutManager);
        shimmerRecycler.setItemAnimator(new DefaultItemAnimator());
        shimmerRecycler.hideShimmerAdapter();
        section_rel=(LinearLayout)view.findViewById(R.id.section_rel);
        section_rel.setOnClickListener(this);
        class_rel=(LinearLayout)view.findViewById(R.id.class_rel);
        class_rel.setOnClickListener(this);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        section_spinner=(Spinner)view.findViewById(R.id.section_spinner);
        class_spinner=(Spinner)view.findViewById(R.id.class_spinner);
        SectionAndClassData model=new SectionAndClassData();
        model.id="0";
        model.my_class="Select class";
        listClass.add(model);
        section_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    sectionName=listSection.get(i).section_id;
                    txt_section.setText(adapterView.getSelectedItem().toString());
                    dialog.show();
                    shimmerRecycler.showShimmerAdapter();
                    getLeaveApplication(classname, listSection.get(i).section_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        class_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    classname=listClass.get(i).id;
                    txt_class.setText(adapterView.getSelectedItem().toString());
                    dialog.show();
                    txt_section.setText("");
                    sectionName="";
                    loadSection(classname);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dialog = new SpotsDialog(getActivity(),"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        loadClass();

        return view;
    }

    @Override
    public void onRefresh() {
        if(!ApiUtils.isEmptyString(classname)&&!ApiUtils.isEmptyString(sectionName)) {
            dialog.show();
            shimmerRecycler.showShimmerAdapter();
            getLeaveApplication(classname, sectionName);
        }
    }

    private void getLeaveApplication(String clsid, String sec_id) {
        shimmerRecycler.showShimmerAdapter();
        final ApiInterface mApiService=ApiUtils.getInterfaceService();
        Call<LeaveModel> mService=mApiService.getTeacherleaveApplication(clsid,sec_id);
        mService.enqueue(new Callback<LeaveModel>() {
            @Override
            public void onResponse(Call<LeaveModel> call, Response<LeaveModel> response) {
              LeaveModel object=response.body();
              if(object.status){
                  NoticeboardTeacherAdapter adapter=new NoticeboardTeacherAdapter(getActivity(),object.data);
                  shimmerRecycler.setAdapter(adapter);
              }else {
                  List<LeaveModelData> listdata=new ArrayList<>();
                  NoticeboardTeacherAdapter adapter=new NoticeboardTeacherAdapter(getActivity(),listdata);
                  shimmerRecycler.setAdapter(adapter);
                  Toast.makeText(getActivity(),"No Application available",Toast.LENGTH_SHORT).show();
              }
                shimmerRecycler.hideShimmerAdapter();
                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<LeaveModel> call, Throwable t) {
                shimmerRecycler.hideShimmerAdapter();
                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View view) {
        String item=null;
        Intent intent=null;
        switch (view.getId()){
            case R.id.section_rel:
                section_spinner.performClick();
                break;
            case R.id.class_rel:
                class_spinner.performClick();
                break;
        }

    }


    public void loadClass(){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<GetClassModel> mService=mApiService.getTechClass();
        mService.enqueue(new Callback<GetClassModel>() {
            @Override
            public void onResponse(Call<GetClassModel> call, Response<GetClassModel> response) {
                GetClassModel object=response.body();
                if(object.status){
                    listClass.addAll(object.data.classlist);
                    CustomAdapter adapter=new CustomAdapter(getActivity(),R.layout.listitems_layout,R.id.title_txt,listClass);
                    class_spinner.setAdapter(adapter);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetClassModel> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public void loadSection(String cls_id){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<GetSection> mService=mApiService.getTechSection(cls_id);
        mService.enqueue(new Callback<GetSection>() {
            @Override
            public void onResponse(Call<GetSection> call, Response<GetSection> response) {
                GetSection object=response.body();
                if(object.status){
                    listSection.clear();
                    SectionAndClassData model=new SectionAndClassData();
                    model.section="Select section";
                    model.id="0";
                    listSection.add(model);
                    listSection.addAll(object.data);
                    CustomAdapter adapter=new CustomAdapter(getActivity(),R.layout.listitems_layout,R.id.title_txt,listSection);
                    section_spinner.setAdapter(adapter);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetSection> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }



}
