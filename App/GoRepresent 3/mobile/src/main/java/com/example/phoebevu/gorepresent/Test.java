package com.example.phoebevu.gorepresent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView) findViewById(R.id.img);
        String path = "https://theunitedstates.io/images/congress/225x275/L00579.jpg";
        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").placeholder(R.drawable.barbara)
                .error(R.drawable.logo2).into(imageView);
    }

}
