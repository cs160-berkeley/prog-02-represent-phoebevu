package com.example.phoebevu.gorepresent;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CongressList extends Activity{
    ListView repList;
    private String[] ref;
    private JSONArray resultArray;
    private String[] bio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congress_list);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String jsonStr;
        ref = null;
        resultArray = null;
        if (extras != null) {
            jsonStr = extras.getString("JSON");
            JSONObject jObj;
            try {
                jObj = new JSONObject(jsonStr);
                int repNum = jObj.getInt("count");
                ref = new String[repNum];
                bio = new String[repNum];
                resultArray = jObj.getJSONArray("results");
                for (int i=0; i < resultArray.length(); i++) {
                    JSONObject repInfo = resultArray.getJSONObject(i);
                    String repName = repInfo.getString("first_name") + " " + repInfo.getString("last_name");
                    bio[i] = repInfo.getString("bioguide_id");
                    ref[i] = repName;
                }

            } catch (Exception e) {

            }
        }

        repList = (ListView) findViewById(R.id.repList);
        mArrayAdapter mAdapter = new mArrayAdapter(this, ref, resultArray, bio);
        repList.setAdapter(mAdapter);

        repList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject repInfo = resultArray.getJSONObject(position);
                    Intent detail = new Intent(CongressList.this, DetailView.class);
                    if (repInfo.getString("bioguide_id") == null) {
                        Log.d("CONGRESS BIO_ID: ", "IS NULL");
                    }

                    detail.putExtra("BIO_ID", repInfo.getString("bioguide_id"));
                    detail.putExtra("TITLE", repInfo.getString("chamber"));
                    detail.putExtra("NAME", ref[position]);
                    if (repInfo.getString("party").equalsIgnoreCase("D")) {
                        detail.putExtra("PARTY", "Democrat");
                    } else if (repInfo.getString("party").equalsIgnoreCase("R")) {
                        detail.putExtra("PARTY", "Republican");
                    } else {
                        detail.putExtra("PARTY", "Other");
                    }
                    detail.putExtra("END", repInfo.getString("term_end"));

                    startActivity(detail);
                } catch (Exception e) {
                    Log.d("CLICK ERROR: ", "LIST");
                }

            }
        });


    }



}
