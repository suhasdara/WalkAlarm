/* Credit: https://github.com/PPartisan/Simple-Alarms/ */

package com.suhasdara.walkalarm;

import java.util.Arrays;

public class Alarm {
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

    void setTime(long time) {
        this.time = time;
    }

    long getTime() {
        return time;
    }

    void setLabel(String label) {
        this.label = label;
    }

    String getLabel() {
        return label;
    }

    void setDay(int day, boolean isAlarmed) {
        allDays[day] = isAlarmed;
    }

    boolean[] getDays() {
        return allDays;
    }

    boolean getDay(int day){
        return allDays[day];
    }

    void turnOn() {
        this.isEnabled = true;
    }

    void turnOff() {
        this.isEnabled = false;
    }

    boolean isEnabled() {
        return isEnabled;
    }

    public String toString() {
        return id + "\n" +
               time + "\n" +
               label + "\n" +
               Arrays.toString(allDays) + "\n" +
               isEnabled + "\n";
    }

    private static boolean[] buildDaysArray(int... days) {
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
}
