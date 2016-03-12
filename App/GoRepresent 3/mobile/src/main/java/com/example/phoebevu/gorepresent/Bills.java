package com.example.phoebevu.gorepresent;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;


public class Bills extends Fragment {
    private ListView billList;
    private static String BID = "bio_id";

    private String bioId;
    public JSONObject bObj;

    public Bills() {
        // Required empty public constructor
    }


    public static Bills getInstance(String bioId) {
        if (bioId == null) {
            Log.d("DETAIL INSTANCE:", "bioId NULL");
        } else {
            Log.d("DETAIL INSTANCE:", "bioId NOT NULL: " + bioId);
        }
        Bills fragment = new Bills();
        Bundle args = new Bundle();
        args.putString(BID, bioId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.bioId = getArguments().getString(BID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_bills, container, false);
        final Context context = container.getContext();

        // Get Bills JSON obj
        bJson(bioId, layout, context);
        return layout;
    }

    public String bUrl;
    public void bJson(String bioId, final View layout, final Context context) {
        bUrl = "http://congress.api.sunlightfoundation.com/bills?sponsor_id=" +
                bioId +"&apikey=f9b8cfcd9ffc439382861ef60b3c09b2";

        Log.d("URL:", bUrl);
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DowloadTask mTask = new DowloadTask(new AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    String error = "Unable to retrieve web page. URL may be invalid.";
                    if (result != null && !result.equalsIgnoreCase(error)) {
                        String[] bList = new String[3];
                        String[] dateList = new String[3];
                        try {
                            bObj = new JSONObject(result);
                            if (bObj == null) {
                                Log.d("BILLS:", "bObj NULL");
                            };
                            JSONArray bArray = bObj.getJSONArray("results");
                            if (bArray == null) {
                                Log.d("BILLS: ", "bArray NULL");
                            }

                            for (int i = 0; i < 3; i++) {
                                JSONObject b = bArray.getJSONObject(i);
                                if (b == null) {
                                    Log.d("BILLS: ", "b NULL");
                                }
                                String bName = b.getString("official_title");
                                bName = bName.substring(0, Math.min(bName.length(), 30))+ "...";
                                String date = b.getString("introduced_on");

                                bList[i] = bName;
                                dateList[i] = date;
                            }

                        } catch (Exception e) {
                            Log.d("BILLS:", "CAN'T GET JSON OBJ FOR BILL");
                        }
                        billList = (ListView) layout.findViewById(R.id.billList);
                        tabArrayAdapter tAdapter = new tabArrayAdapter(context, bList, dateList);
                        billList.setAdapter(tAdapter);
                    } else {
                        Log.d("BILLs ERROR:", "JUST CAN'T");
                    }
                }
            });
            mTask.execute(bUrl);
        } else {
            Log.d("INFO", "No network connection available.");
        }
    }

}
