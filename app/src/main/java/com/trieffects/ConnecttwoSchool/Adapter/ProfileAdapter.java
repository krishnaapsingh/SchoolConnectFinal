package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.trieffects.ConnecttwoSchool.Fragment.ProfileFragment;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    ArrayList<ChildData> data;
    Context mContext;
    ProfileFragment farg;
    public ProfileAdapter(Context context, ArrayList<ChildData> list,ProfileFragment f){
        data=list;
        mContext=context;
        farg=f;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircularImageView user_image;
        TextView child_name,class_txt,section_txt;
        LinearLayout main;
        public ViewHolder(View itemView) {
            super(itemView);
            child_name=(TextView)itemView.findViewById(R.id.child_name);
            class_txt=(TextView)itemView.findViewById(R.id.class_txt);
            section_txt=(TextView)itemView.findViewById(R.id.section_txt);
            user_image=(CircularImageView)itemView.findViewById(R.id.user_image);
            main=(LinearLayout)itemView.findViewById(R.id.main);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.profile_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
    holder.child_name.setText(data.get(position).firstname+" "+data.get(position).lastname);
    holder.class_txt.setText(data.get(position).my_class);
    holder.section_txt.setText(data.get(position).section);
        if(!ApiUtils.isEmptyString(data.get(position).image)){
            Glide.with(mContext).load(ApiUtils.ImageBaseUrl+data.get(position).image).error(R.drawable.mypic).into(holder.user_image);
        }

        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farg.showHistor(position,v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
