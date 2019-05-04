package com.nyit.datausagemonitor;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class AddApps extends AppCompatActivity {
    public static boolean isAppsInfoGetted = false;
    RecyclerView mRecyclerView;


    private List<PackageInfoItem> packageInfoItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PackageInfoDatabaseHelper db = PackageInfoDatabaseHelper.getInstance(getApplicationContext());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // get all the applicaiton names and uid and listname from the system and stored in SQL
        if(db.allPackagesTableIsEmpty()){
            CreateDataBase();
        }

        else{
            // get all the information from database
            packageInfoItems = db.getAllPackageInfoItems();

//           packageInfoItems.add(new PackageInfoItem("ddd",12,"xd"));
//           packageInfoItems.add(new PackageInfoItem("aaa",1232,"aaaxd"));

        }


        // recyclerview
        mRecyclerView = findViewById(R.id.recyclerViewtoSeeAllApps);
        PackageInfoRecyclerViewAdapter mAdapter;
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PackageInfoRecyclerViewAdapter(packageInfoItems,getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);



    }

    private void CreateDataBase(){
        ActivityManager manager = (ActivityManager) getApplicationContext()
                .getSystemService(getApplicationContext().ACTIVITY_SERVICE);
        manager.getRunningAppProcesses();
        PackageInfoDatabaseHelper db = PackageInfoDatabaseHelper.getInstance(getApplicationContext());

        List<PackageInfo> installedPackages = getApplicationContext().getPackageManager().getInstalledPackages(0);
        //       List<PackageInfoItem> packageInfoItems = new ArrayList<>();

        // create database for packageinfoitems

//            TextView packageNmaeView = findViewById(R.id.packageNames);
        String packageName = "" ;
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = installedPackages.get(i);


            // if it is system app,.. contuine
//            try {
//                ApplicationInfo applicationInfo = getApplicationContext().getPackageManager().getApplicationInfo(packageInfo.packageName,0 );
//                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
//
//                    continue;
//                }
//
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }

            packageName = packageInfo.packageName;
            // packageNmaeView.setText(packageName + i);

            int Nuid = PackageManagerHelper.getPackageUid(getApplicationContext(), packageName);


            // get the listname after .  for example com.android.smoketest  list name is smoketest
         //   String last = packageName.substring(packageName.lastIndexOf('.') + 1);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
           String names=  applicationInfo.loadLabel(getPackageManager()).toString();


            // this is for recyclerview
       //     packageInfoItems.add(new PackageInfoItem(packageName,Nuid,last));
        //    db.insertAllPackages(packageName, Nuid,last);
            packageInfoItems.add(new PackageInfoItem(packageName,Nuid,names));
            db.insertAllPackages(packageName, Nuid,names);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, SelectedAppsUsage.class);
        startActivity(intent);
        finish();
    }
}
