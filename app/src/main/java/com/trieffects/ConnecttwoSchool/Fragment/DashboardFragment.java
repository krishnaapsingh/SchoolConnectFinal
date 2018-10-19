package com.trieffects.ConnecttwoSchool.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.trieffects.ConnecttwoSchool.Activity.HomeActivity;
import com.trieffects.ConnecttwoSchool.Adapter.DashboardAdapter;
import com.trieffects.ConnecttwoSchool.Adapter.MyAdapter;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

@SuppressLint("ValidFragment")
public class DashboardFragment extends Fragment {
    GridView grid;
    String[] web = {"Notice Board", "Attendance", "Fee Activity", "Timetable", "Tracking", "Result","Connect","Leave Application","Digital Teacher"} ;
    int[] imageId = {R.drawable.noticeboard, R.drawable.attendance, R.drawable.feeactivity,R.drawable.timetable, R.drawable.tracking, R.drawable.result, R.drawable.message, R.drawable.leaveapplication, R.drawable.digitalteacher,};

    String[] stdweb = {"Notice Board", "Attendance", "HomeWork", "Timetable", "Tracking", "Result","Message","Exam. TimeTable","Digital Teacher"} ;
    int[] stdimageId = {R.drawable.noticeboard, R.drawable.attendance, R.drawable.feeactivity,R.drawable.timetable, R.drawable.tracking, R.drawable.result, R.drawable.message, R.drawable.leaveapplication, R.drawable.digitalteacher,};

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {R.drawable.baneer_1,R.drawable.unt,R.drawable.baneer_1,R.drawable.unt,R.drawable.baneer_1};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    CircleIndicator indicator;
    HomeActivity mContext;
    public DashboardFragment(){

    }

    public DashboardFragment(HomeActivity activity){
        mContext=activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dashboard_view,container,false);
        grid=(GridView)view.findViewById(R.id.grid);
        mPager = (ViewPager)view.findViewById(R.id.pager);
       indicator = (CircleIndicator)view.findViewById(R.id.indicator);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        DashboardAdapter adapter;
        if(PrefrencesUtils.getUserMode().equalsIgnoreCase("1")){
            adapter=new DashboardAdapter(getActivity(), stdweb, stdimageId,height);
        }else {
            adapter=new DashboardAdapter(getActivity(), web, imageId,height);
        }
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mContext.Replacefragment(position);

            }
        });

        init();
        return view;
    }

    private void init() {
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager.setAdapter(new MyAdapter(getActivity(),XMENArray));

        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 5000);
    }
}
