package com.trieffects.ConnecttwoSchool.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.trieffects.ConnecttwoSchool.Notification.GPSTracker;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;
import com.trieffects.ConnecttwoSchool.Services.DriverServices;

import java.util.Hashtable;
import java.util.List;

public class DriverScreen extends AppCompatActivity  implements SurfaceHolder.Callback, View.OnClickListener, Camera.AutoFocusCallback {
    private Context mContext;
    View centerView;
    ActionBar actionBar;
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private AlertDialog mDialog;
    private static Hashtable hints;
    String data;
    static {
        hints = new Hashtable(1);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_screen);
        currentLocation();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Root no-8");
        // toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Drawable drawable=toolbar.getNavigationIcon();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);


        mDialog = new AlertDialog.Builder(DriverScreen.this)
                .setCancelable(false)
                .setPositiveButton("Yes", null).create();
        centerView = (View) this.findViewById(R.id.centerView);
        mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceview);
        mSurfaceView.setOnClickListener(this);
        data=getIntent().getStringExtra("Data");

        GPSTracker gpsTracker = new GPSTracker(this);
        if(gpsTracker.canGetLocation()){
            double lat=gpsTracker.getLatitude();
            double log=gpsTracker.getLongitude();
            PrefrencesUtils.saveLattu(lat);
            PrefrencesUtils.saveLogude(log);
            Intent intent=new Intent(DriverScreen.this,DriverServices.class);
            startService(intent);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Camera Open
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        mCamera.setPreviewCallback(_previewCallback);

        Camera.Parameters parameters = mCamera.getParameters();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(0);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int h = displayMetrics.heightPixels;
        int w = displayMetrics.widthPixels;
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        mCamera.setParameters(parameters);
        mCamera.startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.setPreviewCallback(null);
        mCamera.release();
        mCamera = null;

    }

    @Override
    public void onClick(View v) {
        if (mCamera != null) {
            mCamera.autoFocus(this);

        }
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
    }

    private Camera.PreviewCallback _previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {

            Log.d("onPreviewFrame", "onPreviewFrame Called");

            if (mDialog.isShowing())
                return;

            // Read Range
            Camera.Size size = camera.getParameters().getPreviewSize();


            // Create BinaryBitmap
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                    data, size.width, size.height, 0, 0, size.width, size.height, false);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // Read QR Code
            Reader reader = new MultiFormatReader();
            Result result = null;
            try {
               result = reader.decode(bitmap);
                String text = result.getText();
                mDialog.setMessage(text);
                mDialog.show();

            } catch (NotFoundException e) {
            } catch (ChecksumException e) {
            } catch (FormatException e) {
            }
        }
    };


    /*public void gpsData(){
        String stringLatitude = String.valueOf(gpsTracker.latitude);
        textview = (TextView)findViewById(R.id.fieldLatitude);
        textview.setText(stringLatitude);

        String stringLongitude = String.valueOf(gpsTracker.longitude);
        textview = (TextView)findViewById(R.id.fieldLongitude);
        textview.setText(stringLongitude);

        String country = gpsTracker.getCountryName(this);
        textview = (TextView)findViewById(R.id.fieldCountry);
        textview.setText(country);

        String city = gpsTracker.getLocality(this);
        textview = (TextView)findViewById(R.id.fieldCity);
        textview.setText(city);

        String postalCode = gpsTracker.getPostalCode(this);
        textview = (TextView)findViewById(R.id.fieldPostalCode);
        textview.setText(postalCode);

        String addressLine = gpsTracker.getAddressLine(this);
        textview = (TextView)findViewById(R.id.fieldAddressLine);
        textview.setText(addressLine);
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            DriverScreen.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DriverScreen.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void currentLocation() {
        if (ActivityCompat.checkSelfPermission(DriverScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DriverScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DriverScreen.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10: {
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent=new Intent(DriverScreen.this,DriverServices.class);
                    startService(intent);
                } else {
                    currentLocation();
                }
                return;
            }

        }
    }

}
