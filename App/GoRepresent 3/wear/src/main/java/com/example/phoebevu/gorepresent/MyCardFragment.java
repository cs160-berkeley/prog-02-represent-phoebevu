package com.example.phoebevu.gorepresent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCardFragment extends CardFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "title";
    private static final String PARTY = "party";
    private static final String ICON = "iconid";
    private static final String REP = "rep";
    private static final String JSTR = "jstr";
    private static final String C = "county";
    private static final String S = "state";
    private static final String OVOTE = "oVote";
    private static final String RVOTE = "rVote";
    // TODO: Rename and change types of parameters
    private String mTitle;
    private String mParty;
    private int mIcon;
    private int mRep;
    private String mjStr;
    private String county;
    private String state;
    private int obama;
    private int romney;

    private OnFragmentInteractionListener mListener;

    public MyCardFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MyCardFragment newInstance(String title, String party, int iconId, int rep, String jStr,
                                             String c, String s, int oVote, int rVote) {
        MyCardFragment fragment = new MyCardFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(PARTY, party);
        args.putInt(ICON, iconId);
        args.putInt(REP, rep);
        args.putString(JSTR, jStr);
        args.putString(C, c);
        args.putString(S, s);
        args.putInt(OVOTE, oVote);
        args.putInt(RVOTE, rVote);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
            mParty = getArguments().getString(PARTY);
            mIcon = getArguments().getInt(ICON);
            mRep = getArguments().getInt(REP);
            mjStr = getArguments().getString(JSTR);
            county = getArguments().getString(C);
            state = getArguments().getString(S);
            obama = getArguments().getInt(OVOTE);
            romney = getArguments().getInt(RVOTE);
        }
    }

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout;
        if (mRep == 1) {
            layout = inflater.inflate(R.layout.vote_view, container, false);
            TextView mCounty = (TextView) layout.findViewById(R.id.county);
            TextView mstate = (TextView) layout.findViewById(R.id.state);
            TextView mOVote = (TextView) layout.findViewById(R.id.ovote);
            TextView mRVote = (TextView) layout.findViewById(R.id.rvote);

            mCounty.setText(county);
            mstate.setText("State: " + state);
            mOVote.setText("Obama: " + obama  + "% Vote");
            mRVote.setText("Romney: " + romney + "% Vote");

        } else {
            layout = inflater.inflate(R.layout.card, container, false);
            TextView t = (TextView) layout.findViewById(R.id.title);
            TextView p = (TextView) layout.findViewById(R.id.party);
            ImageView img = (ImageView) layout.findViewById(R.id.iconid);

            t.setText(mTitle);
            p.setText(mParty);
            if (mParty.equalsIgnoreCase("Democrat")) {
                img.setImageResource(R.drawable.democrat);
            } else {
                img.setImageResource(R.drawable.republican);
            }

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent trial = new Intent(getActivity(), WatchToPhoneService.class);
                    try {
                        JSONObject jObj = new JSONObject(mjStr);
                        trial.putExtra("BIO_ID", jObj.getString("bioguide_id"));
                        trial.putExtra("TITLE", jObj.getString("chamber"));
                        trial.putExtra("NAME", jObj.getString("first_name") + " " + jObj.getString("last_name"));
                        trial.putExtra("PARTY", mParty);
                        trial.putExtra("END", jObj.getString("term_end"));

                    } catch (Exception e) {
                        Log.d("CARD FRAGMENT:", "CAN'T CREATE JObj");
                    }
//                    trial.putExtra("REP", mTitle);

                    getActivity().startService(trial);
                }
            });
        }

        return layout;
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_my_card, container, false);
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
