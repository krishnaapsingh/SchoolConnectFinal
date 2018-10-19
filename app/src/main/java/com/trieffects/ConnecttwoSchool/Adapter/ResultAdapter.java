package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ResultData1;
import com.trieffects.ConnecttwoSchool.R;

import java.util.List;

/**
 * Created by Trieffects on 15-Nov-17.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    List<ResultData1> list;
    Context mContext;
    public ResultAdapter(Context context, List<ResultData1> data){
    list=data;
    mContext=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sno_txt,sub_txt,totalmark_txt,th_txt,ph_txt,fullmark_txt,grade_txt;
        public ViewHolder(View itemView) {
            super(itemView);
            sno_txt=(TextView)itemView.findViewById(R.id.sno_txt);
            sub_txt=(TextView)itemView.findViewById(R.id.sub_txt);
            totalmark_txt=(TextView)itemView.findViewById(R.id.totalmark_txt);
            th_txt=(TextView)itemView.findViewById(R.id.th_txt);

            ph_txt=(TextView)itemView.findViewById(R.id.ph_txt);
            fullmark_txt=(TextView)itemView.findViewById(R.id.fullmark_txt);
            grade_txt=(TextView)itemView.findViewById(R.id.grade_txt);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.result_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         holder.sno_txt.setText((position+1)+"");
         holder.sub_txt.setText(list.get(position).exam_name);
         holder.fullmark_txt.setText(list.get(position).full_marks);
         if(!ApiUtils.isEmptyString(list.get(position).get_marks)) {
             holder.th_txt.setText("" + (int) Float.parseFloat(list.get(position).get_marks));
         }

         holder.ph_txt.setText("-");
        if(!ApiUtils.isEmptyString(list.get(position).get_marks)) {
            holder.totalmark_txt.setText("" + (int) Float.parseFloat(list.get(position).get_marks));
        }
         int value=-1;
        if(!ApiUtils.isEmptyString(list.get(position).get_marks)) {
            value=((int) Float.parseFloat(list.get(position).get_marks));
        }
         if(value>=90){
             holder.grade_txt.setText("A+");
         }else  if(value<90&&value>=75){
             holder.grade_txt.setText("A");
         }else  if(value<75&&value>=60){
             holder.grade_txt.setText("B");
         }else if(value<60&&value>=34){
             holder.grade_txt.setText("c");
         }else if(value>=0&&value<=34) {
             holder.grade_txt.setText("F");
         }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
