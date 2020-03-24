package com.suhasdara.walkalarm.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.suhasdara.walkalarm.R;
import com.suhasdara.walkalarm.activity.AlarmLandingActivity;
import com.suhasdara.walkalarm.database.AlarmDatabaseHelper;
import com.suhasdara.walkalarm.model.Alarm;
import com.suhasdara.walkalarm.utils.AlarmUtils;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {

    private final static String ALARM_BUNDLE = "com.suhasdara.walkalarm.service.AlarmReceiver.ALARM_BUNDLE";
    private final static String ALARM = "com.suhasdara.walkalarm.service.AlarmReceiver.ALARM";
    private final static String CHANNEL_ID = "walkalarm_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        Alarm alarm = intent.getBundleExtra(ALARM_BUNDLE).getParcelable(ALARM);
        NotificationManager notifMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(alarm.getName())
                .setTicker(alarm.getName())
                .setVibrate(AlarmUtils.getVibratePattern())
                .setContentIntent(launchLandingPage(context, alarm))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH);

        notifMgr.notify(alarm.getNotifId(), builder.build());
        scheduleNextAlarm(context, alarm);
    }

    public static void scheduleAlarm(Context context, Alarm alarm) {
        long curTime = System.currentTimeMillis();
        if(alarm.getTime() < curTime) {
            alarm.setTime(AlarmUtils.getNextAlarmTime(alarm));
            AlarmDatabaseHelper.getInstance(context).updateAlarm(alarm);
            AlarmLoaderService.launchService(context);
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ALARM, alarm);
        intent.putExtra(ALARM_BUNDLE, bundle);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, alarm.getNotifId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmScheduler.with(context).schedule(alarm, pIntent);
    }

    public static void scheduleNextAlarm(Context context, Alarm alarm) {
        if(AlarmUtils.isSingleTimeAlarm(alarm)) {
            cancelAlarm(context, alarm);
            alarm.setEnabled(false);
        } else {
            long nextTime = AlarmUtils.getNextAlarmTime(alarm);
            alarm.setTime(nextTime);
            AlarmDatabaseHelper.getInstance(context).updateAlarm(alarm);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(ALARM, alarm);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, alarm.getNotifId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmScheduler.with(context).schedule(alarm, pIntent);
        }

        AlarmDatabaseHelper.getInstance(context).updateAlarm(alarm);
        AlarmLoaderService.launchService(context);
    }

    public static void scheduleAlarms(Context context, ArrayList<Alarm> alarms) {
        for(Alarm alarm : alarms) {
            if(alarm.isEnabled()) {
                scheduleAlarm(context, alarm);
            }
        }
    }

    public static void cancelAlarm(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, alarm.getNotifId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    public static void createNotificationChannel(Context context) {
        if(Build.VERSION.SDK_INT >= 26) {
            NotificationManager notifMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String notifChannelName = context.getString(R.string.notif_channel_name);
            if(notifMgr.getNotificationChannel(notifChannelName) == null) {
                NotificationChannel notifChannel = new NotificationChannel(CHANNEL_ID, notifChannelName, NotificationManager.IMPORTANCE_HIGH);
                notifChannel.enableVibration(true);
                notifChannel.setVibrationPattern(AlarmUtils.getVibratePattern());
                notifChannel.setBypassDnd(true);
                notifMgr.createNotificationChannel(notifChannel);
            }
        }
    }

    private static PendingIntent launchLandingPage(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmLandingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(context, alarm.getNotifId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static class AlarmScheduler {
        private Context context;
        private AlarmManager alarmManager;

        private AlarmScheduler(@NonNull Context context, @NonNull AlarmManager alarmManager) {
            this.context = context;
            this.alarmManager = alarmManager;
        }

        private static AlarmScheduler with(@NonNull Context context) {
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            return new AlarmScheduler(context, manager);
        }

        private void schedule(Alarm alarm, PendingIntent intent) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarm.getTime(), launchLandingPage(context, alarm)), intent);
        }
    }
}
