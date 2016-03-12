package com.example.phoebevu.gorepresent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by phoebevu on 3/1/16.
 */
public class MyGridPageAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<MyRow> mPages;
    private String[] names;
    private String[] parties;
    private int[] isRep;
    private JSONArray jArray;
    private String county;
    private String state;
    private int obama;
    private int romney;

    public MyGridPageAdapter(Context ctx, FragmentManager fm, String[] names,
                             String[] parties, int[] isRep, JSONArray jArr,
                             String c, String s, int oVote, int rVote) {
        super(fm);
        mContext = ctx;
        this.names = names;
        this.parties = parties;
        this.isRep = isRep;
        this.jArray = jArr;
        this.county = c;
        this.state = s;
        this.obama = oVote;
        this.romney = rVote;
        initPages();
    }


    private void initPages() {
        mPages = new ArrayList<MyRow>();

        for (int i = 0; i < names.length; i++) {
            String title;
            String p;
            if (isRep[i] == 1) {
                title = "Rep. ";
            } else {
                title = "Sen. ";
            }
            String repName = title + names[i];
            Log.d("GRID ADAPT: ", "NAME: " + repName);
            if (parties[i].equalsIgnoreCase("D")) {
                p = "Democrat";
            } else if (parties[i].equalsIgnoreCase("R")) {
                p = "Republican";
            } else {
                p = "Other";
            }
            MyRow row = new MyRow();
            row.addPages(new MyPage(repName, p, R.drawable.barbara, R.drawable.watchbackground, 0));
            if (isRep[i] == 1) {
                row.addPages(new MyPage(repName, p, R.drawable.barbara, R.drawable.watchbackground, 1));
            }
            mPages.add(row);
        }
    }

    @Override
    public Fragment getFragment(int row, int col) {
        MyPage page = ((MyRow)mPages.get(row)).getPages(col);
        JSONObject jObj = null;
        try {
            jObj = jArray.getJSONObject(row);
            String jString = jObj.toString();
        } catch (Exception e) {
            Log.d("GRID ADAPT: ", "CAN'T GET JOBJ");
        }
        MyCardFragment fragment = MyCardFragment.newInstance(page.mTitle, page.mparty,
                                                            page.mIconId, page.mRep, jObj.toString(),
                                                            county, state, obama, romney);

        fragment.setExpansionEnabled(false);
        return fragment;
    }

    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        MyPage page = ((MyRow)mPages.get(row)).getPages(col);
        return mContext.getResources().getDrawable(page.mBackgroundId, null);
    }

    @Override
    public Drawable getBackgroundForRow(int row) {
        return mContext.getResources().getDrawable(R.drawable.watchbackground, null);
    }
    @Override
    public int getRowCount() {
        return mPages.size();
    }

    @Override
    public int getColumnCount(int row) {
        return mPages.get(row).size();
    }


    public class MyRow {

        ArrayList<MyPage> mPagesRow = new ArrayList<MyPage>();

        public void addPages(MyPage page) {
            mPagesRow.add(page);
        }

        public MyPage getPages(int index) {
            return mPagesRow.get(index);
        }

        public int size(){
            return mPagesRow.size();
        }
    }


    public class MyPage {
        public String mTitle;
        public String mparty;
        public int mIconId;
        public int mBackgroundId;
        public int mRep;

        public MyPage(String title, String party, int iconId, int backgroundId, int rep) {
            this.mTitle = title;
            this.mparty = party;
            this.mIconId = iconId;
            this.mBackgroundId = backgroundId;
            this.mRep = rep;
        }
    }



}
