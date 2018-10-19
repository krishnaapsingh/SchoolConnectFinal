package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Activity.ClassResultActivity;
import com.trieffects.ConnecttwoSchool.Activity.StudentResultScreen;
import com.trieffects.ConnecttwoSchool.Fragment.ExamListFragment;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ExamData;
import com.trieffects.ConnecttwoSchool.R;
import com.trieffects.ConnecttwoSchool.TecherFragment.TeacherResultShowFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class ExamLIstAdapter extends RecyclerView.Adapter<ExamLIstAdapter.ViewHolder>{
    List<ExamData> listData=new ArrayList<>();
    Context mContext;
    int itemData;
    public ExamLIstAdapter(List<ExamData> list, Context context,int item){
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
              if(itemData==1) {
                  Intent intent = new Intent(mContext, StudentResultScreen.class);
                  intent.putExtra("exam_id", listData.get(position).exam_id);
                  intent.putExtra("classid", ExamListFragment.classid);
                  intent.putExtra("sectionid",ExamListFragment.sectionid);
                  intent.putExtra("std_id", ExamListFragment.std_id);
                  intent.putExtra("image",ExamListFragment.imageUrl);
                  mContext.startActivity(intent);
              }else {
                  if(ApiUtils.isEmptyString(TeacherResultShowFragment.classname)){
                      Toast.makeText(mContext,"Please Select class",Toast.LENGTH_SHORT).show();
                  }else  if(ApiUtils.isEmptyString(TeacherResultShowFragment.sectionName)){
                      Toast.makeText(mContext,"Please Select section",Toast.LENGTH_SHORT).show();
                  }else {
                      Intent intent = new Intent(mContext, ClassResultActivity.class);
                      intent.putExtra("exam_id", listData.get(position).exam_id);
                      intent.putExtra("class_id", TeacherResultShowFragment.classname);
                      intent.putExtra("section_id",TeacherResultShowFragment.sectionName);
                      mContext.startActivity(intent);
                  }
              }
          }
      });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
