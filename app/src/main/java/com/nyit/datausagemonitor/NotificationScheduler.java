package com.nyit.datausagemonitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.Calendar;

public class NotificationScheduler {


    public static void setNotification(Context context){


            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent myIntent;
            PendingIntent pendingIntent;

            // SET TIME HERE everyday at 9:30 pm
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 21);
            calendar.set(Calendar.MINUTE, 30);


            ComponentName receiver = new ComponentName(context.getApplicationContext(),SummaryNotification.class);
            PackageManager pm = context.getApplicationContext().getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);


            myIntent = new Intent(context, SummaryNotification.class);
            pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

            //   manager.set(AlarmManager.RTC, SystemClock.elapsedRealtime()+3000,pendingIntent);

            // manager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);

            manager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 10 * 1000, pendingIntent);

        //    Toast.makeText(context, "what??D?", Toast.LENGTH_SHORT).show();

    }
}
