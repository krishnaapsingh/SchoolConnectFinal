package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Api;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.HolidayData;
import com.trieffects.ConnecttwoSchool.R;

import java.util.List;

/**
 * Created by shashikeshkumar on 31/03/18.
 */

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.ViewHolder> {
    List<HolidayData> list;
    Context mContext;
    public HolidayAdapter(List<HolidayData> data, Context context){
        list=data;
        mContext=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.holidayview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      holder.fes_name.setText(list.get(position).holiday_name);
        holder.fes_description.setText(list.get(position).details);
        holder.startdate_txt.setText(list.get(position).start_date);
        holder.end_date_txt.setText(list.get(position).end_date);
        holder.totalday_txt.setText(list.get(position).total_days);
        if(!ApiUtils.isEmptyString(list.get(position).img_name)){
            Glide.with(mContext).load(ApiUtils.ImageBaseUrl+list.get(position).img_name).error(R.drawable.login_logo).into(holder.fes_img);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fes_img;
        TextView fes_name,fes_description,startdate_txt,end_date_txt,totalday_txt;
        public ViewHolder(View itemView) {
            super(itemView);
            fes_img=itemView.findViewById(R.id.fes_img);
            fes_name=itemView.findViewById(R.id.fes_name);
            fes_description=itemView.findViewById(R.id.fes_description);
            startdate_txt=itemView.findViewById(R.id.startdate_txt);
            end_date_txt=itemView.findViewById(R.id.end_date_txt);
            totalday_txt=itemView.findViewById(R.id.totalday_txt);
        }
    }
}
