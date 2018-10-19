package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.R;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder>{

    String[] subName;

    public TimeTableAdapter(Context context,String[] subname){
        subName=subname;
        }

public class ViewHolder extends RecyclerView.ViewHolder {

        TextView datetxt;
    public ViewHolder(View itemView) {
        super(itemView);
        datetxt=(TextView)itemView.findViewById(R.id.datetxt);
    }
}
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.dateview1, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.datetxt.setTextSize(16);
        holder.datetxt.setText(subName[position]);


    }

    @Override
    public int getItemCount() {
        return subName.length;
    }
}
