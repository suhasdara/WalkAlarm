package com.suhasdara.walkalarm.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.suhasdara.walkalarm.model.Alarm;

import java.util.Calendar;

public class AlarmUtils {

    public static String getTextTime(Alarm alarm) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(alarm.getTime());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        return (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute;
    }

    public static void checkAlarmPermissions(Activity activity) {
        if(Build.VERSION.SDK_INT < 23) {
            return;
        }

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.VIBRATE);
        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.VIBRATE}, 1);
        }
    }
}
