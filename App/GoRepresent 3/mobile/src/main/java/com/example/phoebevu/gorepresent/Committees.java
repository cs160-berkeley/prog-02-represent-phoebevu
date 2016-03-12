package com.example.phoebevu.gorepresent;

import android.content.Context;
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


public class Committees extends Fragment {
    private ListView cList;
    private static String BID = "bio_id";

    private String bioId;
    public JSONObject cObj;

    public Committees() {
        // Required empty public constructor
    }

    public static Committees getInstance(String bioId) {
        if (bioId == null) {
            Log.d("DETAIL INSTANCE:", "bioId NULL");
        } else {
            Log.d("DETAIL INSTANCE:", "bioId NOT NULL: " + bioId);
        }
        Committees fragment = new Committees();
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
        View layout = inflater.inflate(R.layout.fragment_committees, container, false);
        final Context context = container.getContext();

        cJson(bioId, layout, context);
        return layout;
    }

    public String cUrl;
    public void cJson(String bioId, final View layout, final Context context) {
        cUrl = "http://congress.api.sunlightfoundation.com/committees?member_ids=" +
                bioId +"&apikey=f9b8cfcd9ffc439382861ef60b3c09b2";

        Log.d("CURL:", cUrl);
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DowloadTask mTask = new DowloadTask(new AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    String error = "Unable to retrieve web page. URL may be invalid.";
                    if (result != null && !result.equalsIgnoreCase(error)) {
                        String[] ref = new String[3];
                        try {
                            cObj = new JSONObject(result);
                            if (cObj == null) {
                                Log.d("COMM:", "bObj NULL");
                            }
                            JSONArray cArray = cObj.getJSONArray("results");
                            if (cArray == null) {
                                Log.d("COMM: ", "cArray NULL");
                            }

                            for (int i = 0; i < 3; i++) {
                                JSONObject c = cArray.getJSONObject(i);
                                if (c == null) {
                                    Log.d("COMM: ", "c NULL");
                                }
                                String cName = c.getString("name");
                                Log.d("COMM: ", "cName: " + cName);

                                ref[i] = cName;
                            }

                        } catch (Exception e) {
                            Log.d("BILLS:", "CAN'T GET JSON OBJ FOR BILL");
                        }

                        cList = (ListView) layout.findViewById(R.id.commList);
                        cArrayAdapter cAdapter = new cArrayAdapter(context, ref);
                        cList.setAdapter(cAdapter);
                    } else {
                        Log.d("BILLs ERROR:", "JUST CAN'T");
                    }
                }
            });
            mTask.execute(cUrl);
        } else {
            Log.d("INFO", "No network connection available.");
        }
    }


}
