package com.trieffects.ConnecttwoSchool.Notification;

/**
 * Created by Trieffects on 25-08-2016.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;

import java.util.List;
import java.util.Locale;


public class GPSTracker extends Service implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;
    boolean isWiFiEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;

    Location locationNetwork, locationGPS; // location
    double latitude; // latitude
    double longitude; // longitude
    double altitude;
    float angle;
    Float accracy;
    float speed = (float) 10.0;
    double curTime = 0;
    double oldLat = 0.0;
    double oldLon = 0.0;
    String StateName, CityName, CountryName, LocalAddress, Throughfare, Subthroughfare;
    int i;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String[] PermissionsLocation =
            {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
    private View mLayout;


    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    public Location getLocation() {

        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {

                    if (ActivityCompat.checkSelfPermission((Activity)mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                        }, 10);
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        locationNetwork = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (locationNetwork != null) {
                            latitude = locationNetwork.getLatitude();
                            longitude = locationNetwork.getLongitude();
                            accracy = locationNetwork.getAccuracy();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (locationGPS == null) {
                        if (ActivityCompat.checkSelfPermission((Activity)mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity)mContext, new String[]{
                                    android.Manifest.permission.ACCESS_FINE_LOCATION
                            }, 10);
                        } else {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                locationGPS = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (locationGPS != null) {
                                    latitude = locationGPS.getLatitude();
                                    longitude = locationGPS.getLongitude();
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return locationGPS;
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                    Toast.makeText(this, "It's good", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }


    public void get_LocationDetails() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.d("Network", "Network");
        List<String> providers = locationManager.getProviders(true);

        Location l = null;
        for (int i = 0; i < providers.size(); i++) {
            l = locationManager.getLastKnownLocation(providers.get(i));
            if (l != null)
                break;
        }
        if (l != null) {
            latitude = l.getLatitude();
            longitude = l.getLongitude();
        }

    }


    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (locationGPS != null) {
            latitude = locationGPS.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */

//    public double getAngle(double startLat, double startLng, double endLat, double endLng) {
//        double startLat = getLatitude();
//        double startLng = getLongitude();
//        new Handler().postDelayed(new Runnable() {
//
//
//            @Override
//            public void run() {
//                /* This method will be executed once the timer is over
//                Start your app main activity */
//
//                // SavePref savePref = new SavePref(SplashActivity.this);
//                // boolean status = savePref.fetchLoginResponse();
//
//
//
//            }
//        }, 60000);
//
//
//        double latitude1 = Math.toRadians(startLat);
//        double latitude2 = Math.toRadians(endLat);
//        double longDiff = Math.toRadians(endLng - startLng);
//        double y = Math.sin(longDiff) * Math.cos(latitude2);
//        double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);
//        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
//
//    }

    public double getLongitude() {
        if (locationGPS != null) {
            longitude = locationGPS.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public Float getAccuracyByGPS() {
        if (locationGPS != null) {
            accracy = locationGPS.getAccuracy();
        }
        return accracy;
    }

    public Float getAccuracyByNetwork() {
        if (locationNetwork != null) {
            accracy = locationNetwork.getAccuracy();
        }
        return accracy;
    }

    public float getSpeed() {
        double newTime = System.currentTimeMillis();
        double newLat = getLatitude();
        double newLon = getLongitude();
        if (locationNetwork.hasSpeed()) {
            speed = locationNetwork.getSpeed();
            Toast.makeText(getApplication(), "SPEED : " + String.valueOf(speed) + "m/s", Toast.LENGTH_SHORT).show();
        } else {
            double distance = calculationBydistance(newLat, newLon, oldLat, oldLon);
            double timeDifferent = newTime - curTime;
            speed = (float) (distance / timeDifferent);
            curTime = newTime;
            oldLat = newLat;
            oldLon = newLon;


        }
        return speed;
    }

    public double calculationBydistance(double lat1, double lon1, double lat2, double lon2) {
        double radius = 6372.8;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return radius * c;
    }

    public double getAltitude() {
        if (locationGPS != null) {
            altitude = locationGPS.getAltitude();
        }
        return altitude;
    }


    public String Address() {
        try {
// Getting address from found locations.
            Log.e("ENTER IN GEOCODER =", String.valueOf(StateName));
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(mContext, Locale.getDefault());
            addresses = geocoder.getFromLocation(locationGPS.getLatitude(), locationGPS.getLongitude(), 1);
            Log.e("ADDRESSES IN GEOCODER =", String.valueOf(addresses));
            if (addresses != null && addresses.size() > 0) {

                StateName = addresses.get(0).getAdminArea();
                CountryName = addresses.get(0).getCountryName();

                Throughfare = addresses.get(0).getAddressLine(0);
                Subthroughfare = addresses.get(0).getAddressLine(1);
                CityName = addresses.get(0).getAddressLine(2);
//                LocalAddress = addresses.get(0).getAddressLine()
//                Address returnAddress = addresses.get(0);
//                for (i = 0; i < returnAddress.getMaxAddressLineIndex(); i++) {
                LocalAddress = (Throughfare + "," + Subthroughfare + "," + CityName);
//                returnAddress.getAddressLine(i) }
                // you can get more details other than this . like country code, state code, etc.
                Log.e("StateName =", String.valueOf(StateName));
                Log.e("CityName =", String.valueOf(CityName));
                Log.e("CountryName =", String.valueOf(CountryName));
                Log.e("LocalAddress =", String.valueOf(LocalAddress));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LocalAddress;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS Settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        PrefrencesUtils.saveLattu(location.getLatitude());
        PrefrencesUtils.saveLogude(location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


}


