package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.Timemodel;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class TimeTableAdapterAdapter1 extends RecyclerView.Adapter<TimeTableAdapterAdapter1.ViewHolder>{
    List<Timemodel> listData=new ArrayList<>();
    Context context;
    int size;
    float txt;
    public TimeTableAdapterAdapter1(List<Timemodel> list, Context mcontext, int item,float txtsize){
       listData=list;
        size=item;
       context=mcontext;
        txt=txtsize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView montxt,tustxt,wedtxt,thustxt,firtxt,sattxt;
      //  RelativeLayout rel1,rel2,rel3,rel4,rel5,rel6;

        private RecyclerView.LayoutManager layoutManager;
        public ViewHolder(View itemView) {
            super(itemView);
            montxt=(TextView)itemView.findViewById(R.id.montxt);
            tustxt=(TextView)itemView.findViewById(R.id.tustxt);
            wedtxt=(TextView)itemView.findViewById(R.id.wedtxt);
            thustxt=(TextView)itemView.findViewById(R.id.thustxt);
            firtxt=(TextView)itemView.findViewById(R.id.firtxt);
            sattxt=(TextView)itemView.findViewById(R.id.sattxt);

         /*   rel1=(RelativeLayout)itemView.findViewById(R.id.rel1);
            rel2=(RelativeLayout)itemView.findViewById(R.id.rel2);
            rel3=(RelativeLayout)itemView.findViewById(R.id.rel3);
            rel4=(RelativeLayout)itemView.findViewById(R.id.rel4);
            rel5=(RelativeLayout)itemView.findViewById(R.id.rel5);
            rel6=(RelativeLayout)itemView.findViewById(R.id.rel6);*/


        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.timeview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       // for(int j=0;j<listData.size();j++){

            if(!ApiUtils.isEmptyString(listData.get(position).getMob())) {
                holder.montxt.setText(generateCenterSpannableText(listData.get(position).getMob()));
            }
            if(!ApiUtils.isEmptyString(listData.get(position).getTus())) {
                holder.tustxt.setText(generateCenterSpannableText(listData.get(position).getTus()));
            }
            if(!ApiUtils.isEmptyString(listData.get(position).getWed())) {
                holder.wedtxt.setText(generateCenterSpannableText(listData.get(position).getWed()));
            }
            if(!ApiUtils.isEmptyString(listData.get(position).getThes())) {
                holder.thustxt.setText(generateCenterSpannableText(listData.get(position).getThes()));
            }
            if(!ApiUtils.isEmptyString(listData.get(position).getFir())) {
                holder.firtxt.setText(generateCenterSpannableText(listData.get(position).getFir()));
            }
            if(!ApiUtils.isEmptyString(listData.get(position).getSat())) {
                holder.sattxt.setText(generateCenterSpannableText(listData.get(position).getSat()));
            }
        //}


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private SpannableString generateCenterSpannableText(String ss){

        SpannableString s = new SpannableString(ss);
        s.setSpan(new RelativeSizeSpan(txt), 0, 18, 0);
        s.setSpan(new ForegroundColorSpan(Color.RED), 0,18, 0);

        return s;
    }
}
