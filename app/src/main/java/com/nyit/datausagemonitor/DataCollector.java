package com.nyit.datausagemonitor;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class DataCollector {
    Context mContext;
    NetworkStatsManager mNetworkStatsManager;
    long mStartTime, mEndTime;


    public  DataCollector(NetworkStatsManager networkStatsManager, Context context,long startTime, long endTime){
        mNetworkStatsManager = networkStatsManager;
        mContext = context;
        mStartTime = startTime;
        mEndTime = endTime;

    }
    // total downloads
    public long getAllRxBytesMobile() {
        NetworkStats.Bucket bucket;
        try {
            bucket = mNetworkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(ConnectivityManager.TYPE_MOBILE),
                    mStartTime,
                    mEndTime);
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

//
//    public long getAllRxBytesMobilebydates(long st, long et) {
//        NetworkStats.Bucket bucket;
//        try {
//            bucket = mNetworkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
//                    getSubscriberId(ConnectivityManager.TYPE_MOBILE),
//                    st,
//                    et);
//        } catch (RemoteException e) {
//            return -1;
//        }
//        return bucket.getRxBytes();
//    }


    // total uploads
    public long getAllTxBytesMobile() {
        NetworkStats.Bucket bucket;
        try {
            bucket = mNetworkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(ConnectivityManager.TYPE_MOBILE),
                    mStartTime,
                    mEndTime);
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    public long getAllRxBytesWifi() {
        NetworkStats.Bucket bucket;
        try {
            bucket = mNetworkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    mStartTime,
                    mEndTime);
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getPackageRxBytesMobile(int packageUid) {
        NetworkStats networkStats = null;
        try {
            networkStats = mNetworkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(ConnectivityManager.TYPE_MOBILE),
                    mStartTime,
                    mEndTime,
                    packageUid);


        } catch (RemoteException e) {
            return -1;
        }

        long rxBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            rxBytes += bucket.getRxBytes();
        }
        networkStats.close();
        return rxBytes;
    }
    public long getPackageRxBytesWifi(int packageUid) {
        NetworkStats networkStats = null;
        try {
            networkStats = mNetworkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_WIFI,
                    getSubscriberId(ConnectivityManager.TYPE_WIFI),
                    mStartTime,
                    mEndTime,
                    packageUid);


        } catch (RemoteException e) {
            return -1;
        }

        long rxBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            rxBytes += bucket.getRxBytes();
        }
        networkStats.close();
        return rxBytes;
    }




    private String getSubscriberId(int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                Toast.makeText(mContext,"You don't have READ_PHONE_STATE permission",Toast.LENGTH_SHORT).show();
            }
            return tm.getSubscriberId();
        }
        return "";
    }



}
