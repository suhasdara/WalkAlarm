package com.suhasdara.walkalarm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.SparseBooleanArray;

import com.suhasdara.walkalarm.model.Alarm;

import java.util.ArrayList;

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

    public void insertAlarm(Alarm alarm) {
        //New value for row
        ContentValues values = new ContentValues();
        values.put(AlarmContract.TIME, alarm.getTime());
        values.put(AlarmContract.NAME, alarm.getName());
        values.put(AlarmContract.DAYS, Alarm.convertArrayToString(alarm.getDays()));
        values.put(AlarmContract.ENABLED, alarm.isEnabled() ? 1 : 0);

        long id = getWritableDatabase().insert(AlarmContract.TABLE_NAME, null, values);
        alarm.setId(id);
    }

    public void updateAlarm(Alarm alarm) {
        //New value for row
        ContentValues values = new ContentValues();
        values.put(AlarmContract.TIME, alarm.getTime());
        values.put(AlarmContract.NAME, alarm.getName());
        values.put(AlarmContract.DAYS, Alarm.convertArrayToString(alarm.getDays()));
        values.put(AlarmContract.ENABLED, alarm.isEnabled() ? 1 : 0);

        String whereClause = "_id="+alarm.getId();

        getWritableDatabase().update(AlarmContract.TABLE_NAME, values, whereClause, null);
    }

    public ArrayList<Alarm> getAlarms() {
        Cursor c = getReadableDatabase().query(AlarmContract.TABLE_NAME, null, null, null, null, null, null);
        if(c == null) {
            return new ArrayList<>();
        }

        ArrayList<Alarm> alarms = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                long id = c.getLong(c.getColumnIndex(AlarmContract._ID));
                long time = c.getLong(c.getColumnIndex(AlarmContract.TIME));
                String name = c.getString(c.getColumnIndex(AlarmContract.NAME));
                SparseBooleanArray days = Alarm.convertStringToBoolArray(c.getString(c.getColumnIndex(AlarmContract.DAYS)));
                boolean enabled = c.getInt(c.getColumnIndex(AlarmContract.ENABLED)) == 1;

                alarms.add(new Alarm(id, time, name, days, enabled));
            } while(c.moveToNext());
        }
        c.close();

        return alarms;
    }
}
