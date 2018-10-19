package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Model.ExamData;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class ExamLIstAdapter1 extends RecyclerView.Adapter<ExamLIstAdapter1.ViewHolder>{
    List<ExamData> listData=new ArrayList<>();
    Context mContext;
    int itemData;
    public ExamLIstAdapter1(List<ExamData> list, Context context, int item){
        listData=list;
        mContext=context;
        itemData=item;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button heading;
        TextView textnotice;
        RelativeLayout calendar_layout;
        public ViewHolder(View itemView) {
            super(itemView);
            heading=(Button)itemView.findViewById(R.id.heading);
            textnotice=(TextView)itemView.findViewById(R.id.textnotice);
            calendar_layout=(RelativeLayout)itemView.findViewById(R.id.calendar_layout);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.examlist_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
      holder.textnotice.setText(Html.fromHtml(listData.get(position).name));
      holder.calendar_layout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

          }
      });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
