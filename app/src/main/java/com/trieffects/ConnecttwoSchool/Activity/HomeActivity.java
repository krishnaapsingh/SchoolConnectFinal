package com.trieffects.ConnecttwoSchool.Activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.orhanobut.hawk.Hawk;
import com.trieffects.ConnecttwoSchool.Fragment.AttendanceFragment;
import com.trieffects.ConnecttwoSchool.Fragment.DashboardFragment;
import com.trieffects.ConnecttwoSchool.Fragment.DigitalTeacherFragment;
import com.trieffects.ConnecttwoSchool.Fragment.ExamListFragment;
import com.trieffects.ConnecttwoSchool.Fragment.HolidayFragment;
import com.trieffects.ConnecttwoSchool.Fragment.LeaveApplicationFragment;
import com.trieffects.ConnecttwoSchool.Fragment.MessageFragment;
import com.trieffects.ConnecttwoSchool.Fragment.NoticeboardFragment;
import com.trieffects.ConnecttwoSchool.Fragment.ProfileFragment;
import com.trieffects.ConnecttwoSchool.Fragment.StdFeeFragment;
import com.trieffects.ConnecttwoSchool.Fragment.SubjectFragment;
import com.trieffects.ConnecttwoSchool.Fragment.SyllabusDownload;
import com.trieffects.ConnecttwoSchool.GeoFencingModel.MainActivity;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Notification.FireMsgService;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.Other.Utility;
import com.trieffects.ConnecttwoSchool.R;
import com.trieffects.ConnecttwoSchool.TecherFragment.Leave_Applicationfragment_fragment;
import com.trieffects.ConnecttwoSchool.TecherFragment.SyllabusUpload;
import com.trieffects.ConnecttwoSchool.TecherFragment.TeacherAttendanceFragment;
import com.trieffects.ConnecttwoSchool.TecherFragment.TeacherDigitalFragment;
import com.trieffects.ConnecttwoSchool.TecherFragment.TeacherHomeWork;
import com.trieffects.ConnecttwoSchool.TecherFragment.TeacherMessageFragment;
import com.trieffects.ConnecttwoSchool.TecherFragment.TeacherNoticeBoard;
import com.trieffects.ConnecttwoSchool.TecherFragment.TeacherProfileFragment;
import com.trieffects.ConnecttwoSchool.TecherFragment.TeacherResultShowFragment;
import com.trieffects.ConnecttwoSchool.TecherFragment.Teacher_fee_fragment;
import com.trieffects.ConnecttwoSchool.menu.DrawerAdapter;
import com.trieffects.ConnecttwoSchool.menu.DrawerItem;
import com.trieffects.ConnecttwoSchool.menu.SimpleItem;
import com.trieffects.ConnecttwoSchool.menu.SpaceItem;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class HomeActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener{
    private static final int POS_NOtice = 0;
    private static final int POS_ClassRoom = 1;
    private static final int POS_FeeActivity = 2;
    private static final int POS_TimeTable = 3;
    private static final int Asign_sub = 4;

    private static final int POS_Lounge= 5;
    private static final int POS_Calender= 6;
    private static final int POS_LOGOUT = 7;
    ActionBar actionBar;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;
    Toolbar toolbar;
    int check=0;
    CircularImageView user_image;
    TextView userName,userEmail;
    DrawerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Utility.checkPermission(this);

        FireMsgService.setBadge(this,0,1);
       toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Dashboard");
        toolbar.setTitleTextColor(Color.WHITE);
  slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        if(PrefrencesUtils.getUserMode().equalsIgnoreCase("1")){
            adapter =new DrawerAdapter(Arrays.asList(
                    createItemFor(POS_NOtice).setChecked(true),
                    createItemFor(POS_ClassRoom),
                    createItemFor(2),
                    createItemFor(3),
                    createItemFor(4),
                    new SpaceItem(24),
                    createItemFor(5)));
        }else if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2")){
            adapter =new DrawerAdapter(Arrays.asList(
                    createItemFor(POS_NOtice).setChecked(true),
                    createItemFor(POS_ClassRoom),
                    createItemFor(POS_FeeActivity),
                    createItemFor(3),
                    createItemFor(4),
                    new SpaceItem(24),
                    createItemFor(5)));
        } else{
            adapter =new DrawerAdapter(Arrays.asList(
                    createItemFor(POS_NOtice).setChecked(true),
                    createItemFor(POS_ClassRoom),
                    createItemFor(POS_FeeActivity),
                    createItemFor(POS_TimeTable),
                    createItemFor(Asign_sub),
                    createItemFor(POS_Lounge),
                    createItemFor(POS_Calender),
                    new SpaceItem(24),
                    createItemFor(POS_LOGOUT)));
        }
        adapter.setListener(this);
        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        userName=(TextView)findViewById(R.id.user_name);
        userEmail=(TextView)findViewById(R.id.useremail);
        user_image=(CircularImageView)findViewById(R.id.user_image);
        userName.setText(PrefrencesUtils.getUserName());
        if(!ApiUtils.isEmptyString(PrefrencesUtils.getEmail())){
            userEmail.setText(PrefrencesUtils.getEmail());
        }

        if(!ApiUtils.isEmptyString(PrefrencesUtils.getImage())){
            Glide.with(this).load(ApiUtils.ImageBaseUrl+PrefrencesUtils.getImage()).error(R.drawable.mypic).into(user_image);
        }


        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setSelected(POS_NOtice);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                if(check==0) {
                    toolbar.setTitle("Dashboard");
                    Fragment selectedScreen = new DashboardFragment(HomeActivity.this);
                    showFragment(selectedScreen);
                }
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onItemSelected(int position) {

        if ((position-1) ==POS_LOGOUT) {
            Hawk.deleteAll();
            Intent intent=new Intent(HomeActivity.this,SchoolCodeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else  if ((position) ==POS_ClassRoom) {
            check=1;
            toolbar.setTitle("Profile");
            if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2")) {
                Fragment selectedScreen = new TeacherProfileFragment();
                showFragment(selectedScreen);
            }else {
                Fragment selectedScreen = new ProfileFragment();
                showFragment(selectedScreen);
            }
        }else  if ((position) ==POS_FeeActivity) {
            check=1;
            toolbar.setTitle("Home Work");
            if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2")){
                PrefrencesUtils.saveHomecount("");
                Fragment selectedScreen = new TeacherHomeWork();
                showFragment(selectedScreen);
            }else {
                HomeWorkActivity.check = "1";
                HomeWorkActivity.check = "1";
                PrefrencesUtils.saveHomecount("");
               Intent intent=new Intent(HomeActivity.this,HomeWorkActivity.class);
                startActivity(intent);
            }

        }else if(position==Asign_sub){
            check=1;
            toolbar.setTitle("Subject");
            Fragment selectedScreen = new SubjectFragment();
            showFragment(selectedScreen);
        }else if(position==POS_TimeTable){
            Intent intent=new Intent(HomeActivity.this,ExamTimeTable.class);
            startActivity(intent);
        }else if(position==POS_Lounge){
            check=1;
            toolbar.setTitle("Syllabus");
            if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2")){
                Fragment selectedScreen = new SyllabusUpload();
                showFragment(selectedScreen);
            }else {
                Fragment selectedScreen = new SyllabusDownload();
                showFragment(selectedScreen);
            }
        }else if(position==POS_Calender){
            check=1;
            toolbar.setTitle("Holiday List");
            Fragment selectedScreen = new HolidayFragment();
            showFragment(selectedScreen);
        }else {
            toolbar.setTitle("Dashboard");
            Fragment selectedScreen = new DashboardFragment(HomeActivity.this);
            showFragment(selectedScreen);
        }
        slidingRootNav.closeMenu();

    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        HomeActivity.this.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position],position)
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorPrimaryDark))
                .withSelectedTextTint(color(R.color.colorPrimaryDark));
    }

    private String[] loadScreenTitles(){
        if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2")){
            return getResources().getStringArray(R.array.teacher_drawer);
        }if(PrefrencesUtils.getUserMode().equalsIgnoreCase("1")){
            return getResources().getStringArray(R.array.ld_student);
        }
            return getResources().getStringArray(R.array.ld_activityScreenTitles);

    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta;

             if(PrefrencesUtils.getUserMode().equalsIgnoreCase("1")) {
                 ta = getResources().obtainTypedArray(R.array.stduicon);
             }else  if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2")) {
                 ta = getResources().obtainTypedArray(R.array.techer_activityScreenIcons);
             }else {
                 ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
             }

        Drawable[]  icons= new Drawable[ta.length()];

        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                    icons[i] = ContextCompat.getDrawable(this, id);

            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    public void Replacefragment(int i){
        check=1;
        if(i==0){
            toolbar.setTitle("Notice board");
            if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2"))
            {
                Fragment selectedScreen = new TeacherNoticeBoard();
                showFragment(selectedScreen);
            }else {
                Fragment selectedScreen = new NoticeboardFragment();
                showFragment(selectedScreen);
            }
            PrefrencesUtils.saveNoticecount("");
        }else if(i==1){
            toolbar.setTitle("Attendance");
            if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2"))
            {
                Fragment selectedScreen = new TeacherAttendanceFragment();
                showFragment(selectedScreen);
            }else {
                Fragment selectedScreen = new AttendanceFragment();
                showFragment(selectedScreen);
            }
            PrefrencesUtils.saveAttendancecount("");
        } if(i==2){
            if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2"))
            {  toolbar.setTitle("Fee Activity");

                Fragment selectedScreen = new Teacher_fee_fragment();
                showFragment(selectedScreen);
            }else {
                if(PrefrencesUtils.getUserMode().equalsIgnoreCase("1")){
                    HomeWorkActivity.check = "1";
                    HomeWorkActivity.check = "1";
                    PrefrencesUtils.saveHomecount("");
                    Intent intent=new Intent(HomeActivity.this,HomeWorkActivity.class);
                    startActivity(intent);
                }else {
                    toolbar.setTitle("Fee Activity");
                    Fragment selectedScreen = new StdFeeFragment();
                    showFragment(selectedScreen);
                }
            }
        }else if(i==3){
            if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2"))
            {
                Intent intent = new Intent(HomeActivity.this, TeacherTimeTableActivity.class);
                startActivity(intent);
                HomeActivity.this.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }else {
                Intent intent = new Intent(HomeActivity.this, TimeTableActivity.class);
                startActivity(intent);
                HomeActivity.this.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
            PrefrencesUtils.saveTimeTablecount("");
        } if(i==4){
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                HomeActivity.this.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
           PrefrencesUtils.savetMapcount("");
        }else if(i==5){
            toolbar.setTitle("Result");
            if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2"))
            {
                Fragment selectedScreen = new TeacherResultShowFragment();
                showFragment(selectedScreen);
            }else {

                Fragment selectedScreen = new ExamListFragment();
                showFragment(selectedScreen);
            }
            PrefrencesUtils.saveResultcount("");

        } if(i==6){
            if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2"))
            {
                toolbar.setTitle("Message");
                Fragment selectedScreen = new TeacherMessageFragment();
                showFragment(selectedScreen);
            }else {
                toolbar.setTitle("Message");
                Fragment selectedScreen = new MessageFragment();
                showFragment(selectedScreen);
            }
            PrefrencesUtils.saveMessagecount("");
        }else if(i==7){

            if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2"))
            {
                toolbar.setTitle("Leave Application");
                Fragment selectedScreen = new Leave_Applicationfragment_fragment();
                showFragment(selectedScreen);
            }else {
                if(PrefrencesUtils.getUserMode().equalsIgnoreCase("1")){
                    Intent intent=new Intent(HomeActivity.this,ExamTimeTable.class);
                    startActivity(intent);
                }else {
                    toolbar.setTitle("Leave Application");
                    Fragment selectedScreen = new LeaveApplicationFragment();
                    showFragment(selectedScreen);
                }
            }
            PrefrencesUtils.saveLeavecount("");
        }else if(i==8){
            toolbar.setTitle("Digital Teacher");
            if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2"))
            {
                Fragment selectedScreen = new TeacherDigitalFragment();
                showFragment(selectedScreen);
            }else {
                Fragment selectedScreen = new DigitalTeacherFragment();
                showFragment(selectedScreen);
            }
            PrefrencesUtils.saveDigitalcount("");
        }
    }

    @Override
    public void onBackPressed() {
        if(check==0){
            finish();
        }else{
            check=0;
            toolbar.setTitle("Dashboard");
            Fragment selectedScreen = new DashboardFragment(HomeActivity.this);
            showFragment(selectedScreen);
            slidingRootNav.closeMenu();
        }
    }
}
