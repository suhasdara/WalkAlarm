package com.suhasdara.walkalarm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.suhasdara.walkalarm.model.Alarm;

import java.util.ArrayList;

public class AlarmLoaderReceiver extends BroadcastReceiver {
    private OnAlarmsLoadedListener listener;

    @SuppressWarnings("unused")
    public AlarmLoaderReceiver(){}

    public AlarmLoaderReceiver(OnAlarmsLoadedListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final ArrayList<Alarm> alarms = intent.getParcelableArrayListExtra(AlarmLoaderService.ALARMS);
        listener.onAlarmsLoaded(alarms);
    }

    public void setOnAlarmsLoadedListener(OnAlarmsLoadedListener listener) {
        this.listener = listener;
    }

    public interface OnAlarmsLoadedListener {
        void onAlarmsLoaded(ArrayList<Alarm> alarms);
    }
}
