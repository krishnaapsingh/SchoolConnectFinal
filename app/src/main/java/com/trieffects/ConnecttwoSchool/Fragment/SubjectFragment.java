package com.trieffects.ConnecttwoSchool.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.plus.model.people.Person;
import com.trieffects.ConnecttwoSchool.Adapter.SubjectAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Model.GetSubjectModel;
import com.trieffects.ConnecttwoSchool.Model.SubjectDataList;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shashikeshkumar on 30/03/18.
 */

public class SubjectFragment extends Fragment {

    ShimmerRecyclerView subject_rv;
    RecyclerView.LayoutManager manager;
   SubjectAdapter adapter;
    Spinner spinner_std;
    LinearLayout select_student_layout;
    List<ChildData> list;
    TextView std_name;
    AlertDialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.subject_fragment,container,false);
        subject_rv=view.findViewById(R.id.subject_rv);
        spinner_std=(Spinner)view.findViewById(R.id.spinner_std);
        std_name=(TextView)view.findViewById(R.id.std_name);
        manager=new GridLayoutManager(getActivity(),2);
        subject_rv.setLayoutManager(manager);
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
                    loadSubject(list.get(i).class_id,list.get(i).section_id);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            select_student_layout.setVisibility(View.GONE);
            dialog.show();
            loadSubject(PrefrencesUtils.getClassId(), PrefrencesUtils.getSectionId());
        }
        return view;
    }
    public void childData(){
        if(PrefrencesUtils.getChildInfo()!=null) {
            list = PrefrencesUtils.getChildInfo();
            ArrayAdapter<ChildData> dataAdapter = new ArrayAdapter<ChildData>(getActivity(), android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_std.setAdapter(dataAdapter);
            if(list.size()==1){
                dialog.show();
                loadSubject(list.get(0).class_id,list.get(0).student_id);
                select_student_layout.setVisibility(View.GONE);
            }
        }
    }

    public void loadSubject(String classid,String section_id){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<GetSubjectModel> mService=mApiService.getSubjectList(classid,section_id);
        mService.enqueue(new Callback<GetSubjectModel>() {
            @Override
            public void onResponse(Call<GetSubjectModel> call, Response<GetSubjectModel> response) {
                GetSubjectModel object=response.body();
                if(object.status){
                    adapter=new SubjectAdapter(object.data,getActivity());
                    subject_rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetSubjectModel> call, Throwable t) {
                dialog.dismiss();
            }
        });

    }
}
