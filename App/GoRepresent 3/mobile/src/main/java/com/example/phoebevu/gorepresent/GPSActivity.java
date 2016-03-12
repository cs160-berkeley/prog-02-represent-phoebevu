package com.example.phoebevu.gorepresent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by phoebevu on 3/8/16.
 */
public class GPSActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mLatitude;
    private String mLongtitude;
    String url;
    private String url2;
    private String response = null;
    private String response2 = null;
    public GPSActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.content_gps);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public String getLat() {
        return mLatitude;
    }

    public String getLon() {
        return mLongtitude;
    }
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            mLatitude = String.valueOf(mLastLocation.getLatitude());
            mLongtitude = String.valueOf(mLastLocation.getLongitude());
            Log.d("GPS INFO:", "LAT: " + mLatitude + " LONG: " + mLongtitude);
            gpsJson(null, mLatitude, mLongtitude);

            String repJSON = response;
            Log.d("GPS VIEW: ", "Before voteJson");
            voteJSON(mLatitude, mLongtitude);
            String geoStr = response2;
            Log.d("GPS VIEW: ", "voteJson Str: " + geoStr);
            String county = null;
            String state = null;
            String phoneWatchMSG = null;
            try {
                Log.d("MOBILE VIEW: ", "Before geoObj");
                JSONObject geoObj = new JSONObject(geoStr);

                JSONArray jArr = geoObj.getJSONArray("results").getJSONObject(0)
                        .getJSONArray("address_components");
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject component = jArr.getJSONObject(i);
                    String type = component.getJSONArray("types").getString(0);
                    if (type.equalsIgnoreCase("administrative_area_level_2")) {
                        county = component.getString("long_name");
                    } else if (type.equalsIgnoreCase("administrative_area_level_1")) {
                        state = component.getString("short_name");
                    }
                }
                Log.d("GPS MAIN:", "COUNTY: " + county);
                Log.d("GPS MAIN:", "STATE: " + state);
            } catch (Exception e) {
                Log.d("GPS MAIN:", "Can't create geoObj");
            }
            if (county != null && state != null) {
                String key = county + ", " + state;

                try {
                    JSONObject voteObj = new JSONObject(loadJSONFromAsset());
                    JSONObject countyState = voteObj.getJSONObject(key);
                    Log.d("GPS VIEW: ", "VOTE INFO: " + countyState.toString());
                    phoneWatchMSG = repJSON + "|" + county + "|" + state + "|" + countyState.toString();
                } catch (Exception e) {
                    Log.d("GPS VIEW:", "voteObj can't create");
                }

            }
            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
            sendIntent.putExtra("JSON", phoneWatchMSG);
            startService(sendIntent);
            Log.d("MOBILE VIEW: ", "After geoJson");
            Intent congressIntent = new Intent(GPSActivity.this, CongressList.class);
            congressIntent.putExtra("JSON", repJSON);

            startActivity(congressIntent);
        }

    }

    public void voteJSON(String lat, String lon) {
        url2 = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon;
        ConnectivityManager connMgr2 = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo2 = connMgr2.getActiveNetworkInfo();
        if (networkInfo2 != null && networkInfo2.isConnected()) {
            DowloadTask gTask = new DowloadTask();
            gTask.execute(url2);
            try {
                response2 = gTask.get();
                Log.d("GPS MAIN:", "GPS vote REsponse" + response2);
            } catch (Exception e) {
                Log.d("GPS MAIN:", "Can't load GPS vote task");
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void gpsJson(String zipcode, String lat, String lon) {

        if (lat != null && lon != null) {
            url = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=" +
                    lat +"&longitude="+ lon+"&apikey=f9b8cfcd9ffc439382861ef60b3c09b2";
        } else {
            url = "http://congress.api.sunlightfoundation.com/legislators/locate?zip="
                    + zipcode + "&apikey=f9b8cfcd9ffc439382861ef60b3c09b2";
        }
        Log.d("URL:", url);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DowloadTask mTask = new DowloadTask();
            mTask.execute(url);
            try {
                response = mTask.get();
                Log.d("GPS MAIN:", "GPS REsponse" + response);
            } catch (Exception e) {
                Log.d("GPS MAIN:", "Can't load GPS task");
            }
//            new GPSDownloadWebpageTask().execute(url);
        } else {
            Log.d("INFO", "No network connection available.");
        }
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("election-county-2012.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}

