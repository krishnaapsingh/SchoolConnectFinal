package com.trieffects.ConnecttwoSchool.TecherFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.trieffects.ConnecttwoSchool.Adapter.CustomAdapter;
import com.trieffects.ConnecttwoSchool.Adapter.ExamLIstAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ExamModel;
import com.trieffects.ConnecttwoSchool.Model.GetClassModel;
import com.trieffects.ConnecttwoSchool.Model.GetSection;
import com.trieffects.ConnecttwoSchool.Model.SectionAndClassData;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherResultShowFragment extends Fragment implements View.OnClickListener {
    AlertDialog dialog;
    ExamLIstAdapter adapter;
    private ShimmerRecyclerView shimmerRecycler;

    LinearLayout class_rel,section_rel;
    Spinner section_spinner,class_spinner;
   List<SectionAndClassData> listSection=new ArrayList<>();
    List<SectionAndClassData> listClass=new ArrayList<>();

     TextView txt_section,txt_class;
     ImageView back_img;
   public static String classname,sectionName;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.teacher_resultshow_view,container,false);

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
                    getCurrentNotice(classname, listSection.get(i).section_id);
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


    public int validation(){
        if(ApiUtils.isEmptyString(classname)){
            Toast.makeText(getActivity(),"Please Select class name",Toast.LENGTH_SHORT).show();
        }else if(ApiUtils.isEmptyString(sectionName)){
            Toast.makeText(getActivity(),"Please Select class name",Toast.LENGTH_SHORT).show();
        }else {
            return 1;
        }
        return -1;
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


    public void getCurrentNotice(String id,String sectionid){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<ExamModel> mService=mApiService.getExamList(id,sectionid);
        mService.enqueue(new Callback<ExamModel>(){
            @Override
            public void onResponse(Call<ExamModel> call, Response<ExamModel> response) {
                ExamModel object=response.body();
                if(object.status==true){
                    adapter=new ExamLIstAdapter(object.data.result_array,getActivity(),2);
                    shimmerRecycler.setAdapter(adapter);
                    shimmerRecycler.hideShimmerAdapter();
                    dialog.dismiss();
                }else {
                    dialog.dismiss();
                    shimmerRecycler.hideShimmerAdapter();
                    Toast.makeText(getActivity(),"No data found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExamModel> call, Throwable t) {
                dialog.dismiss();
                shimmerRecycler.hideShimmerAdapter();
                Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }



}
