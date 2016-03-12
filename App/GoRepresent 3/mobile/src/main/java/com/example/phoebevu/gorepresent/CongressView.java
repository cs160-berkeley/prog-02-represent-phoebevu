package com.example.phoebevu.gorepresent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CongressView extends AppCompatActivity {
    ListView repList;
    ArrayAdapter arrayAdapter;
//
//    public void rep1(View view) {
//        Intent intent = new Intent(this, DetailView.class);
//        startActivity(intent);
//    }
//
//    public void rep2(View view) {
//        Intent intent = new Intent(this, DetailView2.class);
//        startActivity(intent);
//    }
//
//    public void rep3(View view) {
//        Intent intent = new Intent(this, DetailView3.class);
//        startActivity(intent);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congress_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        repList = (ListView) findViewById(R.id.rep_list);
//
//        // this-The current activity context.
//        // Second param is the resource Id for list layout row item
//        // Third param is input array
//        arrayAdapter = new ArrayAdapter(this, android.R.layout.rep_list);
//        repList.setAdapter(arrayAdapter);


    }

}
