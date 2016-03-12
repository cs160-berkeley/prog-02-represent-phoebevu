package com.example.phoebevu.gorepresent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailView extends AppCompatActivity {
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            TextView title = (TextView) findViewById(R.id.title);
            TextView name = (TextView) findViewById(R.id.name);
            TextView party = (TextView) findViewById(R.id.party);
            TextView end = (TextView) findViewById(R.id.endDate);
            ImageView imageView = (ImageView) findViewById(R.id.picture);
            RelativeLayout background = (RelativeLayout) findViewById(R.id.detailBackground);

            if (extras.getString("TITLE").equalsIgnoreCase("senate")) {
                title.setText("Senator");
            } else {
                title.setText("Representative");
            }
            if (extras.getString("PARTY").equalsIgnoreCase("Democrat")) {
                background.setBackgroundResource(R.drawable.detail1);
            } else if (extras.getString("PARTY").equalsIgnoreCase("Republican")) {
                background.setBackgroundResource(R.drawable.detail2);
            } else {
                background.setBackgroundColor(Color.YELLOW);
            }
            name.setText(extras.getString("NAME"));
            party.setText(extras.getString("PARTY"));
            end.setText(extras.getString("END"));

            mPager = (ViewPager) findViewById(R.id.pager);
            if (extras.getString("BIO_ID") != null) {
                Log.d("CONGRESS BIO_ID: ", extras.getString("BIO_ID"));
                String path = "https://theunitedstates.io/images/congress/225x275/" +
                                extras.getString("BIO_ID") + ".jpg";

//                Picasso.with(DetailView.this).load(path).placeholder(R.drawable.usericon).error(R.drawable.usericon).into(imageView);
                new Photo_politicians((ImageView) imageView).execute(path);

            } else {
                Log.d("CONGRESS BIO_ID: ", "IS NULL");
            }
            mPager.setAdapter(new MyTabPagerAdapter(getSupportFragmentManager(), extras.getString("BIO_ID")));
            mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
            mTabs.setDistributeEvenly(true);
            mTabs.setViewPager(mPager);
        }
    }

    class MyTabPagerAdapter extends FragmentPagerAdapter {

        String[] tabs;
        String bioId;

        public MyTabPagerAdapter(FragmentManager fm, String bioId) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
            this.bioId = bioId;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                Bills bills = Bills.getInstance(bioId);
                return bills;
            } else {
                Committees committees = Committees.getInstance(bioId);
                return committees;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
