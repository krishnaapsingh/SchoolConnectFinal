package com.trieffects.ConnecttwoSchool.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Model.SubjectDataList;
import com.trieffects.ConnecttwoSchool.R;

import java.util.List;

/**
 * Created by Trieffects on 18-Nov-17.
 */
public class CustomAdapter1 extends ArrayAdapter<SubjectDataList> {

    LayoutInflater flater;

    public CustomAdapter1(Activity context, int resouceId, int textviewId, List<SubjectDataList> list){

        super(context,resouceId,textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SubjectDataList rowItem = getItem(position);

        View rowview = flater.inflate(R.layout.listitems_layout,null,true);
        TextView txtTitle = (TextView) rowview.findViewById(R.id.title_txt);
        txtTitle.setText(rowItem.name);


        return rowview;
    }
}