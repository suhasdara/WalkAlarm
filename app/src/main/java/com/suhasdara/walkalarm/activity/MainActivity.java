package com.suhasdara.walkalarm.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suhasdara.walkalarm.R;
import com.suhasdara.walkalarm.adapter.AlarmAdapter;
import com.suhasdara.walkalarm.component.AlarmView;
import com.suhasdara.walkalarm.database.AlarmDatabaseHelper;
import com.suhasdara.walkalarm.model.Alarm;
import com.suhasdara.walkalarm.service.AlarmLoaderReceiver;
import com.suhasdara.walkalarm.service.AlarmLoaderService;
import com.suhasdara.walkalarm.utils.AlarmUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AlarmLoaderReceiver.OnAlarmsLoadedListener {
    private FloatingActionButton addAlarm;
    private RecyclerView alarmsCon;
    private TextView curAlarm;

    private AlarmAdapter adapter;
    private AlarmLoaderReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiver = new AlarmLoaderReceiver(this);
        adapter = new AlarmAdapter();

        addAlarm = findViewById(R.id.fab_add);
        alarmsCon = findViewById(R.id.alarm_box);
        curAlarm = findViewById(R.id.cur_alarm);

        alarmsCon.setAdapter(adapter);
        alarmsCon.setLayoutManager(new LinearLayoutManager(this));
        setAddListeners();
    }

    private void setAddListeners() {
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmUtils.checkAlarmPermissions(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, AlarmEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter(AlarmLoaderService.ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        AlarmLoaderService.launchService(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onAlarmsLoaded(ArrayList<Alarm> alarms) {
        adapter.setAlarms(alarms);
    }
}
