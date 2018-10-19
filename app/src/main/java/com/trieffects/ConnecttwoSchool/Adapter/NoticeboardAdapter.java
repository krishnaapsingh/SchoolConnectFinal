package com.trieffects.ConnecttwoSchool.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Fragment.NoticeboardFragment;
import com.trieffects.ConnecttwoSchool.Model.NotificationData;
import com.trieffects.ConnecttwoSchool.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class NoticeboardAdapter extends RecyclerView.Adapter<NoticeboardAdapter.ViewHolder>{
    List<NotificationData> listData=new ArrayList<>();
    NoticeboardFragment fragment;
    public NoticeboardAdapter( List<NotificationData> list,NoticeboardFragment context){
        listData=list;
        fragment=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button heading;
        TextView loadmorebtn;
        RelativeLayout calendar_layout;
        TextView textnotice,day_txt,time_txt,datetxt;
        public ViewHolder(View itemView) {
            super(itemView);
            calendar_layout=itemView.findViewById(R.id.calendar_layout);
            heading=(Button)itemView.findViewById(R.id.heading);
            loadmorebtn=itemView.findViewById(R.id.loadmorebtn);
            day_txt=(TextView)itemView.findViewById(R.id.day_txt);
            textnotice=(TextView)itemView.findViewById(R.id.textnotice);
            time_txt=(TextView)itemView.findViewById(R.id.time_txt);
            datetxt=(TextView)itemView.findViewById(R.id.datetxt);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.noticeboard_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(listData.size()==position){
            holder.calendar_layout.setVisibility(View.GONE);
            holder.datetxt.setVisibility(View.GONE);
            holder.loadmorebtn.setVisibility(View.VISIBLE);
            holder.loadmorebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  fragment.loadmore();
                }
            });
        }else {
            holder.calendar_layout.setVisibility(View.VISIBLE);
            holder.datetxt.setVisibility(View.VISIBLE);
            holder.heading.setText(listData.get(position).title);
            holder.textnotice.setText(Html.fromHtml(listData.get(position).message));
            String[] d=listData.get(position).created_at.split(" ");
            String input_date=d[0];
            holder.datetxt.setText(input_date);
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
            holder.loadmorebtn.setVisibility(getItemCount());
        }
    }

    @Override
    public int getItemCount() {
        return listData.size()+1;
    }
}
