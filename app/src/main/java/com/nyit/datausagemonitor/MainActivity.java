package com.nyit.datausagemonitor;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
     long mStartTime = 0;
     long mEndTime = System.currentTimeMillis();
    NetworkStatsManager networkStatsManager;
    DataCollector dataCollector;
    long mDataLimit;
    int mStartDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button seeAppUsageButton = findViewById(R.id.select_app_button);
        TextView textViewHasPlan = findViewById(R.id.hasPlan);
        TextView textViewWeclome = findViewById(R.id.welcome);
        LinearLayout linearLayout = findViewById(R.id.total_usage_layout);
        TextView totalUploadsView = findViewById(R.id.total_upload_view);
        TextView totalDownLoadsView = findViewById(R.id.total_download_view);
        ImageView setLimitView =findViewById(R.id.set_date_limit_view);
     //   ImageView togoCalendar = findViewById(R.id.go_to_calendar);
        setLimitView.setOnClickListener(this);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mStartDay = preferences.getInt("STARTINGDAY", 1);
        mDataLimit = preferences.getInt("DATALIMITS", 2 );

        Toast.makeText(this, "start day :" +mStartDay
                + " data limits: " + mDataLimit, Toast.LENGTH_SHORT).show();

        // make the inital starting time as the first day of this month. and the ending time is the current time

        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, mStartDay); // set to the beginning of the plan date
        mStartTime = c.getTimeInMillis();
        mEndTime=   System.currentTimeMillis();

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setMax(100);


    //    Toast.makeText(this, "time: "+c.getTime(), Toast.LENGTH_SHORT).show();

        // get permission from user or check the permission

        GetPermission.checkPermissions(this);

        if(GetPermission.hasNetworkHistoryPermissions(this)) {

            linearLayout.setVisibility(View.VISIBLE);
//            togoCalendar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), Calendar.class);
//                    view.getContext().startActivity(intent);
//                }
//            });

        //    long startTime =getIntent().getLongExtra("PASSUNIXTIME", 0l);
//
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            long startTime = preferences.getLong("STARTINGTIME", 0);
//
//            mStartTime=startTime;
         //   mStartTime= 1555701701424l;
        //    mStartTime = 0l;


            networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
            dataCollector = new DataCollector(networkStatsManager,getApplicationContext(),mStartTime,mEndTime);

            long downloadsUsage = dataCollector.getAllRxBytesMobile();
            long uploadsUsage = dataCollector.getAllTxBytesMobile();
            String downloadsView = "Downloads usage for this month : "+ BytesConversion.getDataSize(downloadsUsage);
            String uploadsView ="Uploads usage for this month : " + BytesConversion.getDataSize(uploadsUsage);
            totalDownLoadsView.setText(downloadsView);
            totalUploadsView.setText(uploadsView);


            PackageInfoDatabaseHelper db = PackageInfoDatabaseHelper.getInstance(getApplicationContext());

            if (!db.selectedPackagesTableIsEmpty()) {

            //    Toast.makeText(this, "hahahhahahaha", Toast.LENGTH_SHORT).show();

                seeAppUsageButton.setText("check usage for Apps");


                textViewHasPlan.setVisibility(View.INVISIBLE);
                textViewWeclome.setVisibility(View.INVISIBLE);

                seeAppUsageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), SelectedAppsUsage.class);
                        view.getContext().startActivity(intent);
                        finish();
                    }
                });
            } else {

                Button addAppsButton = findViewById(R.id.select_app_button);

                addAppsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), AddApps.class);
                        view.getContext().startActivity(intent);
                        finish();
                    }
                });

            }

            // bar chart function


            Spinner spinner = findViewById(R.id.theSpinner);
            String[] items = new String[]{"This Cycle", "Weekly", "Monthly"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);


            drawBarChart(7);


            // notifciation
            NotificationScheduler.setNotification(getApplicationContext());


        }

        else {
                textViewHasPlan.setText("Please get the permission");
                seeAppUsageButton.setVisibility(View.INVISIBLE);
            }




    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (i){
            case 1:

                drawBarChart(7);

                break;
            case 2:

                drawBarChart(30);

                break;
            case 0:
                int numOfDays;
                Calendar c = Calendar.getInstance();   // this takes current date
                String day = DateFormat.format("dd", c.getTime()).toString();
          //      Toast.makeText(this, "day is " +day, Toast.LENGTH_SHORT).show();
                int days = Integer.parseInt(day);

                if(days>mStartDay){
                    numOfDays =  days-mStartDay;
                }
                else numOfDays = days + (31 -mStartDay);


                drawBarChart(numOfDays);
                break;


        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void drawBarChart(int days){

        BarChart barChart = findViewById(R.id.bar_chart_view);
        ArrayList<BarEntry>  entries = new ArrayList<>();

        int NumberOfDaystoShowinChat = days;

        StoreTimestamp storeTimestamp = new StoreTimestamp();
        long [][] times = storeTimestamp.getLastNDays(NumberOfDaystoShowinChat);

        String [] xLables = new String[NumberOfDaystoShowinChat];
        long startingDateForSelectedApps = times[0][0];
        for (int i =0; i<=times.length-1;i++){

            DataCollector dc = new DataCollector(networkStatsManager,getApplicationContext(),times[i][0],times[i][1]);

            long usageforTheDay = dc.getAllRxBytesMobile();
            Long dataUsageForChart= BytesConversion.getDataSizeForChart(usageforTheDay);

            String st = DateFormat.format("MM.dd", times[i][0]).toString();
            xLables[i] = st;

            entries.add(new BarEntry(i,dataUsageForChart));

        }

        SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.putLong("PASSSTARTDATEFORSELECTEDAPPS", startingDateForSelectedApps);
        editor1.commit();
      //  Toast.makeText(this, "st :" + startingDateForSelectedApps, Toast.LENGTH_SHORT).show();

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLables));

        BarDataSet barDataSet = new BarDataSet(entries,"Usages");

        BarData data = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(data);
        barChart.invalidate();
        Description description = new Description();
        description.setText("great chat");
        barChart.setDescription(description);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.set_date_limit_view:
                Intent intent = new Intent(view.getContext(), SetMobilePlanActivity.class);
                view.getContext().startActivity(intent);

                break;
        }
    }
}
