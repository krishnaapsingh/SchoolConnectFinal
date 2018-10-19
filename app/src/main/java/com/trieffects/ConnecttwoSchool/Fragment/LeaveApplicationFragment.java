package com.trieffects.ConnecttwoSchool.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.trieffects.ConnecttwoSchool.Activity.LeaveApplicationLIst;
import com.trieffects.ConnecttwoSchool.Adapter.LeaveInformationAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Model.LeaveModel;
import com.trieffects.ConnecttwoSchool.Model.LeaveModelData;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeaveApplicationFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {
    private ShimmerRecyclerView shimmerRecycler;
    List<ChildData> list;
    Spinner spinner_std;
    LinearLayout select_student_layout;
    TextView std_name;
    List<LeaveModelData> dataList=new ArrayList<>();
    AlertDialog dialog;
    ImageView plus_btn;
    String stdId;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.leave_application_fragment,container,false);
        shimmerRecycler = (ShimmerRecyclerView)view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        spinner_std=(Spinner)view.findViewById(R.id.spinner_std);
        shimmerRecycler.setLayoutManager(mLayoutManager);
        shimmerRecycler.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        dialog = new SpotsDialog(getActivity(),"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        std_name=(TextView)view.findViewById(R.id.std_name);
        plus_btn=(ImageView)view.findViewById(R.id.plus_btn);

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
                    //dialog.show();
                    std_name.setText(adapterView.getSelectedItem().toString());
                    shimmerRecycler.showShimmerAdapter();
                    dialog.show();
                    stdId=list.get(i).student_id;
                    loadLeaveApplication(list.get(i).student_id);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            select_student_layout.setVisibility(View.GONE);
            shimmerRecycler.showShimmerAdapter();
            dialog.show();
            stdId=PrefrencesUtils.getUserId();
            loadLeaveApplication(PrefrencesUtils.getUserId());
        }

        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LeaveApplicationLIst.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        return view;
    }
    public void childData(){
        if(PrefrencesUtils.getChildInfo()!=null) {
            list = PrefrencesUtils.getChildInfo();
            ArrayAdapter<ChildData> dataAdapter = new ArrayAdapter<ChildData>(getActivity(), android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_std.setAdapter(dataAdapter);
            if (list.size()==1) {
                stdId=list.get(0).student_id;
                loadLeaveApplication(list.get(0).student_id);
                select_student_layout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRefresh() {
        if(!ApiUtils.isEmptyString(stdId)) {
            dialog.show();
            shimmerRecycler.showShimmerAdapter();
            loadLeaveApplication(stdId);
        }
    }

    public void loadLeaveApplication(String std_id){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<LeaveModel> mService=mApiService.getleaveApplication(std_id);
        mService.enqueue(new Callback<LeaveModel>() {
            @Override
            public void onResponse(Call<LeaveModel> call, Response<LeaveModel> response) {
                LeaveModel object=response.body();
                if(object.status){
                    dataList=object.data;
                    LeaveInformationAdapter adapter=new LeaveInformationAdapter(getActivity(),dataList);
                    shimmerRecycler.setAdapter(adapter);
                      shimmerRecycler.hideShimmerAdapter();
                }else {
                    dataList.clear();
                    LeaveInformationAdapter adapter=new LeaveInformationAdapter(getActivity(),dataList);
                    shimmerRecycler.setAdapter(adapter);
                    shimmerRecycler.hideShimmerAdapter();
                    Toast.makeText(getActivity(),"No Data found",Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<LeaveModel> call, Throwable t) {
                dialog.dismiss();
                shimmerRecycler.hideShimmerAdapter();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


}
