package com.trieffects.ConnecttwoSchool.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Model.LeaveApplicationModel;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveApplicationLIst extends AppCompatActivity {
    CardView start_date,end_date;
    TextView txt_startdate,txt_enddate,txt_totalday,discription_txt;
    Spinner spinner_std;
    LinearLayout select_student_layout;
    static int check=0;
    List<ChildData> list;
    AlertDialog dialog;
    ActionBar actionBar;
    TextView std_name;
    String std_id,cls_id,sec_id,stdname;
    Button submit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Leave Application");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);

        start_date=(CardView)findViewById(R.id.start_date);
        end_date=(CardView)findViewById(R.id.end_date);
        discription_txt=(TextView)findViewById(R.id.discription_txt);
        txt_startdate=(TextView)findViewById(R.id.txt_start);
        txt_enddate=(TextView)findViewById(R.id.txt_end);
        txt_totalday=(TextView)findViewById(R.id.txt_total);
        submit_btn=(Button)findViewById(R.id.submit_btn);
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        txt_startdate.setText((month+1)+"-"+day+"-"+year);
        txt_enddate.setText((month+1)+"-"+day+"-"+year);
        txt_totalday.setText(getDate(txt_startdate.getText().toString().trim(),txt_enddate.getText().toString().trim()));
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check=0;
                DialogFragment dialogfragment = new DatePickerDialogTheme();
                dialogfragment.show(getFragmentManager(), "Theme");
            }
        });

        dialog = new SpotsDialog(LeaveApplicationLIst.this,"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check=1;
                DialogFragment dialogfragment = new DatePickerDialogTheme();
                dialogfragment.show(getFragmentManager(), "Theme");
            }
        });
        spinner_std=(Spinner)findViewById(R.id.spinner_std);
        std_name=(TextView)findViewById(R.id.std_name);
        select_student_layout=(LinearLayout)findViewById(R.id.select_student_layout);
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
                    std_id = list.get(i).student_id;
                    cls_id=list.get(i).class_id;
                    sec_id=list.get(i).section_id;
                    if(!ApiUtils.isEmptyString(list.get(i).lastname)){
                        stdname=list.get(i).firstname+" "+list.get(i).lastname;
                    }else {
                        stdname=list.get(i).firstname;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            select_student_layout.setVisibility(View.GONE);
            stdname =PrefrencesUtils.getUserName();
            std_id=PrefrencesUtils.getUserId();
            cls_id=PrefrencesUtils.getClassId();
            sec_id=PrefrencesUtils.getSectionId();
        }

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()==1){
                    dialog.show();
                    submitLeaveApplication(std_id,txt_startdate.getText().toString().trim(),txt_enddate.getText().toString().trim(),discription_txt.getText().toString().trim(),cls_id,sec_id,stdname);
                }
            }
        });
    }

    public int validation(){

        if(Integer.parseInt(txt_totalday.getText().toString().trim())==0){
            Toast.makeText(LeaveApplicationLIst.this,"Please select start and end date",Toast.LENGTH_SHORT).show();
        }else  if(Integer.parseInt(txt_totalday.getText().toString().trim())<0){
            Toast.makeText(LeaveApplicationLIst.this,"End data also greater then start date",Toast.LENGTH_SHORT).show();
        }else if(discription_txt.getText().toString().trim().equalsIgnoreCase("")){
            Toast.makeText(LeaveApplicationLIst.this,"Write you reason for leave",Toast.LENGTH_SHORT).show();
        }else {
            return 1;
        }

        return -1;
    }

    public void childData(){
        if(PrefrencesUtils.getChildInfo()!=null) {
            list = PrefrencesUtils.getChildInfo();
            ArrayAdapter<ChildData> dataAdapter = new ArrayAdapter<ChildData>(LeaveApplicationLIst.this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_std.setAdapter(dataAdapter);
            if (list.size() == 1) {
                std_id = list.get(0).student_id;
                cls_id=list.get(0).class_id;
                sec_id=list.get(0).section_id;
                if(!ApiUtils.isEmptyString(list.get(0).lastname)){
                    stdname=list.get(0).firstname+" "+list.get(0).lastname;
                }else {
                    stdname=list.get(0).firstname;
                }
                select_student_layout.setVisibility(View.GONE);
            }
        }
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
            String value=(month+1)+"-" +day+"-"+year;
            TextView  txt_startdate=(TextView)getActivity().findViewById(R.id.txt_start);
            TextView  txt_enddate=(TextView)getActivity().findViewById(R.id.txt_end);
            TextView  txt_totalday=(TextView)getActivity().findViewById(R.id.txt_total);
            if(check==0){
                txt_startdate.setText(value);
            }else {

                txt_enddate.setText(value);
            }
            txt_totalday.setText(getDate(txt_startdate.getText().toString().trim(),txt_enddate.getText().toString().trim()));
        }
    }

    public static String getDate(String inputString1,String inputString2){
        SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy");
        long diff=00;
        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            diff = date2.getTime() - date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }


    public void submitLeaveApplication(String std_id,String std_ldt_frm,String std_ldt_till,String std_leave_r,String class_id,String section_id,String usrname){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<LeaveApplicationModel> mService=mApiService.submitLeave(std_id,std_ldt_frm,std_ldt_till,std_leave_r,class_id,section_id,usrname);
        mService.enqueue(new Callback<LeaveApplicationModel>() {
            @Override
            public void onResponse(Call<LeaveApplicationModel> call, Response<LeaveApplicationModel> response) {
                LeaveApplicationModel object=response.body();
                if(object.status){
                    finish();
                    LeaveApplicationLIst.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }else {
                    Toast.makeText(getApplicationContext(),"Something wrong",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<LeaveApplicationModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            LeaveApplicationLIst.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LeaveApplicationLIst.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
