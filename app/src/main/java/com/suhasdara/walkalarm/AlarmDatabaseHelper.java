package com.suhasdara.walkalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class AlarmDatabaseHelper extends SQLiteOpenHelper {
    AlarmDatabaseHelper(Context context) {
        super(context, AlarmContract.TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AlarmContract.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int prevVersion, int newVersion) { /* Do nothing */ }

    void updateQuery(long id, String column, String value) {
        //New value for row
        ContentValues values = new ContentValues();
        values.put(column, value);

        String whereClause = "_id="+id;

        getWritableDatabase().update(AlarmContract.TABLE_NAME, values, whereClause, null);
    }

    void updateQuery(long id, String column, long value) {
        //New value for row
        ContentValues values = new ContentValues();
        values.put(column, value);

        String whereClause = "_id="+id;

        getWritableDatabase().update(AlarmContract.TABLE_NAME, values, whereClause, null);
    }
}
