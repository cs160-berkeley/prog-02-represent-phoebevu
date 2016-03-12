package com.example.phoebevu.gorepresent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class CongressView extends Activity {

    private TextView mTextView;
    private String jStr;
    private String[] names;
    private String[] parties;
    private int[] isRep;
    private JSONArray resultArray;
    private String county = null;
    private String state = null;
    private int obama = 0;
    private int romney = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congress_view);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            jStr = extras.getString("JSON");
            if (jStr == null) {
                Log.d("WEAR CONGRESS:", "jStr NULL");
            }
            JSONObject jObj;
            try {
                jObj = new JSONObject(jStr);

                resultArray = jObj.getJSONArray("results");
                names = new String[resultArray.length()];
                parties = new String[resultArray.length()];
                isRep = new int[resultArray.length()];

                for (int i=0; i < resultArray.length(); i++) {
                    JSONObject repInfo = resultArray.getJSONObject(i);
                    String repName = repInfo.getString("first_name") + " " + repInfo.getString("last_name");
                    String chamber = repInfo.getString("chamber");
                    if (chamber.equalsIgnoreCase("house")) {
                        isRep[i] = 1;
                    } else {
                        isRep[i] = 0;
                    }
                    names[i] = repName;
                    String party = repInfo.getString("party");
                    parties[i] = party;
                }
            } catch (Exception e) {
                Log.d("WEAR CONGRESS:", "ERROR");
            }
            county = extras.getString("COUNTY");
            state = extras.getString("STATE");
            try {
                JSONObject voteObj = new JSONObject(extras.getString("VOTE"));
                obama = voteObj.getInt("obama");
                romney = voteObj.getInt("romney");
            } catch (Exception e) {Log.d("CONGRESS VIEW:", "can't create voteObj");}
        } else {
            Log.d("WEAR CONGRESS:", "extra NULL");
        }

        final GridViewPager mGridPager = (GridViewPager) findViewById(R.id.pager);
//        mGridPager.setAdapter(new MyGridPageAdapter(this, getFragmentManager()));
        mGridPager.setAdapter(new MyGridPageAdapter(this, getFragmentManager(), names, parties, isRep,
                                                                                resultArray, county, state,
                                                                                    obama, romney));
    }
}
