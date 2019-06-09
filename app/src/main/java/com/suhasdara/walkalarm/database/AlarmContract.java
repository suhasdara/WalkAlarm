package com.suhasdara.walkalarm.database;

import android.provider.BaseColumns;

class AlarmContract implements BaseColumns {
    private AlarmContract() {}

    static final String TABLE_NAME = "alarms";
    static final String TIME = "time";
    static final String LABEL = "label";
    static final String DAYS = "days";
    static final String ENABLED = "enabled";

    static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME +  " (" +
            _ID + " INTEGER NOT NULL AUTO_INCREMENT, " +
            TIME + " INTEGER, " +
            LABEL + " TEXT, " +
            DAYS + " TEXT, " +
            ENABLED + " TEXT)";

    static final String DROP_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
