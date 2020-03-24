package com.suhasdara.walkalarm.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.SparseBooleanArray;

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

    public static long getNextAlarmTime(Alarm alarm) {
        if(isSingleTimeAlarm(alarm)) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(alarm.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            return cal.getTimeInMillis();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(alarm.getTime());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;

        SparseBooleanArray days = alarm.getDays();

        int day = (dayOfWeek + 1) % Alarm.NUM_DAYS;
        int iteration = 1;
        while(iteration <= Alarm.NUM_DAYS) {
            if(days.get(day)) {
                cal.add(Calendar.DAY_OF_MONTH, iteration);
                break;
            }

            day = (day + 1) % Alarm.NUM_DAYS;
            iteration++;
        }

        return cal.getTimeInMillis();
    }

    public static boolean isSingleTimeAlarm(Alarm alarm) {
        SparseBooleanArray days = alarm.getDays();

        for(int i = Alarm.SUN; i <= Alarm.SAT; i++) {
            if(days.get(i)) {
                return false;
            }
        }
        return true;
    }

    public static long[] getVibratePattern() {
        return new long[] {100,1000,100,500,100,1000};
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
