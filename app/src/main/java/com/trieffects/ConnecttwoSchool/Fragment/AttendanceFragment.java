package com.trieffects.ConnecttwoSchool.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.trieffects.ConnecttwoSchool.Adapter.CalenderAdapter1;
import com.trieffects.ConnecttwoSchool.Adapter.ChildAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.AttendanceModel;
import com.trieffects.ConnecttwoSchool.Model.CalenderModel;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AttendanceFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {
    int[] monthDay={31,28,31,30,31,30,31,31,30,31,30,31};
    String[] monthName={"January","February","March","April","May","June","July","August","September","October","November","December"};
    int k=0;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    CalenderAdapter1 adapter;
     Calendar calendar;
     Button heading;
     List<CalenderModel> list=new ArrayList<>();
    Spinner monthSpinner,childSpinner;
    int check=0,updateday;
    AlertDialog dialog;
    LinearLayout spinner1_layout;
    List<AttendanceList> listAttend=new ArrayList<>();
    ArrayList<ChildData> data=new ArrayList<>();
    String std_Id,calenderPosition;
    int changeRecycler=0;
    private SwipeRefreshLayout swipeRefreshLayout;
    String yearvalue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.attendance_fragment,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        heading=(Button)view.findViewById(R.id.heading);
        spinner1_layout=(LinearLayout) view.findViewById(R.id.spinner1_layout);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        dialog = new SpotsDialog(getActivity(),"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        calendar=Calendar.getInstance();
        int mont=calendar.get(Calendar.MONTH);
        monthSpinner = (Spinner)view.findViewById(R.id.monthSpinner);
        childSpinner= (Spinner)view.findViewById(R.id.childSpinner);
        if(PrefrencesUtils.getUserMode().equalsIgnoreCase("3")) {
            loadChild();
            childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    std_Id = data.get(position).student_id;
                    if (!ApiUtils.isEmptyString(calenderPosition)) {
                        list.clear();
                        dialog.show();
                        yearvalue=String.valueOf(calendar.get(Calendar.YEAR));
                        getAttendance(String.valueOf(calendar.get(Calendar.YEAR)), calenderPosition, std_Id);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            spinner1_layout.setVisibility(View.GONE);
            std_Id=PrefrencesUtils.getUserId();
        }

        spinner1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childSpinner.performClick();
            }
        });
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.month_of_year, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter1);

        monthSpinner.setSelection((mont+1));
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position!=0){
                    list.clear();
                    calenderPosition=String.valueOf(position);
                    dialog.show();
                    if(6 >= (position)){
                        heading.setText(String.valueOf(calendar.get(Calendar.YEAR)));
                        yearvalue=String.valueOf(calendar.get(Calendar.YEAR));
                        getAttendance(String.valueOf(calendar.get(Calendar.YEAR)),String.valueOf(position),std_Id);
                    }else {
                        heading.setText(String.valueOf((calendar.get(Calendar.YEAR)-1)));
                        yearvalue=String.valueOf((calendar.get(Calendar.YEAR)-1));

                        getAttendance(String.valueOf((calendar.get(Calendar.YEAR)-1)),String.valueOf(position),std_Id);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;
    }

    @Override
    public void onRefresh() {
        if(!ApiUtils.isEmptyString(yearvalue)&&!ApiUtils.isEmptyString(std_Id)&&!ApiUtils.isEmptyString(calenderPosition)) {
            dialog.show();
            getAttendance(yearvalue,String.valueOf(calenderPosition),std_Id);
        }
    }


    public void loadChild(){
        if(PrefrencesUtils.getChildInfo()!=null) {
            data = PrefrencesUtils.getChildInfo();
            if (data.size() > 1) {
                ChildAdapter adapter = new ChildAdapter(getActivity(), R.layout.child_view, R.id.title_txt, data);
                childSpinner.setAdapter(adapter);
            } else {
                std_Id = data.get(0).student_id;
                spinner1_layout.setVisibility(View.GONE);
            }
        }
    }


    public void getAttendance(final String year,final String month,String id){
        listAttend.clear();
        list.clear();
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<AttendanceModel> mServices=mApiService.GetAttendance(year,month,id);
        mServices.enqueue(new Callback<AttendanceModel>() {
            @Override
            public void onResponse(Call<AttendanceModel> call, Response<AttendanceModel> response) {
                AttendanceModel object=response.body();
                if(object.status==true){
                    for(int i=0;i<object.data.size();i++){
                        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                        Date testDate = null;
                        try {
                            testDate = format1.parse(object.data.get(i).date);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        SimpleDateFormat formatter = new SimpleDateFormat("dd");
                        String newFormat = formatter.format(testDate);
                       AttendanceList model=new AttendanceList();
                       model.setDate(newFormat);
                       model.setTitle(object.data.get(i).title);
                       listAttend.add(model);
                    }
                    dialog.dismiss();
                }else{
                    listAttend.clear();
                    dialog.dismiss();
                }
                swipeRefreshLayout.setRefreshing(false);
                calenderLoad(Integer.parseInt(month),Integer.parseInt(year));
            }

            @Override
            public void onFailure(Call<AttendanceModel> call, Throwable t) {
                calenderLoad(Integer.parseInt(month),Integer.parseInt(year));
                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    public void calenderLoad(int mont,int yearv){
        changeRecycler=0;
        Calendar c = Calendar.getInstance();
        String input_date="01/"+(mont)+"/"+yearv;
        SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
        Date dt1= null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2=new SimpleDateFormat("EEEE");
        String finalDay=format2.format(dt1);

        if(finalDay.equalsIgnoreCase("sunday")){
            check=0;
        }else  if(finalDay.equalsIgnoreCase("monday")){
            check=1;
        }else  if(finalDay.equalsIgnoreCase("tuesday")){
            check=2;
        }else  if(finalDay.equalsIgnoreCase("wednesday")){
            check=3;
        }else  if(finalDay.equalsIgnoreCase("thursday")){
            check=4;
        }else  if(finalDay.equalsIgnoreCase("friday")){
            check=5;
            if((monthDay[mont-1])>30) {
                changeRecycler = 1;
            }
        }else{
            check=6;
            if((monthDay[mont-1])>29) {
                changeRecycler = 1;
            }
        }
        int demo;
        int indexsize=7;
        if(changeRecycler==1){
            demo=6;
        }else {
            demo=5;
        }

        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(demo, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        if(check==0){
            updateday=7;
        }else {
            updateday=7-check;
        }
        int year=c.get(Calendar.YEAR);
        int totalday=0;
        if(year%4 == 0&&mont==2)
        {
            if( year%100 == 0 && year % 400 == 0) {
                    totalday=29;
            }else {
                totalday=(monthDay[mont-1]-1);
            }
        }else {
            totalday=(monthDay[mont-1]-1);
        }

        for(int i=0;i<31;i=updateday){
            CalenderModel model=new CalenderModel();
            int[] day=new int[7];
            int[] att=new int[7];
            k=0;
            for(int n=0;n<check;n++){
                att[k]=0;
                day[k++]=0;

            }
            if(check>0){
                check=0;
            }else {
                updateday=7;
            }

            int j;
            for(j=i;j<(i+updateday);j++){
                if(j>totalday){
                     att[k]=0;
                    for(int m=0;m<listAttend.size();m++){
                        if(listAttend.get(m).date.equalsIgnoreCase(String.format("%02d",(j+1)))){
                            if(listAttend.get(m).title.equalsIgnoreCase("Present")) {
                                att[k] = 1;
                            }else   if(listAttend.get(m).title.equalsIgnoreCase("Late") || listAttend.get(m).title.equalsIgnoreCase("Late with excuse")) {
                                att[k] =2;
                            }else {
                                att[k] =3;
                            }
                            break;
                        }else {
                            if(att[k]!=1) {
                                att[k] = 0;
                            }
                        }
                    }
                    day[k++]=(j-totalday);
                }else {
                    for(int m=0;m<listAttend.size();m++){
                        if(listAttend.get(m).date.equalsIgnoreCase(String.format("%02d",(j+1)))){
                            if(listAttend.get(m).title.equalsIgnoreCase("Present")) {
                                att[k] = 1;
                            }else   if(listAttend.get(m).title.equalsIgnoreCase("Late") || listAttend.get(m).title.equalsIgnoreCase("Late with excuse")) {
                                att[k] =2;
                            }else {
                                att[k] =3;
                            }
                            break;
                        }else {
                            if(att[k]!=1) {
                                att[k] = 0;
                            }
                        }
                    }
                    day[k++]=(j+1);
                }



            }
            updateday=j;
            model.setDay(day);
            model.setColorno(att);
            list.add(model);

        }
        adapter=new CalenderAdapter1(list,getActivity(),changeRecycler);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.invalidate();

    }
}
