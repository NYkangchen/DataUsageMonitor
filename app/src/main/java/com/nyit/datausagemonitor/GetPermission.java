package com.nyit.datausagemonitor;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

public class GetPermission {

    public static void checkPermissions(Context context) {
        checkNetworkHistoryPermission(context);
    }

    private static void checkNetworkHistoryPermission(Context context) {
        if (!hasNetworkHistoryPermissions(context)) {
            requestNetworkHistoryPermission(context);
        }
        else{
            // Toast.makeText(context, "yeah get permission", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean hasNetworkHistoryPermissions(Context context) {
        return hasPermissionToReadNetworkHistory(context) && hasPermissionToReadPhoneStats(context);
    }

    private static boolean hasPermissionToReadPhoneStats(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }
    }

    private static boolean hasPermissionToReadNetworkHistory(final Context context) {
        final AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        return false;
    }

    public static void requestNetworkHistoryPermission(Context context) {
        if (!GetPermission.hasPermissionToReadNetworkHistory(context)) {
            requestReadNetworkHistoryAccess(context);
        }
        if (!GetPermission.hasPermissionToReadPhoneStats(context)) {
            requestPhoneStateStats(context);
        }
    }
    private static final int READ_PHONE_STATE_REQUEST = 37;
    private static void requestPhoneStateStats(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_REQUEST);
    }

    private static void requestReadNetworkHistoryAccess(final Context context) {
        final AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                context.getApplicationContext().getPackageName(),
                new AppOpsManager.OnOpChangedListener() {
                    @Override
                    public void onOpChanged(String op, String packageName) {
                        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                android.os.Process.myUid(), context.getPackageName());
                        if (mode != AppOpsManager.MODE_ALLOWED) {
                            return;
                        }
                        appOps.stopWatchingMode(this);
                    }
                });
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        context.startActivity(intent);
    }


}
