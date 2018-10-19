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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.trieffects.ConnecttwoSchool.Adapter.ChildAdapter;
import com.trieffects.ConnecttwoSchool.Adapter.NoticeboardAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Model.Noticeboard;
import com.trieffects.ConnecttwoSchool.Model.NotificationData;
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

public class NoticeboardFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    NoticeboardAdapter adapter;
    private ShimmerRecyclerView shimmerRecycler;
    List<NotificationData> listData=new ArrayList<>();
    AlertDialog dialog;
    Spinner childSpinner;
    ArrayList<ChildData> data=new ArrayList<>();
    String std_Id;
    LinearLayout spinner1_layout;
    TextView std_name;
    private SwipeRefreshLayout swipeRefreshLayout;
    String classId,sectionId;

    public static int count;
    public static int positscroll;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_text, container, false);
        count=1;
        positscroll=0;
        shimmerRecycler = (ShimmerRecyclerView)view.findViewById(R.id.recycler_view);
        spinner1_layout=(LinearLayout)view.findViewById(R.id.select_student_layout);
        std_name=(TextView)view.findViewById(R.id.std_name);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        shimmerRecycler.setLayoutManager(mLayoutManager);
        shimmerRecycler.setItemAnimator(new DefaultItemAnimator());
        shimmerRecycler.showShimmerAdapter();
        shimmerRecycler.showShimmerAdapter();
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        childSpinner= (Spinner)view.findViewById(R.id.spinner_std);
        spinner1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childSpinner.performClick();
            }
        });
        dialog = new SpotsDialog(getActivity(), "Please wait", R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if(PrefrencesUtils.getUserMode().equalsIgnoreCase("3")) {
            loadChild();
            childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    std_Id = data.get(position).student_id;
                    std_name.setText(adapterView.getSelectedItem().toString());
                    classId=data.get(position).class_id;
                    sectionId=data.get(position).section_id;
                    dialog.show();
                    getCurrentNotice(data.get(position).class_id,data.get(position).section_id,std_Id,String.valueOf(count));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            spinner1_layout.setVisibility(View.GONE);
            dialog.show();
            std_Id=PrefrencesUtils.getId();
            classId=PrefrencesUtils.getClassId();
            sectionId=PrefrencesUtils.getSectionId();
            getCurrentNotice(PrefrencesUtils.getClassId(),PrefrencesUtils.getSectionId(),std_Id,String.valueOf(count));
        }


        return view;
    }

    public void loadmore(){
        count++;
        dialog.show();
        //shimmerRecycler.showShimmerAdapter();
        getCurrentNotice(classId, sectionId,std_Id,String.valueOf(count));
    }
    @Override
    public void onRefresh() {
        if(!ApiUtils.isEmptyString(classId)&&!ApiUtils.isEmptyString(sectionId)) {
            dialog.show();
            positscroll=0;
            shimmerRecycler.showShimmerAdapter();
            getCurrentNotice(classId, sectionId,std_Id,String.valueOf(count));
        }
    }



    public void getCurrentNotice(String cls_id,String sec_id,String student_id,String countvalue){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
       Call<Noticeboard> mService=mApiService.getNotice(cls_id,sec_id,student_id,countvalue);
       mService.enqueue(new Callback<Noticeboard>() {
           @Override
           public void onResponse(Call<Noticeboard> call, Response<Noticeboard> response) {
                Noticeboard object=response.body();
                if(object.status==true){
                    adapter=new NoticeboardAdapter(object.data,NoticeboardFragment.this);
                    shimmerRecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    shimmerRecycler.hideShimmerAdapter();
                    if(positscroll>3){
                        shimmerRecycler.scrollToPosition(positscroll-3);
                    }else {
                        positscroll=object.data.size();
                    }
                    dialog.dismiss();
                }else {
                    List<NotificationData> list=new ArrayList<>();
                    shimmerRecycler.setAdapter(adapter);
                    adapter=new NoticeboardAdapter(list,NoticeboardFragment.this);
                    shimmerRecycler.hideShimmerAdapter();
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    Toast.makeText(getActivity(),"No data found",Toast.LENGTH_SHORT).show();
                }
               swipeRefreshLayout.setRefreshing(false);
           }

           @Override
           public void onFailure(Call<Noticeboard> call, Throwable t) {
               dialog.dismiss();
               Toast.makeText(getActivity(),"No data found",Toast.LENGTH_SHORT).show();
               shimmerRecycler.hideShimmerAdapter();
               swipeRefreshLayout.setRefreshing(false);
           }
       });
    }

    public void loadChild(){
        if(PrefrencesUtils.getChildInfo()!=null) {
            data = PrefrencesUtils.getChildInfo();
            if (data.size() > 1) {
                ChildAdapter adapter = new ChildAdapter(getActivity(), R.layout.child_view, R.id.title_txt, data);
                childSpinner.setAdapter(adapter);
            } else {
                std_Id = data.get(0).student_id;
                classId=data.get(0).class_id;
                sectionId=data.get(0).section_id;
                getCurrentNotice( data.get(0).class_id, data.get(0).section_id,std_Id,String.valueOf(count));
                spinner1_layout.setVisibility(View.GONE);
            }
        }
    }

}
