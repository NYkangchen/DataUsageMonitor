package com.nyit.datausagemonitor;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SelectedAppsUsage extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private List<PackageInfoItem> selectedAppInfoItems = new ArrayList<>();
    long mStartTime = 0;
    long mEndTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_apps_usage);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),AddApps.class);
                view.getContext().startActivity(intent);
                finish();

            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        long startTime = preferences.getLong("PASSSTARTDATEFORSELECTEDAPPS", 0);
        mStartTime = startTime;
     //   Toast.makeText(this, "new start time : "+String.valueOf(mStartTime), Toast.LENGTH_SHORT).show();

        PackageInfoDatabaseHelper db = PackageInfoDatabaseHelper.getInstance(getApplicationContext());


        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);

        DataCollector dataCollector = new DataCollector(networkStatsManager,getApplicationContext(),mStartTime,mEndTime);


        List<PackageInfoItem> packageInfoItems = db.getSelectedPackageInfoItems();

        for ( int i = 0; i < packageInfoItems.size();i++){

            String name = packageInfoItems.get(i).getmName();
            Integer uid = packageInfoItems.get(i).getmId();
            String listname = packageInfoItems.get(i).getmListName();
            Long usage = dataCollector.getPackageRxBytesMobile(uid);

            selectedAppInfoItems.add(new PackageInfoItem(name,listname,uid,usage));

        }
        String listsize = String.valueOf(packageInfoItems.size());
       // Toast.makeText(this, listsize, Toast.LENGTH_SHORT).show();

        mRecyclerView = findViewById(R.id.recyclerViewtoSeeSelectedApps);
        SelectedAppInfoRecyclerViewAdapter mAdapter;
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SelectedAppInfoRecyclerViewAdapter(selectedAppInfoItems,getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);


//        StoreTimestamp storeTimestamp = new StoreTimestamp();
//        long [][] times = storeTimestamp.getLastNDays(7);
//
//        for (int i =0; i<=times.length-5;i++){
//
//            String st = DateFormat.format("MM.dd", times[i][0]).toString();
//            String et = DateFormat.format("MM.dd" +
//                    "", times[i][1]).toString();
//
//            Toast.makeText(this, i + " st:" + st + "  et: "+ et, Toast.LENGTH_SHORT).show();
//
//
//        }




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
