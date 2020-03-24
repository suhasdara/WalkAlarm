package com.suhasdara.walkalarm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.suhasdara.walkalarm.database.AlarmDatabaseHelper;
import com.suhasdara.walkalarm.model.Alarm;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class SystemBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            final Context c = context;
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    ArrayList<Alarm> alarms = AlarmDatabaseHelper.getInstance(c).getAlarms();
                    AlarmReceiver.scheduleAlarms(c, alarms);
                }
            });
        }
    }
}
