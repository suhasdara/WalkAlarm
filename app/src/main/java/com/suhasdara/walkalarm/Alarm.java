/* Credit for template: https://github.com/PPartisan/Simple-Alarms/ */

package com.suhasdara.walkalarm;

import android.support.annotation.NonNull;

import java.util.Arrays;

class Alarm {
    private final long id;
    private long time;
    private String label;
    private boolean[] allDays;
    private boolean isEnabled;

    static final int NUM_DAYS = 7;
    static final int SUN = 0;
    static final int MON = 1;
    static final int TUES = 2;
    static final int WED = 3;
    static final int THURS = 4;
    static final int FRI = 5;
    static final int SAT = 6;

    Alarm(long id, long time, String label, int... days) {
        this.id = id;
        this.time = time;
        this.label = label;
        this.allDays = buildDaysArray(days);
    }

    Alarm(long id, long time, String label, boolean[] days) {
        this(id, time, label, days, false);
    }

    Alarm(long id, long time, String label, boolean[] days, boolean enabled) {
        this.id = id;
        this.time = time;
        this.label = label;
        this.allDays = days;
        this.isEnabled = enabled;
    }

    long getId() {
        return id;
    }

    void setTime(long time, AlarmDatabaseHelper db) {
        this.time = time;
        db.updateQuery(id, AlarmContract.TIME, time);
    }

    long getTime() {
        return time;
    }

    void setLabel(String label, AlarmDatabaseHelper db) {
        this.label = label;
        db.updateQuery(id, AlarmContract.LABEL, label);
    }

    String getLabel() {
        return label;
    }

    void setDay(int day, boolean isAlarmed, AlarmDatabaseHelper db) {
        allDays[day] = isAlarmed;
        db.updateQuery(id, AlarmContract.DAYS, convertArrayToString(allDays));
    }

    boolean[] getDays() {
        return allDays;
    }

    boolean getDay(int day){
        return allDays[day];
    }

    void turnOn(AlarmDatabaseHelper db) {
        this.isEnabled = true;
        db.updateQuery(id, AlarmContract.ENABLED, "" + isEnabled);
    }

    void turnOff(AlarmDatabaseHelper db) {
        this.isEnabled = false;
        db.updateQuery(id, AlarmContract.ENABLED, "" + isEnabled);
    }

    boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String toString() {
        return id + "\n" +
               time + "\n" +
               label + "\n" +
               Arrays.toString(allDays) + "\n" +
               isEnabled + "\n";
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

    private static final String strSeparator = "_,_";

    @NonNull
    private static String convertArrayToString(@NonNull boolean[] arr) {
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

    private static boolean[] convertStringToBoolArray(@NonNull String str) {
        String[] arr = str.split(strSeparator);
        boolean[] res = new boolean[arr.length];

        for(int i = 0; i < arr.length; i++) {
            res[i] = Boolean.parseBoolean(arr[i]);
        }

        return res;
    }
}
