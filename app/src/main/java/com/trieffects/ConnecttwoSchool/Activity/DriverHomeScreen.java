package com.trieffects.ConnecttwoSchool.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.orhanobut.hawk.Hawk;
import com.trieffects.ConnecttwoSchool.Adapter.DriverAdapter;
import com.trieffects.ConnecttwoSchool.GeoFencingModel.GeolocationService;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Notification.GPSTracker;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.Other.Utility;
import com.trieffects.ConnecttwoSchool.R;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;

public class DriverHomeScreen extends AppCompatActivity implements View.OnClickListener{

    private PopupWindowHelper popupWindowHelper;
    CircularImageView user_image;
    private View popView;
    private ShimmerRecyclerView shimmerRecycler;
    ImageView back_img;
    Button his_btn,scan_qrbtn;
    EditText d_name,dl_edt,sch_name,rootname;
    private final int REQUEST_LOCATION = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home_screen);
        currentLocation();
        Utility.checkPermission(this);
        d_name=(EditText)findViewById(R.id.d_name);
        dl_edt=(EditText)findViewById(R.id.dl_edt);
        sch_name=(EditText)findViewById(R.id.sch_name);
        rootname=(EditText)findViewById(R.id.rootname);
        user_image=(CircularImageView)findViewById(R.id.user_image);
        if(!ApiUtils.isEmptyString(PrefrencesUtils.getImage())){
            Glide.with(this).load(ApiUtils.ImageBaseUrl+PrefrencesUtils.getImage()).error(R.drawable.mypic).into(user_image);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(!ApiUtils.isEmptyString(PrefrencesUtils.getUserName())){
            d_name.setText(PrefrencesUtils.getUserName());
        }

        if(!ApiUtils.isEmptyString(PrefrencesUtils.getDriverLicnce())){
            dl_edt.setText(PrefrencesUtils.getDriverLicnce());
        }

        if(!ApiUtils.isEmptyString(PrefrencesUtils.getSchoolName())){
            sch_name.setText(PrefrencesUtils.getSchoolName());
        }


        his_btn=(Button)findViewById(R.id.his_btn);
        scan_qrbtn=(Button)findViewById(R.id.scan_qrbtn);
        showHistor();


        his_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverAdapter adapter=new DriverAdapter();
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DriverHomeScreen.this);
                shimmerRecycler.setLayoutManager(mLayoutManager);
                shimmerRecycler.setItemAnimator(new DefaultItemAnimator());
                shimmerRecycler.showShimmerAdapter();
                shimmerRecycler.setAdapter(adapter);
                shimmerRecycler.hideShimmerAdapter();
                popupWindowHelper.showFromBottom(v);
            }
        });

        scan_qrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DriverHomeScreen.this,DriverScreen.class);
                startActivity(intent);
                DriverHomeScreen.this.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        GPSTracker gpsTracker = new GPSTracker(this);
        if(gpsTracker.canGetLocation()){
            PrefrencesUtils.saveLattu(gpsTracker.getLatitude());
            PrefrencesUtils.saveLogude(gpsTracker.getLongitude());
            startService(new Intent(this, GeolocationService.class));
        }else {
            gpsTracker.showSettingsAlert();
        }

    }

    private void currentLocation() {
        if (ActivityCompat.checkSelfPermission(DriverHomeScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DriverHomeScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DriverHomeScreen.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    currentLocation();
                }
                return;
            }

        }
    }

    public void showHistor(){
       popView = LayoutInflater.from(this).inflate(R.layout.pop_student, null);
        popupWindowHelper = new PopupWindowHelper(popView);
        shimmerRecycler=(ShimmerRecyclerView)popView.findViewById(R.id.reclerview_std);
        back_img=(ImageView)popView.findViewById(R.id.back_img);
        back_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_img:
                popupWindowHelper.dismiss();
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.logout:
                Hawk.deleteAll();
                Intent intent=new Intent(DriverHomeScreen.this,SchoolCodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

        }
        return true;

    }
}
