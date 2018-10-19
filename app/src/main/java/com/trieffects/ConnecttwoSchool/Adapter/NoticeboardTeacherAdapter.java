package com.trieffects.ConnecttwoSchool.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.LeaveApplicationModel;
import com.trieffects.ConnecttwoSchool.Model.LeaveModelData;
import com.trieffects.ConnecttwoSchool.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class NoticeboardTeacherAdapter extends RecyclerView.Adapter<NoticeboardTeacherAdapter.ViewHolder>{

    List<LeaveModelData> listdata;
    private View popView;
    AlertDialog dialog;
    private PopupWindowHelper popupWindowHelper;
    Context mContext;

    public NoticeboardTeacherAdapter(Context context,List<LeaveModelData> list){
        listdata=list;
        mContext=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textmessage,textnotice,day_txt,time_txt;
        RelativeLayout calendar_layout;
        public ViewHolder(View itemView) {
            super(itemView);
            textnotice=(TextView)itemView.findViewById(R.id.textnotice);
            textmessage=(TextView)itemView.findViewById(R.id.textmessage);
            textmessage.setMinLines(2);
            textmessage.setMaxLines(2);
            day_txt=(TextView)itemView.findViewById(R.id.day_txt);
            time_txt=(TextView)itemView.findViewById(R.id.time_txt);
            calendar_layout=(RelativeLayout)itemView.findViewById(R.id.calendar_layout);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.message_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
         holder.textmessage.setText(listdata.get(position).reason);
         holder.textnotice.setText(listdata.get(position).student_name);
        String[] d=listdata.get(position).created_at.split(" ");
        String input_date=d[0];
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2=new SimpleDateFormat("EEEE");
        holder.day_txt.setText(format2.format(dt1));
        holder.time_txt.setText(d[1]);
         holder.calendar_layout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 showHistor(mContext,listdata.get(position).reason,listdata.get(position).id,v,position,listdata.get(position).leave_from,listdata.get(position).leave_till);
             }
         });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    public void showHistor(Context context, String msg,final String leaveid,View v, final int pos,String str_date,String e_date){
        popView = LayoutInflater.from(context).inflate(R.layout.pop_leave, null);
        popupWindowHelper = new PopupWindowHelper(popView);
        Button btn_approve=(Button)popView.findViewById(R.id.btn_approve);
        TextView msg_txt=(TextView)popView.findViewById(R.id.msg_txt);
        TextView start_date=(TextView)popView.findViewById(R.id.start_date);
        start_date.setText(str_date);
        TextView end_date=(TextView)popView.findViewById(R.id.end_date);
        end_date.setText(e_date);
        msg_txt.setText(msg);
        Button btn_unapprove=(Button)popView.findViewById(R.id.btn_unapprove);
       ImageView back_img=(ImageView)popView.findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowHelper.dismiss();
            }
        });
        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UproveApplication(leaveid,"1",pos);
            }
        });

        btn_unapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UproveApplication(leaveid,"2",pos);
            }
        });

        popupWindowHelper.showFromBottom(v);
    }

    public void UproveApplication(String lv_id, String std_id, final int pos){
       dialog = new SpotsDialog(mContext,"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<LeaveApplicationModel> mService=mApiService.approveLeave(lv_id,std_id);
        mService.enqueue(new Callback<LeaveApplicationModel>() {
            @Override
            public void onResponse(Call<LeaveApplicationModel> call, Response<LeaveApplicationModel> response) {
                LeaveApplicationModel object=response.body();
                if(object.status){
                   listdata.remove(pos);
                   NoticeboardTeacherAdapter.this.notifyDataSetChanged();
                }else {
                    Toast.makeText(mContext,"Please check your internet connection ",Toast.LENGTH_SHORT).show();
                }
                popupWindowHelper.dismiss();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<LeaveApplicationModel> call, Throwable t) {
                dialog.dismiss();
                popupWindowHelper.dismiss();
                Toast.makeText(mContext,"Please check your internet connection ",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
