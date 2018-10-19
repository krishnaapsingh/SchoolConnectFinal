package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.SubjectDataList;
import com.trieffects.ConnecttwoSchool.R;

import java.util.List;

/**
 * Created by shashikeshkumar on 30/03/18.
 */

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    List<SubjectDataList> data;
    Context mContex;
    public SubjectAdapter(List<SubjectDataList> lists,Context context){
        data=lists;
        mContex=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
     holder.subname.setText(data.get(position).name);
     if(!ApiUtils.isEmptyString(data.get(position).teacher_name)) {
         holder.teachname.setText(data.get(position).teacher_name);
     }
        if(!ApiUtils.isEmptyString(data.get(position).img_name)) {
            Glide.with(mContex).load(ApiUtils.ImageBaseUrl+data.get(position).img_name).error(R.drawable.subject).into(holder.sub_img);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sub_img;
        TextView subname,teachname;
        public ViewHolder(View itemView) {
            super(itemView);
            sub_img=itemView.findViewById(R.id.sub_img);
            subname=itemView.findViewById(R.id.subname);
            teachname=itemView.findViewById(R.id.teachname);
        }
    }
}
