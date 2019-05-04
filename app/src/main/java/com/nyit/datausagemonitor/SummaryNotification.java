package com.nyit.datausagemonitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static android.app.PendingIntent.FLAG_ONE_SHOT;
import static android.content.Context.NOTIFICATION_SERVICE;


public class SummaryNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        long mEndTime = System.currentTimeMillis();
        long mStartTimeWeekly =mEndTime-86400000l*7;
        long mStartTimedaily =mEndTime - 86400000l;

        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        DataCollector dataCollectorWeekly = new DataCollector(networkStatsManager,context,mStartTimeWeekly,mEndTime);
        DataCollector dataCollectorDaily = new DataCollector(networkStatsManager,context,mStartTimedaily,mEndTime);

        long dailyUsage = dataCollectorDaily.getAllRxBytesMobile();
        String du= BytesConversion.getDataSize(dailyUsage);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"primary_notification_channel");

        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                FLAG_ONE_SHOT );

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Data usage summary")
                .setContentIntent(pendingIntent)
                .setContentText("Total data usage by phone  for today is: " + du)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
        Toast.makeText(context, "should be here!", Toast.LENGTH_SHORT).show();
    }

}




