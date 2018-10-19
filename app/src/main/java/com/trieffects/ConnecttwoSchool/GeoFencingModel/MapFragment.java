package com.trieffects.ConnecttwoSchool.GeoFencingModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trieffects.ConnecttwoSchool.GeoFencingData.DirectionsJSONParser;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.GetLatLogBusModel;
import com.trieffects.ConnecttwoSchool.Notification.GPSTracker;
import com.trieffects.ConnecttwoSchool.R;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {
    protected SupportMapFragment mapFragment;
    protected GoogleMap map;
    ArrayList<LatLng> markerPoints;
    TextView km_dis;
    int t=0;
    MarkerOptions options = new MarkerOptions();
    MarkerOptions options2 = new MarkerOptions();
  //  new MarkerOptions()
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int resultCode = bundle.getInt("done");
                if (resultCode == 1) {
                    GPSTracker gpsTracker=new GPSTracker(getActivity());
                    Double latitude = bundle.getDouble("latitude");
                    Double longitude = bundle.getDouble("longitude");
                    if(gpsTracker.canGetLocation()){
                        latitude=gpsTracker.getLatitude();
                        longitude=gpsTracker.getLongitude();
                    }

                    updateMarker(latitude, longitude);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container,
                false);
        km_dis=(TextView)rootView.findViewById(R.id.km_dis);
        markerPoints = new ArrayList<LatLng>();
        mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getChildFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.map_container, mapFragment);
        fragmentTransaction.commit();

        return rootView;
    }


    public void drawLine(LatLng point,LatLng point2){
        if(markerPoints.size()>1){
            markerPoints.clear();
            //map.clear();
        }

        // Adding new item to the ArrayList
        markerPoints.add(point);
        markerPoints.add(point2);



        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.man_icon);
        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.bus);
        options.position(point2);
        options2.position(point);
        options.icon(icon2);
       options2.icon(icon);

        map.addMarker(options);
        map.addMarker(options2);
        if(markerPoints.size() >= 2){
            LatLng origin = markerPoints.get(0);
            LatLng dest = markerPoints.get(1);
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapFragment != null) {
            t=0;
            mapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    map.animateCamera(CameraUpdateFactory.zoomTo(13));
                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setZoomControlsEnabled(false);
                    displayGeofences();

                }
            });
        }

        getActivity().registerReceiver(receiver,
                new IntentFilter("me.hoen.geofence_21.geolocation.service"));
    }

    protected void displayGeofences() {
        if(t==0) {
            t=1;
            HashMap<String, SimpleGeofence> geofences = SimpleGeofenceStore.getInstance().getSimpleGeofences();

            for (Map.Entry<String, SimpleGeofence> item : geofences.entrySet()) {
                SimpleGeofence sg = item.getValue();

                CircleOptions circleOptions1 = new CircleOptions()
                        .center(new LatLng(28.5960588, 77.3173051))
                        .radius(sg.getRadius()).strokeColor(Color.BLACK)
                        .strokeWidth(2).fillColor(0x500000ff);
                map.addCircle(circleOptions1);
            }
        }
    }

    protected void createMarker(Double latitude, Double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    protected void updateMarker(Double latitude, Double longitude) {
            createMarker(latitude, longitude);
        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        getBusLocation(latLng,latitude,longitude);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.events:
                Fragment f = new EventsFragment();

                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, f).addToBackStack("events")
                        .commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLUE);
            }

            if(lineOptions!=null){
                map.addPolyline(lineOptions);
            }

        }
    }





    public void getBusLocation(final LatLng latLng, final double lat_a, final double lng_a){
        final ApiInterface mApiInterface= ApiUtils.getInterfaceService();
        Call<GetLatLogBusModel> mService=mApiInterface.getBusLocation();
        mService.enqueue(new Callback<GetLatLogBusModel>() {
            @Override
            public void onResponse(Call<GetLatLogBusModel> call, Response<GetLatLogBusModel> response) {
                 GetLatLogBusModel object=response.body();
                 if(object.status){
                     double lat=Double.valueOf(object.data.get(0).curr_lat);
                     double lng=Double.valueOf(object.data.get(0).curr_lng);
                     LatLng latLng2=new LatLng(lat,lng);
                     distance(lat_a,lng_a,Double.valueOf(object.data.get(0).curr_lat),Double.valueOf(object.data.get(0).curr_lng));
                     drawLine(latLng,latLng2);
                 }
            }

            @Override
            public void onFailure(Call<GetLatLogBusModel> call, Throwable t) {

            }
        });
    }


    public void distance (double lat_a, double lng_a, double lat_b, double lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;
        float result=(new Float(distance * meterConversion).floatValue())/1000;
        km_dis.setText(result+ "Km. Away");
    }

}
