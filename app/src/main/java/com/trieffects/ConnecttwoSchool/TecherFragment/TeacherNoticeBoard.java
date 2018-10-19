package com.trieffects.ConnecttwoSchool.TecherFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Adapter.CustomAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.GetClassModel;
import com.trieffects.ConnecttwoSchool.Model.GetSection;
import com.trieffects.ConnecttwoSchool.Model.HomeWorkModelUpload;
import com.trieffects.ConnecttwoSchool.Model.SectionAndClassData;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherNoticeBoard extends Fragment implements View.OnClickListener{
    List<SectionAndClassData> listSection=new ArrayList<>();
    List<SectionAndClassData> listClass=new ArrayList<>();
    LinearLayout class_rel,section_rel;
    Spinner section_spinner,class_spinner;
    TextView txt_section,txt_class,date_txt;
    String class_id,section_id;
    AlertDialog dialog;
    RelativeLayout start_date;
    EditText edt_description,txt_subject;
    Button submit_btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.teacher_notice_boardview,container,false);
        txt_section=(TextView)view.findViewById(R.id.txt_section);
        txt_class=(TextView)view.findViewById(R.id.txt_class);
        date_txt=(TextView)view.findViewById(R.id.txt_start);
        start_date=(RelativeLayout)view.findViewById(R.id.start_date);

        submit_btn=(Button)view.findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(this);
        section_rel=(LinearLayout)view.findViewById(R.id.section_rel);
        section_rel.setOnClickListener(this);
        class_rel=(LinearLayout)view.findViewById(R.id.class_rel);
        class_rel.setOnClickListener(this);
        txt_subject=(EditText) view.findViewById(R.id.txt_subject);
        edt_description=(EditText) view.findViewById(R.id.edt_description);
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
                    section_id=adapterView.getSelectedItem().toString();
                    section_id=listSection.get(i).section_id;
                    txt_section.setText(adapterView.getSelectedItem().toString());
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
                    class_id=adapterView.getSelectedItem().toString();
                    class_id=listClass.get(i).id;
                    txt_class.setText(adapterView.getSelectedItem().toString());
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
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.section_rel:
                section_spinner.performClick();
                break;
            case R.id.class_rel:
                class_spinner.performClick();
                break;
            case R.id.submit_btn:
                if(validation()==1){
                    dialog.show();
                    noticeUpload(PrefrencesUtils.getUserId(),class_id,section_id,date_txt.getText().toString().trim(),txt_subject.getText().toString().trim(),edt_description.getText().toString().trim());
                }
                break;
        }
    }

    private void noticeUpload(String teacher_id, final String classid, String sectionid, String publish_date, String title, String message) {
        final ApiInterface mApiService=ApiUtils.getInterfaceService();
        Call<HomeWorkModelUpload> mService=mApiService.noticeUpload(teacher_id,classid,sectionid,publish_date,title,message);
        mService.enqueue(new Callback<HomeWorkModelUpload>() {
            @Override
            public void onResponse(Call<HomeWorkModelUpload> call, Response<HomeWorkModelUpload> response) {
                HomeWorkModelUpload object=response.body();
                if(object.status){
                    txt_subject.setText("");
                    date_txt.setText("");
                    edt_description.setText("");
                    Toast.makeText(getActivity(),"Notice send successfully ",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getActivity(),object.message,Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<HomeWorkModelUpload> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public int validation(){
        if(ApiUtils.isEmptyString(class_id)){
            Toast.makeText(getActivity(),"Please Select class",Toast.LENGTH_SHORT).show();
        }else if(ApiUtils.isEmptyString(section_id)){
            Toast.makeText(getActivity(),"Please Select section",Toast.LENGTH_SHORT).show();
        }else if(ApiUtils.isEmptyString(txt_subject.getText().toString().trim())){
            txt_subject.setError("Please enter notice subject");
        }else if(ApiUtils.isEmptyString(date_txt.getText().toString().trim())){
            Toast.makeText(getActivity(),"Please Select notice publish date",Toast.LENGTH_SHORT).show();
        }else if(ApiUtils.isEmptyString(edt_description.getText().toString().trim())){
            edt_description.setError("Please enter notice description");
        }else{
            return  1;
        }
        return  -1;
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
            datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis()- 1000);

            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            String value=year+"-"+(month+1)+"-" +day;
            TextView txt_startdate=(TextView)getActivity().findViewById(R.id.txt_start);
            txt_startdate.setText(value);

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
                    listClass.clear();
                    SectionAndClassData model=new SectionAndClassData();
                    model.my_class="Select class";
                    listClass.add(model);
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
        listSection.clear();
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<GetSection> mService=mApiService.getTechSection(cls_id);
        mService.enqueue(new Callback<GetSection>() {
            @Override
            public void onResponse(Call<GetSection> call, Response<GetSection> response) {
                GetSection object=response.body();
                if(object.status){
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
