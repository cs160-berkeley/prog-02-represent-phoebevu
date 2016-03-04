package com.example.phoebevu.gorepresent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;

/**
 * Created by phoebevu on 3/1/16.
 */
public class MyGridPageAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<MyRow> mPages;

    public MyGridPageAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
        initPages();
    }

    private void initPages() {
        mPages = new ArrayList<MyRow>();

        MyRow row1 = new MyRow();
        row1.addPages(new MyPage("Sen. Barbara Boxer", "Democrat", R.drawable.barbara, R.drawable.watchbackground, 0));

        MyRow row2 = new MyRow();
        row2.addPages(new MyPage("Sen. Dianne Feinstein", "Democrat", R.drawable.dianne, R.drawable.watchbackground, 0));

        MyRow row3 = new MyRow();
        row3.addPages(new MyPage("Rep. Lorretta Sanchez", "Democrat", R.drawable.lorretta, R.drawable.watchbackground, 0));
        row3.addPages(new MyPage("Rep. Lorretta Sanchez", "Democrat", R.drawable.lorretta, R.drawable.watchbackground, 1));

        mPages.add(row1);
        mPages.add(row2);
        mPages.add(row3);
    }

    @Override
    public Fragment getFragment(int row, int col) {
        MyPage page = ((MyRow)mPages.get(row)).getPages(col);
        MyCardFragment fragment = MyCardFragment.newInstance(page.mTitle, page.mparty, page.mIconId, page.mRep);

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
