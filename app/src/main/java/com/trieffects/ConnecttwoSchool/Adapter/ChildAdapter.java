package com.trieffects.ConnecttwoSchool.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.R;

import java.util.List;

/**
 * Created by Trieffects on 18-Nov-17.
 */
public class ChildAdapter extends ArrayAdapter<ChildData> {

    LayoutInflater flater;

    public ChildAdapter(Activity context, int resouceId, int textviewId, List<ChildData> list){
        super(context,resouceId,textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChildData rowItem = getItem(position);

        View rowview = flater.inflate(R.layout.child_view,null,true);
        TextView txtTitle = (TextView) rowview.findViewById(R.id.title_txt);
        txtTitle.setText(rowItem.firstname);


        return rowview;
    }
}