package com.trieffects.ConnecttwoSchool.TecherFragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.trieffects.ConnecttwoSchool.Adapter.CustomAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.GetClassModel;
import com.trieffects.ConnecttwoSchool.Model.GetSection;
import com.trieffects.ConnecttwoSchool.Model.SectionAndClassData;
import com.trieffects.ConnecttwoSchool.Other.ImageFilePath;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class TeacherDigitalFragment extends Fragment implements View.OnClickListener{
    AlertDialog dialog;
    File filePath;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    TextView file_name,des_txt;
    private final int PICKFILE_REQUEST_CODE=1001;
    LinearLayout class_rel,section_rel;
    Spinner section_spinner,class_spinner;
   List<SectionAndClassData> listSection=new ArrayList<>();
    List<SectionAndClassData> listClass=new ArrayList<>();
     TextView txt_section,txt_class;
   public static String classname,sectionName;
   Button save_homebtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.teacher_digital_view,container,false);

        txt_section=(TextView)view.findViewById(R.id.txt_section);
        txt_class=(TextView)view.findViewById(R.id.txt_class);

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

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        file_name=(TextView)view.findViewById(R.id.file_name);
        des_txt=(TextView)view.findViewById(R.id.des_txt);
        ImageView attachment=(ImageView)view.findViewById(R.id.attch_image);
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED&&
                        ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        android.support.v7.app.AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }

                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, PICKFILE_REQUEST_CODE);
                }

            }
        });


        save_homebtn=(Button)view.findViewById(R.id.save_homebtn);
        save_homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()==1) {
                    dialog.show();
                    loadDigitaldata(PrefrencesUtils.getUserId(), classname, sectionName, filePath,des_txt.getText().toString().trim());
                }
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
        if(ApiUtils.isEmptyString(txt_section.getText().toString().trim())){
            Toast.makeText(getActivity(),"Please Select class name",Toast.LENGTH_SHORT).show();
        }else if(ApiUtils.isEmptyString(txt_class.getText().toString().trim())){
            Toast.makeText(getActivity(),"Please Select class name",Toast.LENGTH_SHORT).show();
        }else  if(ApiUtils.isEmptyString(file_name.getText().toString().trim())){
            Toast.makeText(getActivity(),"Please Select select file",Toast.LENGTH_SHORT).show();
        }else if(des_txt.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(),"Please enter file description ",Toast.LENGTH_SHORT).show();
        } else {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == PICKFILE_REQUEST_CODE)
            {
                //  Uri selectedImageUri = data.getData();
                String realPath;
                realPath = ImageFilePath.getPath(getActivity(), data.getData());
                 if(realPath!=null) {
                     filePath = new File(realPath);
                     file_name.setText(realPath);
                 }
            }

        }
    }


    public void loadDigitaldata(String teacher_id,String class_id,String section_id,File file,String details){
            RequestParams data = new RequestParams();
            data.put("teacher_id",teacher_id);
            data.put("class_id",class_id);
            data.put("section_id",section_id);
            data.put("details",details);

          try {
                data.put("digital_file", file);
            } catch (FileNotFoundException e) {
                Log.d("", e.toString());
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.post("https://www.connect2school.com/admin/api/example/digital_file", data, new TextHttpResponseHandler() {

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
                            filePath=null;
                            file_name.setText("");
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

}
