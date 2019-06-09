package com.suhasdara.walkalarm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.suhasdara.walkalarm.model.Alarm;

public class AlarmDatabaseHelper extends SQLiteOpenHelper {
    public AlarmDatabaseHelper(Context context) {
        super(context, AlarmContract.TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AlarmContract.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int prevVersion, int newVersion) { /* Do nothing */ }

    public void updateAlarm(Alarm alarm) {
        //New value for row
        ContentValues values = new ContentValues();
        values.put(AlarmContract.TIME, alarm.getTime());
        values.put(AlarmContract.LABEL, alarm.getLabel());
        values.put(AlarmContract.DAYS, Alarm.convertArrayToString(alarm.getDays()));
        values.put(AlarmContract.ENABLED, alarm.isEnabled());

        String whereClause = "_id="+alarm.getId();

        getWritableDatabase().update(AlarmContract.TABLE_NAME, values, whereClause, null);
    }
}
