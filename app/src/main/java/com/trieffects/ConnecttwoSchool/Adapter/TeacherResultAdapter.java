package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ResultModelFull;
import com.trieffects.ConnecttwoSchool.Other.CountTotalMark;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Trieffects on 15-Nov-17.
 */

public class TeacherResultAdapter extends RecyclerView.Adapter<TeacherResultAdapter.ViewHolder> {
    List<ResultModelFull> list;
    private List<ResultModelFull> arraylist;
    ArrayList<ResultModelFull> savelist;

    Context mContext;
    public TeacherResultAdapter(Context context, List<ResultModelFull> data){
    list=data;
    mContext=context;
   this.arraylist = new ArrayList<>();
   this.arraylist.addAll(data);
   this.savelist = new ArrayList<>();
        this.savelist.addAll(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      TextView std_name,std_roll,std_dob,father_name,total_txt,div_txt;
        RecyclerView recycler_view_result;
        LinearLayout marksheet_lay;
        CircularImageView user_image;
        CardView card_std;
        public ViewHolder(View itemView) {
            super(itemView);
            std_name=(TextView)itemView.findViewById(R.id.std_name);
            std_roll=(TextView)itemView.findViewById(R.id.std_roll);
            std_dob=(TextView)itemView.findViewById(R.id.std_dob);
            total_txt=(TextView)itemView.findViewById(R.id.obtain_mark);
            div_txt=(TextView)itemView.findViewById(R.id.div_txt);
            father_name=(TextView)itemView.findViewById(R.id.father_name);
            card_std=(CardView)itemView.findViewById(R.id.card_std);
            user_image=(CircularImageView)itemView.findViewById(R.id.user_image);
            marksheet_lay=(LinearLayout)itemView.findViewById(R.id.marksheet_lay);
            recycler_view_result = (RecyclerView)itemView.findViewById(R.id.recycler_view_result);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            recycler_view_result.setLayoutManager(mLayoutManager);
            recycler_view_result.setItemAnimator(new DefaultItemAnimator());

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_result_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.std_name.setText(list.get(position).getName());
        holder.std_roll.setText(list.get(position).getRollno());
        holder.std_dob.setText(list.get(position).getDob());
        if(!ApiUtils.isEmptyString(list.get(position).getImage())) {
            Glide.with(mContext).load(ApiUtils.ImageBaseUrl + list.get(position).getImage()).error(R.drawable.mypic).into(holder.user_image);
        }
        holder.father_name.setText(list.get(position).getFatherName());
        String result=CountTotalMark.count(list.get(position).exam_array);
        holder.total_txt.setText(result);
        if(!result.equalsIgnoreCase("-")) {
            String[] devision = result.split("/");
            int dev=Integer.parseInt(devision[0])/list.get(position).exam_array.size();
            if(dev>=60){
                holder.div_txt.setText("First Division");
            }else if(dev>=45&&dev<60){
                holder.div_txt.setText("Second Division");
            }else if(dev>34&&dev<45){
                holder.div_txt.setText("Third Division");
            }else {
                holder.div_txt.setText("Fail");
            }
        }else {
            holder.div_txt.setText("_");
        }
        ResultAdapter adapter=new ResultAdapter(mContext,list.get(position).exam_array);
        holder.recycler_view_result.setAdapter(adapter);
        holder.marksheet_lay.setVisibility(View.GONE);
        holder.card_std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.marksheet_lay.getVisibility()==View.VISIBLE){
                    holder.marksheet_lay.setVisibility(View.GONE);
                }else {
                    holder.marksheet_lay.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void filter(String charText) {
        if(arraylist.size()<0){
            arraylist.clear();
            arraylist.addAll(savelist);
            TeacherResultAdapter.this.notifyDataSetChanged();
        }
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            arraylist.clear();
            arraylist.addAll(savelist);
            TeacherResultAdapter.this.notifyDataSetChanged();
            //  listName.clear();
            list.addAll(arraylist);
        } else {
            //listName.clear();
            for (ResultModelFull wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    list.add(wp);
                }else if (wp.getRollno().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    list.add(wp);
                }else if (wp.getFatherName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    list.add(wp);
                }
            }
        }
        if(list.size()>0) {
            arraylist.clear();
            arraylist.addAll(list);
            TeacherResultAdapter.this.notifyDataSetChanged();
        }else {
            arraylist.clear();
            arraylist.addAll(savelist);
            TeacherResultAdapter.this.notifyDataSetChanged();
            //  listName.clear();
            list.addAll(arraylist);
        }

    }


}
