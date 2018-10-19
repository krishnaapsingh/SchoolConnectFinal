package com.trieffects.ConnecttwoSchool.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.trieffects.ConnecttwoSchool.Fragment.HomeWorkDayFragment;
import com.trieffects.ConnecttwoSchool.Other.Utility;
import com.trieffects.ConnecttwoSchool.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeWorkActivity extends AppCompatActivity {
    ActionBar actionBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String check="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.std_homework_frament);
        Utility.checkPermission1(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Home Work");
        // toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Drawable drawable=toolbar.getNavigationIcon();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);


        viewPager = (ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeWorkDayFragment("0"), "Monday");
        adapter.addFragment(new HomeWorkDayFragment("1"), "Tuesday");
        adapter.addFragment(new HomeWorkDayFragment("2"), "Wednesday");
        adapter.addFragment(new HomeWorkDayFragment("3"), "Thursday");
        adapter.addFragment(new HomeWorkDayFragment("4"), "Friday");
        adapter.addFragment(new HomeWorkDayFragment("5"), "Saturday");
        viewPager.setAdapter(adapter);
        Calendar calendar=Calendar.getInstance();

        String input_date=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2=new SimpleDateFormat("EEEE");
        String temp=format2.format(dt1);
        if(temp.equalsIgnoreCase("Monday")){
            viewPager.setCurrentItem(0);
        }else if(temp.equalsIgnoreCase("Tuesday")){
            viewPager.setCurrentItem(1);
        }else if(temp.equalsIgnoreCase("Wednesday")){
            viewPager.setCurrentItem(2);
        }else if(temp.equalsIgnoreCase("Thursday")){
            viewPager.setCurrentItem(3);
        }else if(temp.equalsIgnoreCase("Friday")){
            viewPager.setCurrentItem(4);
        }else if(temp.equalsIgnoreCase("Saturday")){
            viewPager.setCurrentItem(5);
        }else{
            viewPager.setCurrentItem(0);
        }

    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            HomeWorkActivity.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
