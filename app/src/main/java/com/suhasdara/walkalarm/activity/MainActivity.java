package com.suhasdara.walkalarm.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suhasdara.walkalarm.R;
import com.suhasdara.walkalarm.component.AlarmView;
import com.suhasdara.walkalarm.database.AlarmDatabaseHelper;
import com.suhasdara.walkalarm.model.Alarm;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AlarmDatabaseHelper database;

    private FloatingActionButton addAlarm;
    private LinearLayout alarmsCon;
    private TextView curAlarm;

    private ArrayList<Alarm> alarms;

    public final static int ALARM_EDIT_ACTIVITY_REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new AlarmDatabaseHelper(this);
//        ((LinearLayout) findViewById(R.id.alarm_box)).addView(new AlarmView(this, "12:20", "false_false_true_true_false_true_false", true));

        addAlarm = findViewById(R.id.fab_add);
        alarmsCon = findViewById(R.id.alarm_box);
        curAlarm = findViewById(R.id.cur_alarm);

        setAddListeners();

        alarms = database.getAlarms();
        refreshAlarmViews();
    }

    private void setAddListeners() {
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(null);
            }
        });
    }

    private void sendMessage(Alarm alarm) {
        if(alarm == null) {
            Intent intent = new Intent(this, AlarmEditActivity.class);
            startActivityForResult(intent, ALARM_EDIT_ACTIVITY_REQUEST_CODE);
        } else {
            Intent intent = new Intent(this, AlarmEditActivity.class);
            intent.putExtra(AlarmEditActivity.ALARM, alarm);
            startActivityForResult(intent, ALARM_EDIT_ACTIVITY_REQUEST_CODE);
        }
    }

    private void refreshAlarmViews() {
        alarmsCon.removeAllViews();
        for(final Alarm a : alarms) {
            AlarmView alarm = new AlarmView(this, a);
            alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage(a);
                }
            });
            alarmsCon.addView(alarm);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ALARM_EDIT_ACTIVITY_REQUEST_CODE && data != null) {
            Alarm alarm = data.getParcelableExtra(AlarmEditActivity.ALARM);
            if(alarm.getId() == Alarm.NO_ID) {
                alarm.insertInDatabase(database);
                alarms.add(alarm);
            } else {
                alarm.updateInDatabase(database);
                for(int i = 0; i < alarms.size(); i++) {
                    Alarm a = alarms.get(i);
                    if(a.getId() == alarm.getId()) {
                        alarms.set(i, alarm);
                        break;
                    }
                }
            }
            refreshAlarmViews();
        }
    }
}
