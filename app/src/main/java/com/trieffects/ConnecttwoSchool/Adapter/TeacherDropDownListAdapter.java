package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Model.MessageModel;
import com.trieffects.ConnecttwoSchool.R;
import com.trieffects.ConnecttwoSchool.TecherFragment.TeacherMessageFragment;

import java.util.List;

/**
 * Created by Trieffects on 29-Nov-17.
 */

public class TeacherDropDownListAdapter extends ArrayAdapter<MessageModel.MessageData> {

    private List<MessageModel.MessageData> dataSet;
    Context mContext;
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkbox;

    }

    public TeacherDropDownListAdapter(List<MessageModel.MessageData> data, Context context) {
        super(context, R.layout.drop_down_list_row, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MessageModel.MessageData dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.drop_down_list_row, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name_txt);
            viewHolder.checkbox=(CheckBox)convertView.findViewById(R.id.checkbox);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        if(checkSelectScroll(dataModel.pid)==1){
            viewHolder.checkbox.setChecked(true);
        }else {
            viewHolder.checkbox.setChecked(false);
            ;
        }

        final String id=dataModel.pid;
        if(TeacherMessageFragment.checkSelected[position]==true&&checkSelectScroll(id)==1){
            viewHolder.checkbox.setChecked(true);
        }else {
            viewHolder.checkbox.setChecked(false);
        }

        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TeacherMessageFragment.checkSelected[position]){
                    viewHolder.checkbox.setChecked(false);
                    checkRemove(id);
                    TeacherMessageFragment.checkSelected[position]=false;
                }else {
                    viewHolder.checkbox.setChecked(true);
                    TeacherMessageFragment.checklist.add(id);
                    TeacherMessageFragment.checkSelected[position]=true;
                }
            }
        });
        viewHolder.txtName.setText(dataModel.father_name);

        return convertView;
    }


    private int checkSelectScroll(String id) {
        for(int i=0;i<TeacherMessageFragment.checklist.size();i++){
            if(TeacherMessageFragment.checklist.get(i).equalsIgnoreCase(id)){
                return 1;
            }
        }
        return -1;
    }

    private void checkRemove(String id) {
        for(int i=(TeacherMessageFragment.checklist.size()-1);i>0;i--){
            if(TeacherMessageFragment.checklist.get(i).equalsIgnoreCase(id)){
                TeacherMessageFragment.checklist.remove(i);
            }
        }
    }

}
