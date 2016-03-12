package com.example.phoebevu.gorepresent;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button zipBttn;
    private Button currBttn;
    public String url;
    public String response = null;
    public String response2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Log.d("AFTER PLISTEN: ", "extra not null");
            String rlat = extras.getString("RLAT");
            Log.d("AFTER PLISTEN:", "  RLAT " + rlat);
            String rlon = extras.getString("RLON");
            Log.d("AFTER PLISTEN:", "  RLON " + rlon);
            updateInfo(null, rlat, rlon);
        }

        zipBttn = (Button) findViewById(R.id.zipBtt);
        zipBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText zipCode = (EditText) findViewById(R.id.zipcode);
                String input = zipCode.getText().toString();

                updateInfo(input, null, null);

            }
        });

        currBttn = (Button) findViewById(R.id.currLoc);
        currBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this, GPSActivity.class);
                startActivity(mapIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateInfo(String input, String lat, String lon) {
        mJson(input, lat, lon);
        String repJSON = response;

        Intent congressIntent = new Intent(MainActivity.this, CongressList.class);
        congressIntent.putExtra("JSON", repJSON);
        startActivity(congressIntent);

        geoJSON(input, lat, lon);
        String geoStr = response2;
        String county = null;
        String state = null;
        String phoneWatchMSG = null;
        try {
            JSONObject geoObj = new JSONObject(geoStr);
            JSONArray jArr = geoObj.getJSONArray("results").getJSONObject(0)
                    .getJSONArray("address_components");

            for (int i = 0; i < jArr.length(); i++) {
                JSONObject component = jArr.getJSONObject(i);
                String type = component.getJSONArray("types").getString(0);
                if (type.equalsIgnoreCase("country")) {
                    String ctr_name = component.getString("short_name");
                    if (!ctr_name.equalsIgnoreCase("US")) {
                        Toast.makeText(this, "This place is not within the U.S boundery, please shake again", Toast.LENGTH_LONG).show();
                        return;}
                }
                if (type.equalsIgnoreCase("administrative_area_level_2")) {
                    county = component.getString("long_name");
                } else if (type.equalsIgnoreCase("administrative_area_level_1")) {
                    state = component.getString("short_name");
                }
            }

        } catch (Exception e) {
            Log.d("MOBILE MAIN:", "Can't create geoObj");
        }
        if (county != null && state != null) {
            String key = county + ", " + state;

            try {
                JSONObject voteObj = new JSONObject(loadJSONFromAsset());
                JSONObject countyState = voteObj.getJSONObject(key);
                Log.d("MOBILE VIEW: ", "VOTE INFO: " + countyState.toString());
                phoneWatchMSG = repJSON + "|" + county + "|" + state + "|" + countyState.toString();
            } catch (Exception e) {
                Log.d("MOBILE VIEW:", "voteObj can't create");
            }
        }
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("JSON", phoneWatchMSG);
        startService(sendIntent);

    }

    public void geoJSON(final String zipcode, String lat, String lon) {
        String geoUrl;
        if (lat == null && lon == null) {
            geoUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + zipcode;
        } else {
            geoUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon;
        }
        ConnectivityManager connMgr2 = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo2 = connMgr2.getActiveNetworkInfo();
        if (networkInfo2 != null && networkInfo2.isConnected()) {
            DowloadTask gTask = new DowloadTask();
            gTask.execute(geoUrl);
            try {
                response2 = gTask.get();
            } catch (Exception e) {
                Log.d("MOBILE MAIN:", "Can't load G task");
            }
        }
    }

    public void mJson(final String zipcode, String lat, String lon) {

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
            } catch (Exception e) {
                Log.d("MOBILE MAIN:", "Can't load task");
            }

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
