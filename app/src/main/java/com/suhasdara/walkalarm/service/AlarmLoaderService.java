package com.suhasdara.walkalarm.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.suhasdara.walkalarm.database.AlarmDatabaseHelper;
import com.suhasdara.walkalarm.model.Alarm;

import java.util.ArrayList;

public class AlarmLoaderService extends IntentService {
    private static final String TAG = AlarmLoaderService.class.getSimpleName();
    public static final String ACTION_COMPLETE = TAG + ".ACTION_COMPLETE";
    public static final String ALARMS = "com.suhasdara.walkalarm.service.AlarmLoaderService.ALARM";

    @SuppressWarnings("unused")
    public AlarmLoaderService() {
        this(TAG);
    }

    public AlarmLoaderService(String name){
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<Alarm> alarms = AlarmDatabaseHelper.getInstance(this).getAlarms();

        Intent i = new Intent(ACTION_COMPLETE);
        i.putParcelableArrayListExtra(ALARMS, alarms);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    public static void launchService(Context context) {
        final Intent intent = new Intent(context, AlarmLoaderService.class);
        context.startService(intent);
    }
}
