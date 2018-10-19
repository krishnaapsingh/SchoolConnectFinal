package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Activity.AttendanceActivity;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.StudentListModel;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class TeacherAttendanceAdapter extends RecyclerView.Adapter<TeacherAttendanceAdapter.ViewHolder>{
    List<StudentListModel> stdList;
    private List<StudentListModel> arraylist;
    ArrayList<StudentListModel> savelist;
    int defultselect;
    String item;
    Context mContext;
    public TeacherAttendanceAdapter( Context context,int defultvalue,List<StudentListModel> List,String mItem){
        defultselect=defultvalue;
        stdList=List;
        item=mItem;
        mContext=context;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(List);
        this.savelist = new ArrayList<>();
        this.savelist.addAll(List);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox1,checkbox2,checkbox3,checkbox4;
        TextView txtname;
        public ViewHolder(View itemView) {
            super(itemView);
            checkbox1=(CheckBox)itemView.findViewById(R.id.checkbox1);
            checkbox2=(CheckBox)itemView.findViewById(R.id.checkbox2);
            checkbox3=(CheckBox)itemView.findViewById(R.id.checkbox3);
            checkbox4=(CheckBox)itemView.findViewById(R.id.checkbox4);
            txtname=(TextView)itemView.findViewById(R.id.name);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.attendance_view_teacher, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(item.equalsIgnoreCase("2")){
            item="0";
        }

        if(ApiUtils.isEmptyString(stdList.get(position).attendence_type_id)){
            stdList.get(position).attendence_type_id="2";
        }
        if((defultselect==1&&item.equalsIgnoreCase("0"))||(stdList.get(position).attendence_type_id.equalsIgnoreCase("1")&&defultselect==-1)){
            holder.checkbox1.setChecked(true);
            AttendanceActivity.submitList.get(position).attendence_type_id="1";
        }else if((defultselect==2&&item.equalsIgnoreCase("0"))||(stdList.get(position).attendence_type_id.equalsIgnoreCase("4")&&defultselect==-1)){
            holder.checkbox2.setChecked(true);
            AttendanceActivity.submitList.get(position).attendence_type_id="4";
        }else if((defultselect==3&&item.equalsIgnoreCase("0"))||((stdList.get(position).attendence_type_id.equalsIgnoreCase("2")||stdList.get(position).attendence_type_id.equalsIgnoreCase("3"))&&defultselect==-1)){
            holder.checkbox3.setChecked(true);
            AttendanceActivity.submitList.get(position).attendence_type_id="2";
        }else if ((defultselect==4&&item.equalsIgnoreCase("0"))||(stdList.get(position).attendence_type_id.equalsIgnoreCase("5")&&defultselect==-1)){
            holder.checkbox4.setChecked(true);
            AttendanceActivity.submitList.get(position).attendence_type_id="5";
        }

      //  holder.txtname.setText(stdList.get(position).);

        if(item.equalsIgnoreCase("1")){
            holder.checkbox1.setEnabled(false);
            holder.checkbox2.setEnabled(false);
            holder.checkbox3.setEnabled(false);
            holder.checkbox4.setEnabled(false);

        }

    holder.checkbox1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!item.equalsIgnoreCase("1")){
                holder.checkbox1.setChecked(true);
                holder.checkbox2.setChecked(false);
                holder.checkbox3.setChecked(false);
                holder.checkbox4.setChecked(false);
                AttendanceActivity.submitList.get(position).attendence_type_id="1";
            }
        }
    });

        holder.checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!item.equalsIgnoreCase("1")) {
                    holder.checkbox1.setChecked(false);
                    holder.checkbox2.setChecked(true);
                    holder.checkbox3.setChecked(false);
                    holder.checkbox4.setChecked(false);
                    AttendanceActivity.submitList.get(position).attendence_type_id="4";
                }
            }
        });

        holder.checkbox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!item.equalsIgnoreCase("1")) {
                    holder.checkbox1.setChecked(false);
                    holder.checkbox2.setChecked(false);
                    holder.checkbox3.setChecked(true);
                    holder.checkbox4.setChecked(false);
                    AttendanceActivity.submitList.get(position).attendence_type_id="2";
                }
            }
        });

        holder.checkbox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!item.equalsIgnoreCase("1")) {
                    holder.checkbox1.setChecked(false);
                    holder.checkbox2.setChecked(false);
                    holder.checkbox3.setChecked(false);
                    holder.checkbox4.setChecked(true);
                    AttendanceActivity.submitList.get(position).attendence_type_id="5";
                }
            }
        });

        if(!ApiUtils.isEmptyString(stdList.get(position).lastname)){
            holder.txtname.setText((position+1)+". "+stdList.get(position).firstname+" s/o "+stdList.get(position).father_name);
        }else {
            holder.txtname.setText((position+1)+". "+stdList.get(position).firstname+" "+stdList.get(position).lastname+" s/o "+stdList.get(position).father_name);
        }


    }

    @Override
    public int getItemCount() {
        return stdList.size();
    }




    public void filter(String charText) {
        if(arraylist.size()<0){
            arraylist.clear();
            arraylist.addAll(savelist);
            TeacherAttendanceAdapter.this.notifyDataSetChanged();
        }
        charText = charText.toLowerCase(Locale.getDefault());
        stdList.clear();
        if (charText.length() == 0) {
            arraylist.clear();
            arraylist.addAll(savelist);
            TeacherAttendanceAdapter.this.notifyDataSetChanged();
            //  listName.clear();
            stdList.addAll(arraylist);
        } else {
            //listName.clear();
            for (StudentListModel wp : arraylist) {
                if (wp.firstname.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    stdList.add(wp);
                }else if(wp.lastname.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    stdList.add(wp);
                }else if(wp.father_name.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    stdList.add(wp);
                }
            }
        }
        if(stdList.size()>0) {
            arraylist.clear();
            arraylist.addAll(stdList);
            TeacherAttendanceAdapter.this.notifyDataSetChanged();
        }else {
            arraylist.clear();
            arraylist.addAll(savelist);
            TeacherAttendanceAdapter.this.notifyDataSetChanged();
            //  listName.clear();
            stdList.addAll(arraylist);
        }

    }






}
