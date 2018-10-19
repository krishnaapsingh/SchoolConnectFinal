package com.trieffects.ConnecttwoSchool.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Activity.MessageInboxActivity;
import com.trieffects.ConnecttwoSchool.Model.MessageModel;
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

public class MessageInboxAdapter extends RecyclerView.Adapter<MessageInboxAdapter.ViewHolder>{
    List<MessageModel.MessageData> listData=new ArrayList<>();
    MessageInboxActivity mActivity;
    public MessageInboxAdapter(List<MessageModel.MessageData> list, MessageInboxActivity activity){
        listData=list;
        mActivity=activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textnotice,textmessage,day_txt,time_txt;
        RelativeLayout calendar_layout;
        public ViewHolder(View itemView) {
            super(itemView);

            textnotice=(TextView)itemView.findViewById(R.id.textnotice);
            textmessage=(TextView)itemView.findViewById(R.id.textmessage);
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
    public void onBindViewHolder(ViewHolder holder,final int position) {
      holder.textnotice.setText(listData.get(0).name);
      holder.textmessage.setText(Html.fromHtml(listData.get(position).message));
      holder.calendar_layout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              mActivity.showPopup(listData.get(position).message);
          }
      });

        String[] d=listData.get(position).created_at.split(" ");
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

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
