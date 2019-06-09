/* Credit for template: https://github.com/PPartisan/Simple-Alarms/ */

package com.suhasdara.walkalarm.model;

import android.support.annotation.NonNull;

import com.suhasdara.walkalarm.database.AlarmDatabaseHelper;

import java.util.Arrays;

public class Alarm {
    private final long id;
    private long time;
    private String label;
    private boolean[] days;
    private boolean enabled;

    public static final int NUM_DAYS = 7;
    public static final int SUN = 0;
    public static final int MON = 1;
    public static final int TUES = 2;
    public static final int WED = 3;
    public static final int THURS = 4;
    public static final int FRI = 5;
    public static final int SAT = 6;

    public Alarm(long id, long time, String label, int... days) {
        this.id = id;
        this.time = time;
        this.label = label;
        this.days = buildDaysArray(days);
    }

    public Alarm(long id, long time, String label, boolean[] days) {
        this(id, time, label, days, false);
    }

    public Alarm(long id, long time, String label, boolean[] days, boolean enabled) {
        this.id = id;
        this.time = time;
        this.label = label;
        this.days = days;
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setDay(int day, boolean isAlarmed) {
        days[day] = isAlarmed;
    }

    public boolean[] getDays() {
        return days;
    }

    public boolean getDay(int day){
        return days[day];
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void updateInDatabase(AlarmDatabaseHelper db) {
        db.updateAlarm(this);
    }

    @Override
    public String toString() {
        return id + "\n" +
               time + "\n" +
               label + "\n" +
               Arrays.toString(days) + "\n" +
               enabled + "\n";
    }

    private static boolean[] buildDaysArray(@NonNull int... days) {
        final boolean[] array = buildBaseDaysArray();

        for (int day : days) {
            array[day] = true;
        }

        return array;
    }

    private static boolean[] buildBaseDaysArray() {
        final boolean[] array = new boolean[NUM_DAYS];

        array[SUN] = false;
        array[MON] = false;
        array[TUES] = false;
        array[WED] = false;
        array[THURS] = false;
        array[FRI] = false;
        array[SAT] = false;

        return array;
    }

    private static final String strSeparator = "_";

    @NonNull
    public static String convertArrayToString(@NonNull boolean[] arr) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            str.append(arr[i]);
            // Do not append comma at the end of last element
            if(i < arr.length - 1){
                str.append(strSeparator);
            }
        }

        return str.toString();
    }

    @NonNull
    public static boolean[] convertStringToBoolArray(@NonNull String str) {
        String[] arr = str.split(strSeparator);
        boolean[] res = new boolean[arr.length];

        for(int i = 0; i < arr.length; i++) {
            res[i] = Boolean.parseBoolean(arr[i]);
        }

        return res;
    }
}
