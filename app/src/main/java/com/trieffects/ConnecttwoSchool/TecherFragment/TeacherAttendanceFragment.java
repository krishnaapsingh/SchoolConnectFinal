package com.trieffects.ConnecttwoSchool.TecherFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Activity.AttendanceActivity;
import com.trieffects.ConnecttwoSchool.Adapter.CustomAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.GetClassModel;
import com.trieffects.ConnecttwoSchool.Model.GetSection;
import com.trieffects.ConnecttwoSchool.Model.SectionAndClassData;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherAttendanceFragment extends Fragment implements View.OnClickListener{
    AlertDialog dialog;
    CardView start_date;
    LinearLayout change_layout;
    Button attendance_btn,att_view_btn,att_modify_btn;
    LinearLayout class_rel,section_rel;
    Spinner section_spinner,class_spinner;
   List<SectionAndClassData> listSection=new ArrayList<>();
    List<SectionAndClassData> listClass=new ArrayList<>();
    TextView txt_start;
   TextView txt_section,txt_class;
    String classname,sectionName;
    String class_id,section_id;
    public String currentdate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.teacher_attendance_view,container,false);

        txt_section=(TextView)view.findViewById(R.id.txt_section);
        txt_class=(TextView)view.findViewById(R.id.txt_class);

        start_date=(CardView)view.findViewById(R.id.start_date);
        change_layout=(LinearLayout)view.findViewById(R.id.change_layout);
        attendance_btn=(Button)view.findViewById(R.id.attendance_btn);
        att_modify_btn=(Button)view.findViewById(R.id.att_modify_btn);
        att_view_btn=(Button)view.findViewById(R.id.att_view_btn);
        section_rel=(LinearLayout)view.findViewById(R.id.section_rel);
        section_rel.setOnClickListener(this);
        class_rel=(LinearLayout)view.findViewById(R.id.class_rel);
        class_rel.setOnClickListener(this);
        attendance_btn.setOnClickListener(this);
        att_modify_btn.setOnClickListener(this);
        att_view_btn.setOnClickListener(this);
        change_layout.setVisibility(View.GONE);
        txt_start=(TextView)view.findViewById(R.id.txt_start);
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        currentdate=year+"-"+(month+1)+"-"+day;
        txt_start.setText(year+"-"+(month+1)+"-"+day);
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogfragment = new DatePickerDialogTheme();
                dialogfragment.show(getFragmentManager(), "Theme");
            }
        });

        section_spinner=(Spinner)view.findViewById(R.id.section_spinner);
        class_spinner=(Spinner)view.findViewById(R.id.class_spinner);
        SectionAndClassData model=new SectionAndClassData();
        model.my_class="Select class";
        listClass.add(model);
        section_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    sectionName=adapterView.getSelectedItem().toString();
                    section_id=listSection.get(i).section_id;
                    txt_section.setText(sectionName);
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
                    classname=adapterView.getSelectedItem().toString();
                    class_id=listClass.get(i).id;
                    txt_class.setText(classname);
                    dialog.show();
                    txt_section.setText("");
                    section_id="";
                    loadSection(class_id);
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
            case R.id.attendance_btn:
                if(validation()==1&&currentdate.equalsIgnoreCase(txt_start.getText().toString().trim())) {
                    item = "0";
                    intent = new Intent(getActivity(), AttendanceActivity.class);
                    intent.putExtra("data", item);
                    intent.putExtra("classname",classname);
                    intent.putExtra("sectionname",sectionName);
                    intent.putExtra("class_id",class_id);
                    intent.putExtra("section_id",section_id);
                    intent.putExtra("date",txt_start.getText().toString().trim());
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(),"Please choose current date",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.att_modify_btn:
                if(validation()==1) {
                    item = "2";
                    intent = new Intent(getActivity(), AttendanceActivity.class);
                    intent.putExtra("data", item);
                    intent.putExtra("classname",classname);
                    intent.putExtra("sectionname",sectionName);
                    intent.putExtra("class_id",class_id);
                    intent.putExtra("section_id",section_id);
                    intent.putExtra("date",txt_start.getText().toString().trim());
                    startActivity(intent);
                }
                break;
            case R.id.att_view_btn:
                if(validation()==1) {
                    item = "1";
                    intent = new Intent(getActivity(), AttendanceActivity.class);
                    intent.putExtra("data", item);
                    intent.putExtra("classname",classname);
                    intent.putExtra("sectionname",sectionName);
                    intent.putExtra("class_id",class_id);
                    intent.putExtra("section_id",section_id);
                    intent.putExtra("date",txt_start.getText().toString().trim());
                    startActivity(intent);
                }

                break;
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


    public static  class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);
            datepickerdialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            String value=year+"-"+(month+1)+"-" +day;
            TextView txt_startdate=(TextView)getActivity().findViewById(R.id.txt_start);
           LinearLayout change_layout=(LinearLayout)getActivity().findViewById(R.id.change_layout);
            txt_startdate.setText(value);
            change_layout.setVisibility(View.VISIBLE);

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
