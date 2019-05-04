package com.nyit.datausagemonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Calendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView =findViewById(R.id.calendar_view);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                AlertDialog.Builder builder = new AlertDialog.Builder(calendarView.getContext());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd" , Locale.US);
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-5:00"));
                String dayString = Integer.toString(day);
                String monthString =Integer.toString(month);

                if (day<10){
                    dayString = "0" + Integer.toString(day);
                }

                if(month<10){
                    monthString ="0" + Integer.toString(month);
                }

                String time = Integer.toString(year) + monthString+dayString;
                Toast.makeText(Calendar.this, time, Toast.LENGTH_SHORT).show();

                long unixTime = 0l;
                try {
                     unixTime = dateFormat.parse(time).getTime();
                    String unixT = Long.toString(unixTime);
      //              Toast.makeText(Calendar.this, unixT, Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final long passUnixTime = unixTime;

                String date =month +"/" + dayString + "/" + year;
                builder.setTitle("Do you want to change the start date to : "+ date);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putLong("STARTINGTIME", passUnixTime);
                        editor.commit();


                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
      //                  intent.putExtra("PASSUNIXTIME",passUnixTime );
                        getApplicationContext().startActivity(intent);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();




            }
        });






    }
}
