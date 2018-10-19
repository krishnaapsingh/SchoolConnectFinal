package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

/**
 * Created by Trieffects on 30-Oct-17.
 */

public class DashboardAdapter extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;
    int height;

    public DashboardAdapter(Context c,String[] web,int[] Imageid ,int h) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
        height=h;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.dash_view, null);
            TextView textView = (TextView) grid.findViewById(R.id.dash_txt);
            TextView badge_notification_1=(TextView)grid.findViewById(R.id.badge_notification_1);
            ImageView imageView = (ImageView)grid.findViewById(R.id.dash_image);
            textView.setText(web[position]);
            imageView.setImageResource(Imageid[position]);

            if(position==0&&!ApiUtils.isEmptyString(PrefrencesUtils.getNoticecount())){
                badge_notification_1.setText(PrefrencesUtils.getNoticecount());
            }else if(position==0){
                badge_notification_1.setVisibility(View.GONE);
            }

            if(position==1&&!ApiUtils.isEmptyString(PrefrencesUtils.getAttendancecount())){
                badge_notification_1.setText(PrefrencesUtils.getAttendancecount());
            }else if(position==1){
                badge_notification_1.setVisibility(View.GONE);
            }
            if(position==2&&!ApiUtils.isEmptyString(PrefrencesUtils.getFeecount())&&!PrefrencesUtils.getUserMode().equalsIgnoreCase("1")){
                badge_notification_1.setText(PrefrencesUtils.getFeecount());
            }else if(position==2){
                badge_notification_1.setVisibility(View.GONE);
            }

            if(position==2&&!ApiUtils.isEmptyString(PrefrencesUtils.getHomecount()) &&PrefrencesUtils.getUserMode().equalsIgnoreCase("1")){
                badge_notification_1.setText(PrefrencesUtils.getHomecount());
            }else if(position==2 &&PrefrencesUtils.getUserMode().equalsIgnoreCase("1")){
                badge_notification_1.setVisibility(View.GONE);
            }

             if(position==3&&!ApiUtils.isEmptyString(PrefrencesUtils.getTimeTablecount())){
                 badge_notification_1.setText(PrefrencesUtils.getTimeTablecount());
            }else if(position==3){
                 badge_notification_1.setVisibility(View.GONE);
             }

             if(position==4&&!ApiUtils.isEmptyString(PrefrencesUtils.getMapcount())){
                 badge_notification_1.setText(PrefrencesUtils.getMapcount());
            }else if(position==4){
                 badge_notification_1.setVisibility(View.GONE);
             }
             if(position==5&&!ApiUtils.isEmptyString(PrefrencesUtils.getResultcount())){
                 badge_notification_1.setText(PrefrencesUtils.getResultcount());
            }else if(position==5){
                 badge_notification_1.setVisibility(View.GONE);
             }

           if(position==6&&!ApiUtils.isEmptyString(PrefrencesUtils.getMessagecount())){
               badge_notification_1.setText(PrefrencesUtils.getMessagecount());
            }else if(position==6){
               badge_notification_1.setVisibility(View.GONE);
              }
         if(position==7&&!ApiUtils.isEmptyString(PrefrencesUtils.getLeavecount())){
             badge_notification_1.setText(PrefrencesUtils.getLeavecount());
          } else if(position==7){
             badge_notification_1.setVisibility(View.GONE);
         }

            if(position==7&&!ApiUtils.isEmptyString(PrefrencesUtils.getDigitalcount())&&PrefrencesUtils.getUserMode().equalsIgnoreCase("1")){
                badge_notification_1.setText(PrefrencesUtils.getDigitalcount());
            }else if(position==7&&PrefrencesUtils.getUserMode().equalsIgnoreCase("1")){
                badge_notification_1.setVisibility(View.GONE);
            }

         if(position==8&&!ApiUtils.isEmptyString(PrefrencesUtils.getDigitalcount())){
             badge_notification_1.setText(PrefrencesUtils.getDigitalcount());
            }else if(position==8){
             badge_notification_1.setVisibility(View.GONE);
            }

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
