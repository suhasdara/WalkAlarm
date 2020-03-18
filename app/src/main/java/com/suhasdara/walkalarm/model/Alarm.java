/* Credit for template: https://github.com/PPartisan/Simple-Alarms/ */

package com.suhasdara.walkalarm.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;

import com.suhasdara.walkalarm.database.AlarmDatabaseHelper;

public class Alarm implements Parcelable {
    private long id;
    private long time;
    private String name;
    private SparseBooleanArray days;
    private boolean enabled;

    public static final int NUM_DAYS = 7;
    public static final int SUN = 0;
    public static final int MON = 1;
    public static final int TUES = 2;
    public static final int WED = 3;
    public static final int THURS = 4;
    public static final int FRI = 5;
    public static final int SAT = 6;

    public static final int NO_ID = -1;

    private Alarm(Parcel in) {
        id = in.readLong();
        time = in.readLong();
        name = in.readString();
        days = in.readSparseBooleanArray();
        enabled = in.readByte() != 0;
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel source) {
            return new Alarm(source);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(time);
        parcel.writeString(name);
        parcel.writeSparseBooleanArray(days);
        parcel.writeByte((byte) (enabled ? 1 : 0));
    }

    public Alarm(long time, String name, int... days) {
        this(time, name, buildDaysArray(days), false);
    }

    public Alarm(long time, String name, SparseBooleanArray days) {
        this(time, name, days, false);
    }

    public Alarm(long time, String name, SparseBooleanArray days, boolean enabled) {
        this.time = time;
        this.name = name;
        this.days = days;
        this.enabled = enabled;
        this.id = NO_ID;
    }

    public Alarm(long id, long time, String name, SparseBooleanArray days, boolean enabled) {
        this(time, name, days, enabled);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDays(SparseBooleanArray days) {
        this.days = days;
    }

    public SparseBooleanArray getDays() {
        return days;
    }

    public void setDay(int day, boolean isAlarmed) {
        days.put(day, isAlarmed);
    }

    public boolean getDay(int day){
        return days.get(day);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void updateInDatabase(AlarmDatabaseHelper db) {
        if(id != NO_ID) {
            db.updateAlarm(this);
        } else {
            insertInDatabase(db);
        }
    }

    public void insertInDatabase(AlarmDatabaseHelper db) {
        db.insertAlarm(this);
    }

    @Override
    public String toString() {
        return id + "\n" +
               time + "\n" +
               name + "\n" +
               days + "\n" +
               enabled + "\n";
    }

    private static SparseBooleanArray buildDaysArray(@NonNull int... days) {
        final SparseBooleanArray array = buildBaseDaysArray();

        for (int day : days) {
            array.put(day, true);
        }

        return array;
    }

    public static SparseBooleanArray buildBaseDaysArray() {
        final SparseBooleanArray array = new SparseBooleanArray();

        for(int i = SUN; i <= SAT; i++) {
            array.put(i, false);
        }

        return array;
    }

    private static final String strSeparator = "_";

    @NonNull
    public static String convertArrayToString(@NonNull SparseBooleanArray arr) {
        StringBuilder str = new StringBuilder();
        str.append(arr.get(SUN));
        for (int i = 1; i < arr.size(); i++) {
            str.append(strSeparator);
            str.append(arr.get(i));
        }

        return str.toString();
    }

    @NonNull
    public static SparseBooleanArray convertStringToBoolArray(@NonNull String str) {
        String[] arr = str.split(strSeparator);
        SparseBooleanArray res = buildBaseDaysArray();

        for(int i = 0; i < arr.length; i++) {
            res.put(i, Boolean.parseBoolean(arr[i]));
        }

        return res;
    }
}
