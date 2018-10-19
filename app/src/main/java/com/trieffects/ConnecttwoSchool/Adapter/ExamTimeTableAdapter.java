package com.trieffects.ConnecttwoSchool.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Model.ExamData;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class ExamTimeTableAdapter extends RecyclerView.Adapter<ExamTimeTableAdapter.ViewHolder>{
    List<ExamData> listData=new ArrayList<>();

    float txt;
    public ExamTimeTableAdapter(List<ExamData> list,float txtsize){
       listData=list;
        txt=txtsize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView datetxt,subtxt,timetxt;
        private RecyclerView.LayoutManager layoutManager;
        public ViewHolder(View itemView) {
            super(itemView);
            datetxt=(TextView)itemView.findViewById(R.id.datetxt);
            subtxt=(TextView)itemView.findViewById(R.id.subtxt);
            timetxt=(TextView)itemView.findViewById(R.id.timetxt);


        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.exam_time_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.datetxt.setText(listData.get(position).date_of_exam);
        holder.subtxt.setText(listData.get(position).name+"\n("+listData.get(position).type+")");
        holder.timetxt.setText(generateCenterSpannableText(listData.get(position).start_to+"-"+listData.get(position).end_from+"\nR-"+listData.get(position).room_no));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private SpannableString generateCenterSpannableText(String ss){

        SpannableString s = new SpannableString(ss);
        s.setSpan(new RelativeSizeSpan(txt), 0, 17, 0);
        s.setSpan(new ForegroundColorSpan(Color.RED), 0,17, 0);

        return s;
    }
}
