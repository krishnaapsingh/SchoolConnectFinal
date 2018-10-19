package com.trieffects.ConnecttwoSchool.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.trieffects.ConnecttwoSchool.Adapter.CustomAdapter1;
import com.trieffects.ConnecttwoSchool.Adapter.HomeWorkAdapter;
import com.trieffects.ConnecttwoSchool.Adapter.NoticeboardAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Model.GetSubjectModel;
import com.trieffects.ConnecttwoSchool.Model.HomeWorkData;
import com.trieffects.ConnecttwoSchool.Model.HomeWorkModel;
import com.trieffects.ConnecttwoSchool.Model.SubjectDataList;
import com.trieffects.ConnecttwoSchool.Other.ImageFilePath;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;
import com.trieffects.ConnecttwoSchool.TecherFragment.TeacherHomeWork;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * Created by yarolegovich on 25.03.2017.
 */

@SuppressLint("ValidFragment")
public class HomeWorkDayFragment extends android.support.v4.app.Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener  {
    List<SubjectDataList> listsubject=new ArrayList<>();
    String subjectname;
    NoticeboardAdapter adapter;
    private RecyclerView RecyclerView;
    AlertDialog dialog;
  Button btn_Home;
  RelativeLayout main;
    private int mYear, mMonth, mDay, mHour, mMinute;
  private final int PICKFILE_REQUEST_CODE=1001;
  TextView file_name,subject_txt,des_txt;
  static TextView date_txt;
    LinearLayout date_pick;
    String selectedPath = "";
    public HomeWorkModel.HWData data;
    String val;
    File filePath;
    Spinner subject_spinner;
    private SwipeRefreshLayout swipeRefreshLayout;


    @SuppressLint("ValidFragment")
    public HomeWorkDayFragment(String dayValue){
        val=dayValue;
    }


    List<HomeWorkData> list=new ArrayList<>();

    List<ChildData> childlist;
    Spinner spinner_std;
    LinearLayout select_student_layout;
    TextView std_name;
    String classId,sectionId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.home_fragment, container, false);
        RecyclerView =view.findViewById(R.id.recycler_view);
        btn_Home=(Button)view.findViewById(R.id.btn_Home);
        main=(RelativeLayout)view.findViewById(R.id.main);
        std_name=(TextView)view.findViewById(R.id.std_name);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.setLayoutManager(mLayoutManager);

        dialog = new SpotsDialog(getActivity(),"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        spinner_std=(Spinner)view.findViewById(R.id.spinner_std);
        select_student_layout=(LinearLayout)view.findViewById(R.id.select_student_layout);
        select_student_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner_std.performClick();
            }
        });


       if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2")){
           btn_Home.setOnClickListener(this);
       }else {
           btn_Home.setVisibility(View.GONE);
       }

          if (PrefrencesUtils.getUserMode().equalsIgnoreCase("2")) {
              dialog.show();
              select_student_layout.setVisibility(View.GONE);
              homeTeachertWorkLoad(PrefrencesUtils.getUserId());
          } else {
              dialog.show();
              if (PrefrencesUtils.getUserMode().equalsIgnoreCase("1")) {
                  homeParentWorkLoad(PrefrencesUtils.getClassId(), PrefrencesUtils.getSectionId());
                  select_student_layout.setVisibility(View.GONE);
              } else {

                  if(PrefrencesUtils.getUserMode().equalsIgnoreCase("3")) {
                      childData();
                      spinner_std.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                          @Override
                          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                              //dialog.show();
                              std_name.setText(adapterView.getSelectedItem().toString());
                              dialog.show();
                              classId=childlist.get(i).class_id;
                              sectionId=childlist.get(i).section_id;
                              homeParentWorkLoad(childlist.get(i).class_id, childlist.get(i).section_id);

                          }

                          @Override
                          public void onNothingSelected(AdapterView<?> adapterView) {

                          }
                      });
                  }else {
                      select_student_layout.setVisibility(View.GONE);
                      dialog.show();
                      homeParentWorkLoad(PrefrencesUtils.getClassId(), PrefrencesUtils.getSectionId());

                  }
              }
          }



        return view;
    }

    @Override
    public void onRefresh() {
        if (PrefrencesUtils.getUserMode().equalsIgnoreCase("2")) {
            dialog.show();
            homeTeachertWorkLoad(PrefrencesUtils.getUserId());
        } else {
            if (PrefrencesUtils.getUserMode().equalsIgnoreCase("1")) {
                dialog.show();
                homeParentWorkLoad(PrefrencesUtils.getClassId(), PrefrencesUtils.getSectionId());
            } else {
                if (PrefrencesUtils.getUserMode().equalsIgnoreCase("3")) {
                    dialog.show();
                    homeParentWorkLoad(classId, sectionId);
                }else {
                    dialog.show();
                    homeParentWorkLoad(PrefrencesUtils.getClassId(), PrefrencesUtils.getSectionId());
                }
            }
        }
    }


    public void childData(){
        if(PrefrencesUtils.getChildInfo()!=null) {
            childlist = PrefrencesUtils.getChildInfo();
            ArrayAdapter<ChildData> dataAdapter = new ArrayAdapter<ChildData>(getActivity(), android.R.layout.simple_spinner_item, childlist);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_std.setAdapter(dataAdapter);
            if (childlist.size()==1) {
                dialog.show();
                classId=childlist.get(0).class_id;
                sectionId=childlist.get(0).section_id;
                homeParentWorkLoad(childlist.get(0).class_id, childlist.get(0).section_id);
                select_student_layout.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Home:
                main.setAlpha((float) 0.3);
                showDialog();
                break;
        }
    }




    public void showDialog(){
       if(listsubject.size()==0) {
           dialog.show();
           loadSubject(TeacherHomeWork.classname, TeacherHomeWork.sectionName);
        }

     final Dialog mdialog = new Dialog(getActivity());
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setCancelable(false);
        mdialog.setContentView(R.layout.dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mdialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
         Button dialogButton = (Button) mdialog.findViewById(R.id.save_homebtn);
         Button close_pop= (Button) mdialog.findViewById(R.id.close_pop);
        file_name=(TextView)mdialog.findViewById(R.id.file_name);
        subject_spinner=(Spinner)mdialog.findViewById(R.id.subject_spinner);
        subject_txt=(TextView)mdialog.findViewById(R.id.subject_txt);
        date_txt=(TextView)mdialog.findViewById(R.id.date_txt);
        des_txt=(TextView)mdialog.findViewById(R.id.des_txt);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ApiUtils.isEmptyString(subjectname)){
                        Toast.makeText(getActivity(),"PLease select subject name",Toast.LENGTH_SHORT).show();
                    }else if(date_txt.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(getActivity(),"PLease select Home work date",Toast.LENGTH_SHORT).show();
                    }else if(des_txt.getText().toString().trim().equalsIgnoreCase("")&&filePath==null){
                        Toast.makeText(getActivity(),"PLease give home work",Toast.LENGTH_SHORT).show();
                    }else {
                        dialog.show();
                        UploadHomeWork(filePath, PrefrencesUtils.getUserId(), subjectname, TeacherHomeWork.classname, TeacherHomeWork.sectionName, date_txt.getText().toString().trim(), des_txt.getText().toString().trim());
                        main.setAlpha((float) 1);
                        mdialog.dismiss();
                    }
                }
            });

        close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectname="";
                main.setAlpha((float) 1);
                mdialog.dismiss();
            }
        });

        ImageView attachment=(ImageView)mdialog.findViewById(R.id.attch_image);
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });

        final LinearLayout subject_lay=(LinearLayout)mdialog.findViewById(R.id.subject_lay);
        subject_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listsubject.size()>0) {
                    CustomAdapter1 adapter = new CustomAdapter1(getActivity(), R.layout.listitems_layout, R.id.title_txt, listsubject);
                    subject_spinner.setAdapter(adapter);
                    subject_spinner.performClick();
                }
            }
        });


       subject_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // if(i>0){
                    subjectname=listsubject.get(i).name;
                subjectname=listsubject.get(i).subject_id;
                  subject_txt.setText(listsubject.get(i).name);

              //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        date_pick=(LinearLayout)mdialog.findViewById(R.id.date_pick);
        date_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogfragment = new DatePickerDialogTheme();
                dialogfragment.show(getActivity().getFragmentManager(), "Theme");
            }
        });
            mdialog.show();

        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == PICKFILE_REQUEST_CODE)
            {
              //  Uri selectedImageUri = data.getData();
                String realPath;
                 realPath = ImageFilePath.getPath(getActivity(), data.getData());

               filePath = new File(realPath);
                file_name.setText(realPath);
            }

        }
    }





    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }



   public void homeParentWorkLoad(String class_id,String section_id){
       final ApiInterface mApiService=ApiUtils.getInterfaceService();
       Call<HomeWorkModel> mService=mApiService.loadHomeWork(class_id,section_id);
       mService.enqueue(new Callback<HomeWorkModel>() {
           @Override
           public void onResponse(Call<HomeWorkModel> call, Response<HomeWorkModel> response) {
               HomeWorkModel object=response.body();
               list.clear();
               if(object.status){
                   data=object.data;
                   if(data.Monday!=null&&val.equalsIgnoreCase("0")) {
                       list = data.Monday;
                   }else if(data.Tuesday!=null&&val.equalsIgnoreCase("1")) {
                       list = data.Tuesday;
                   } if(data.Wednesday!=null&&val.equalsIgnoreCase("2")) {
                       list = data.Wednesday;
                   }else if(data.Thursday!=null&&val.equalsIgnoreCase("3")) {
                       list = data.Thursday;
                   }else if(data.Friday!=null&&val.equalsIgnoreCase("4")) {
                       list = data.Friday;
                   }else if(data.Saturday!=null&&val.equalsIgnoreCase("5")) {
                       list = data.Saturday;
                   }
                   HomeWorkAdapter adapter=new HomeWorkAdapter(getActivity(),list);
                   RecyclerView.setAdapter(adapter);
                   adapter.notifyDataSetChanged();
               }else {
                   HomeWorkAdapter adapter=new HomeWorkAdapter(getActivity(),list);
                   RecyclerView.setAdapter(adapter);
                   Toast.makeText(getActivity(),"No Home work",Toast.LENGTH_SHORT).show();
               }
               dialog.dismiss();
               swipeRefreshLayout.setRefreshing(false);
           }

           @Override
           public void onFailure(Call<HomeWorkModel> call, Throwable t) {
               Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
               dialog.dismiss();
               swipeRefreshLayout.setRefreshing(false);
           }
       });

   }

    public void homeTeachertWorkLoad(String teacher_id){
        final ApiInterface mApiService=ApiUtils.getInterfaceService();
        Call<HomeWorkModel> mService=mApiService.loadHomeWorkTeacher(teacher_id,TeacherHomeWork.classname, TeacherHomeWork.sectionName);
        mService.enqueue(new Callback<HomeWorkModel>() {
            @Override
            public void onResponse(Call<HomeWorkModel> call, Response<HomeWorkModel> response) {
                HomeWorkModel object=response.body();
                list.clear();
                if(object.status){
                    data=object.data;
                    if(data.Monday!=null&&val.equalsIgnoreCase("0")) {
                        list = data.Monday;
                    }else if(data.Tuesday!=null&&val.equalsIgnoreCase("1")) {
                        list = data.Tuesday;
                    } if(data.Wednesday!=null&&val.equalsIgnoreCase("2")) {
                        list = data.Wednesday;
                    }else if(data.Thursday!=null&&val.equalsIgnoreCase("3")) {
                        list = data.Thursday;
                    }else if(data.Friday!=null&&val.equalsIgnoreCase("4")) {
                        list = data.Friday;
                    }else if(data.Saturday!=null&&val.equalsIgnoreCase("5")) {
                        list = data.Saturday;
                    }
                    HomeWorkAdapter adapter=new HomeWorkAdapter(getActivity(),list);
                    RecyclerView.setAdapter(adapter);
                }else {
                    HomeWorkAdapter adapter=new HomeWorkAdapter(getActivity(),list);
                    RecyclerView.setAdapter(adapter);
                    Toast.makeText(getActivity(),"No Home work",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<HomeWorkModel> call, Throwable t) {
                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    public void UploadHomeWork(File file,String teacher_id,String subject_id,String class_id,String section_id,String hw_date,String hw_desc) {
        RequestParams data = new RequestParams();
        data.put("teacher_id",teacher_id);
        data.put("subject_id",subject_id);
        data.put("class_id",class_id);
        data.put("section_id",section_id);
        data.put("hw_date", hw_date);
        data.put("hw_desc",hw_desc);
        try {
            data.put("hw_file", file);
        } catch (FileNotFoundException e) {
            Log.d("", e.toString());
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("https://www.connect2school.com/admin/api/example/give_homework", data, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object=new JSONObject(responseString);
                    if(object.getBoolean("status")==true){
                        Toast.makeText(getActivity(),object.getString("message"),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(),object.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }

    public void loadSubject(String classid,String section_id){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<GetSubjectModel> mService=mApiService.getSubjectList(classid,section_id);
        mService.enqueue(new Callback<GetSubjectModel>() {
            @Override
            public void onResponse(Call<GetSubjectModel> call, Response<GetSubjectModel> response) {
                GetSubjectModel object=response.body();
                if(object.status){
                //    SubjectDataList model=new  SubjectDataList();
                 ///   model.name="Select subject";
                //    model.id="0";
                 //   listsubject.add(model);
                     listsubject.addAll(object.data);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetSubjectModel> call, Throwable t) {
                dialog.dismiss();
            }
        });
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
            String value=(month+1)+"/" +day+"/"+year;
            date_txt.setText(value);

        }
    }

}
