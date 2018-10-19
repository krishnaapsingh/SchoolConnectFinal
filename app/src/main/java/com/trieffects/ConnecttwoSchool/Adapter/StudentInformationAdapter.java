package com.trieffects.ConnecttwoSchool.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;


/**
 * Created by Trieffects on 16-Sep-17.
 */

public class StudentInformationAdapter extends RecyclerView.Adapter<StudentInformationAdapter.StudentHolder> {



    public StudentInformationAdapter(){

    }


    public class StudentHolder extends RecyclerView.ViewHolder {
       TextView dueamount_txt;
        public StudentHolder(View itemView) {
            super(itemView);
            dueamount_txt=(TextView)itemView.findViewById(R.id.dueamount_txt);
        }
    }

    @Override
    public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.studentviewadapter, parent, false);
        return new StudentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StudentHolder holder, int position) {
         holder.dueamount_txt.setText(PrefrencesUtils.getCurrencySymbol()+"   20000"+"/-");
    }

    @Override
    public int getItemCount() {
        return 15;
    }
}
