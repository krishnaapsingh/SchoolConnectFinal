package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Model.LeaveModelData;
import com.trieffects.ConnecttwoSchool.R;

import java.util.List;


/**
 * Created by Trieffects on 16-Sep-17.
 */

public class LeaveInformationAdapter extends RecyclerView.Adapter<LeaveInformationAdapter.StudentHolder> {

//android:textColor="@color/yellowcolor"
Context mContext;
    List<LeaveModelData> list;
    public LeaveInformationAdapter(Context context,List<LeaveModelData> data){
        mContext=context;
        list=data;
    }


    public class StudentHolder extends RecyclerView.ViewHolder {
       TextView startdate_txt,end_date_txt,status_txt,discription_txt;
        public StudentHolder(View itemView) {
            super(itemView);
            startdate_txt=(TextView)itemView.findViewById(R.id.startdate_txt);
            end_date_txt=(TextView)itemView.findViewById(R.id.end_date_txt);
            status_txt=(TextView)itemView.findViewById(R.id.status_txt);
            discription_txt=(TextView)itemView.findViewById(R.id.discription_txt);

        }
    }

    @Override
    public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaveviewadapter, parent, false);
        return new StudentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StudentHolder holder, int position) {
         holder.startdate_txt.setText(list.get(position).leave_from);
        holder.end_date_txt.setText(list.get(position).leave_till);
        if(list.get(position).status.equalsIgnoreCase("1")){
            holder.status_txt.setText("Approved");
            holder.status_txt.setTextColor(Color.GREEN);

        }else {
            holder.status_txt.setText("Unapproved");
            holder.status_txt.setTextColor(Color.WHITE);
        }

        holder.discription_txt.setText(list.get(position).reason);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
