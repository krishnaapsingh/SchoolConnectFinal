package com.trieffects.ConnecttwoSchool.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Activity.MessageInboxActivity;
import com.trieffects.ConnecttwoSchool.Adapter.DropDownListAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.LeaveApplicationModel;
import com.trieffects.ConnecttwoSchool.Model.MessageModel;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageFragment extends Fragment {

    ArrayList<MessageModel.MessageData> list=new ArrayList<>();
    LinearLayout select_student_layout;
    TextView std_name;
    AlertDialog dialog;
    EditText msg_txt;
    Button send_btn;
    ImageView inbox_btn;
    public static boolean[] checkSelected;
    public static List<String> checklist=new ArrayList<>();
    String teacherjson;
    CheckBox checkbox_admin;
    String adminvalue="0";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.message_fragment,container,false);
        checklist.clear();
        inbox_btn=(ImageView)view.findViewById(R.id.inbox_btn);
        msg_txt=(EditText)view.findViewById(R.id.msg_txt);
        send_btn=(Button)view.findViewById(R.id.send_btn);
        checkbox_admin=(CheckBox)view.findViewById(R.id.checkbox_admin);
        std_name=(TextView)view.findViewById(R.id.std_name);
        dialog = new SpotsDialog(getActivity(),"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        select_student_layout=(LinearLayout)view.findViewById(R.id.select_student_layout);



        select_student_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ApiUtils.isEmptyString(teacherjson)){
                    Toast.makeText(getActivity(),"Please select teacher name you want send sms",Toast.LENGTH_SHORT).show();
                }else if(msg_txt.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Enter SMS description ",Toast.LENGTH_SHORT).show();
                }else {
                    dialog.show();
                    sendMessage(PrefrencesUtils.getUserId(),msg_txt.getText().toString().trim(),teacherjson,adminvalue);
                }
            }
        });

        inbox_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MessageInboxActivity.class);
                getActivity().startActivity(intent);
            }
        });


        checkbox_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adminvalue.equalsIgnoreCase("0")){
                    adminvalue="1";
                    checkbox_admin.setChecked(true);
                }else {
                    adminvalue="0";
                    checkbox_admin.setChecked(false);
                }
            }
        });

        dialog.show();
        loadParentList();
        return view;
    }


    public void sendMessage(String userId,String msg,String teachjson,String val){
        final ApiInterface mApiService=ApiUtils.getInterfaceService();
        Call<LeaveApplicationModel> mService=mApiService.sendTeacherMessage(userId,msg,teachjson,val);
        mService.enqueue(new Callback<LeaveApplicationModel>() {
            @Override
            public void onResponse(Call<LeaveApplicationModel> call, Response<LeaveApplicationModel> response) {
                LeaveApplicationModel object=response.body();
                if(object.status){
                    msg_txt.setText("");
                    checklist.clear();
                    std_name.setText("");
                   teacherjson="";
                    for(int i=0;i<checkSelected.length;i++){
                        checkSelected[i]=false;
                    }
                    Toast.makeText(getActivity(),"Message send successfully",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),"Something wrong",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<LeaveApplicationModel> call, Throwable t) {
                Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    public void showDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.multipledata_select);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        Button dialogButton = (Button) dialog.findViewById(R.id.save_homebtn);
        final ListView listview = (ListView)dialog.findViewById(R.id.dropDownList);
        DropDownListAdapter adapter = new DropDownListAdapter(list,getActivity());
        listview.setAdapter(adapter);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTeacher();
                dialog.dismiss();
            }
        });



        dialog.show();

    }


    public void loadParentList(){
        final ApiInterface mApiService=ApiUtils.getInterfaceService();
        Call<MessageModel> mService=mApiService.getTeacherName();
        mService.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                MessageModel object=response.body();
                if(object.status){
                    list=object.data;
                    checkSelected = new boolean[list.size()];
                    for (int i = 0; i < checkSelected.length; i++) {
                        checkSelected[i] = false;
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public String selectTeacher(){
        JSONArray array=new JSONArray();
        String val="";
        try {
            for(int i=0;i<list.size();i++){
                if(checkSelected[i]){
                    JSONObject object=new JSONObject();
                    val=val+list.get(i).name+", ";
                    object.put("teacher_id",list.get(i).id);
                    array.put(object);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!ApiUtils.isEmptyString(val)) {
            std_name.setText(val);
        }else {
            std_name.setText("");
        }
        if(array.length()>0){
            teacherjson=array.toString();
        }else {
            teacherjson="";
        }
        return teacherjson;
    }
}
