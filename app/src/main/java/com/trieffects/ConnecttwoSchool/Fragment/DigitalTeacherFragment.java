package com.trieffects.ConnecttwoSchool.Fragment;


import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.trieffects.ConnecttwoSchool.Adapter.DigitalStdAdapter;
import com.trieffects.ConnecttwoSchool.Adapter.ExamLIstAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Model.DigitalParentModel;
import com.trieffects.ConnecttwoSchool.Model.ExamData;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
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

public class DigitalTeacherFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ExamLIstAdapter adapter;
    private ShimmerRecyclerView shimmerRecycler;
    List<ExamData> listData=new ArrayList<>();
    AlertDialog dialog;
    Spinner spinner_std;
       LinearLayout select_student_layout;
    List<ChildData> list;
    TextView std_name;
    private SwipeRefreshLayout swipeRefreshLayout;
   public static String classid,sectionid,std_id,imageUrl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.digitalfragment_text, container, false);
        shimmerRecycler = (ShimmerRecyclerView)view.findViewById(R.id.recycler_view);
        std_name=(TextView)view.findViewById(R.id.std_name);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        shimmerRecycler.setLayoutManager(mLayoutManager);
        shimmerRecycler.setItemAnimator(new DefaultItemAnimator());
        spinner_std=(Spinner)view.findViewById(R.id.spinner_std);
        dialog = new SpotsDialog(getActivity(),"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        select_student_layout=(LinearLayout)view.findViewById(R.id.select_student_layout);
        select_student_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner_std.performClick();
            }
        });

        if(PrefrencesUtils.getUserMode().equalsIgnoreCase("3")) {
            childData();
            spinner_std.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    dialog.show();
                    std_name.setText(adapterView.getSelectedItem().toString());
                    classid = list.get(i).class_id;
                    sectionid = list.get(i).section_id;
                    std_id = list.get(i).student_id;
                    imageUrl = list.get(i).image;
                    shimmerRecycler.showShimmerAdapter();
                    getCurrentNotice(list.get(i).class_id, list.get(i).section_id);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            select_student_layout.setVisibility(View.GONE);
            classid = PrefrencesUtils.getClassId();
            sectionid = PrefrencesUtils.getSectionId();
            std_id = PrefrencesUtils.getUserId();
            imageUrl = PrefrencesUtils.getImage();
            shimmerRecycler.showShimmerAdapter();
            getCurrentNotice(PrefrencesUtils.getClassId(),PrefrencesUtils.getSectionId() );
            //Toast.makeText(getActivity(),"Student functionality under Development",Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onRefresh() {
        if(!ApiUtils.isEmptyString(classid)&&!ApiUtils.isEmptyString(sectionid)) {
            dialog.show();
            shimmerRecycler.showShimmerAdapter();
            getCurrentNotice(classid, sectionid);
        }
    }


    public void childData(){
        if(PrefrencesUtils.getChildInfo()!=null) {
            list = PrefrencesUtils.getChildInfo();
            ArrayAdapter<ChildData> dataAdapter = new ArrayAdapter<ChildData>(getActivity(), android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_std.setAdapter(dataAdapter);
            if(list.size()==1){
                classid = list.get(0).class_id;
                sectionid = list.get(0).section_id;
                std_id = list.get(0).student_id;
                imageUrl = list.get(0).image;
                getCurrentNotice(list.get(0).class_id,list.get(0).section_id);
                select_student_layout.setVisibility(View.GONE);
            }
        }
    }



    public void getCurrentNotice(String id,String sectionid){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
       Call<DigitalParentModel> mService=mApiService.getDigitalTeaacherList(id,sectionid);
       mService.enqueue(new Callback<DigitalParentModel>(){
           @Override
           public void onResponse(Call<DigitalParentModel> call, Response<DigitalParentModel> response) {
               DigitalParentModel object=response.body();
                if(object.status==true){
                    DigitalStdAdapter adapter=new DigitalStdAdapter(getActivity(),object.data);
                    shimmerRecycler.setAdapter(adapter);
                    shimmerRecycler.hideShimmerAdapter();
                    dialog.dismiss();
                }else {
                    Toast.makeText(getActivity(),"No data found",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    shimmerRecycler.hideShimmerAdapter();
                }
           }

           @Override
           public void onFailure(Call<DigitalParentModel> call, Throwable t) {
               dialog.dismiss();
               shimmerRecycler.hideShimmerAdapter();
               Toast.makeText(getActivity(),"Check your internet connection ",Toast.LENGTH_SHORT).show();
           }
       });
    }

}
