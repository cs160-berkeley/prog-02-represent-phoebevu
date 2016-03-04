package com.example.phoebevu.gorepresent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


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

    // TODO: Rename and change types of parameters
    private String mTitle;
    private String mParty;
    private int mIcon;
    private int mRep;

    private OnFragmentInteractionListener mListener;

    public MyCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCardFragment newInstance(String title, String party, int iconId, int rep) {
        MyCardFragment fragment = new MyCardFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(PARTY, party);
        args.putInt(ICON, iconId);
        args.putInt(REP, rep);
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
        }
    }

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout;
        if (mRep == 1) {
            layout = inflater.inflate(R.layout.vote_view, container, false);

        } else {
            layout = inflater.inflate(R.layout.fragment_my_card, container, false);
            TextView t = (TextView) layout.findViewById(R.id.title);
            TextView p = (TextView) layout.findViewById(R.id.party);
            ImageView img = (ImageView) layout.findViewById(R.id.iconid);

            t.setText(mTitle);
            p.setText(mParty);
            img.setImageResource(mIcon);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent trial = new Intent(getActivity(), WatchToPhoneService.class);
                    trial.putExtra("REP", mTitle);

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
