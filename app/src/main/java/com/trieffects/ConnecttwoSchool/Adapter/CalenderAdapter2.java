package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.R;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class CalenderAdapter2 extends RecyclerView.Adapter<CalenderAdapter2.ViewHolder>{

    int[] d;
    int[] c;
    int pos=0;
    Context mcontext;
        public CalenderAdapter2(int[] day,int col,int[] colorno,Context context){
            d=new int[day.length];
            d=day;
            c=colorno;
            pos=col;
            mcontext=context;
        }

public class ViewHolder extends RecyclerView.ViewHolder {
  TextView datetxt;
  RelativeLayout main;
    public ViewHolder(View itemView) {
        super(itemView);
        datetxt=(TextView)itemView.findViewById(R.id.datetxt);
        main=(RelativeLayout)itemView.findViewById(R.id.main);
    }
}
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.dateview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position==0&&d[position]!=0){
            holder.main.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.lightgray1));
        }
        if(d[position]!=0) {
            if((pos==4||pos==5)&&d[position]>=1&&d[position]<=7){
                holder.datetxt.setHint(d[position] + "");
            }else {
                holder.datetxt.setText(d[position] + "");
            }

        }



        if(c[position]!=0){
            if(c[position]==1){
                if(!((pos==4||pos==5)&&d[position]>=1&&d[position]<=7)) {
                  holder.main.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.holidaycolor));
                }
            }else  if(c[position]==2){
                if(!((pos==4||pos==5)&&d[position]>=1&&d[position]<=7)) {
                    holder.main.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.leavecolor));
                }
            }else  if(c[position]==3){
                if(!((pos==4||pos==5)&&d[position]>=1&&d[position]<=7)) {
                    holder.main.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.absentcolor));
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return 7;
    }
}
