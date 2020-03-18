package com.suhasdara.walkalarm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.suhasdara.walkalarm.R;
import com.suhasdara.walkalarm.model.Alarm;

import java.util.Calendar;

public class AlarmEditActivity extends AppCompatActivity {
    public static final String ALARM = "com.suhasdara.walkalarm.ALARM";

    private Button saveButton;
    private ToggleButton[] dayButtons;
    private EditText alarmName;
    private TimePicker timePicker;

    private long alarm_id;

    private int default_background_color;
    private int default_text_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        alarm_id = Alarm.NO_ID;

        saveButton = findViewById(R.id.save);

        dayButtons = new ToggleButton[Alarm.NUM_DAYS];
        dayButtons[Alarm.SUN] = findViewById(R.id.sunday);
        dayButtons[Alarm.MON] = findViewById(R.id.monday);
        dayButtons[Alarm.TUES] = findViewById(R.id.tuesday);
        dayButtons[Alarm.WED] = findViewById(R.id.wednesday);
        dayButtons[Alarm.THURS] = findViewById(R.id.thursday);
        dayButtons[Alarm.FRI] = findViewById(R.id.friday);
        dayButtons[Alarm.SAT] = findViewById(R.id.saturday);

        default_background_color = 0xfafafa;
        default_text_color = dayButtons[Alarm.SUN].getCurrentTextColor();

        alarmName = findViewById(R.id.alarm_name);
        timePicker = findViewById(R.id.time_edit);

        setSaveListeners();
        setDayButtonListeners();

        Intent intent = getIntent();
        Alarm alarm = intent.getParcelableExtra(ALARM);
        if(alarm != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(alarm.getTime());
            if(Build.VERSION.SDK_INT < 23) {
                timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
            } else {
                timePicker.setHour(cal.get(Calendar.HOUR_OF_DAY));
                timePicker.setMinute(cal.get(Calendar.MINUTE));
            }
            alarmName.setText(alarm.getName());

            alarm_id = alarm.getId();

            for(int i = Alarm.SUN; i <= Alarm.SAT; i++) {
                dayButtons[i].setChecked(alarm.getDays().get(i));
            }
        }
    }

    private void setSaveListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });
    }

    private void setDayButtonListeners() {
        for(ToggleButton button : dayButtons) {
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        buttonView.setBackgroundColor(Color.BLUE);
                        buttonView.setTextColor(Color.WHITE);
                    } else {
                        buttonView.setBackgroundColor(default_background_color);
                        buttonView.setTextColor(default_text_color);
                    }
                }
            });
        }
    }

    private long getMillisTime() {
        int hour, minute;
        if(Build.VERSION.SDK_INT < 23) {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        } else {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        }

        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);

        boolean noneChecked = true;
        for(int i = Alarm.SUN; i <= Alarm.SAT; i++) {
            if(dayButtons[i].isChecked()) {
                noneChecked = false;
                break;
            }
        }

        if(noneChecked) {
            Calendar calComp = Calendar.getInstance();
            if(cal.compareTo(calComp) <= 0) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else {
            int day = dayOfWeek;
            int iteration = 0;
            while(iteration <= Alarm.NUM_DAYS) {
                if(dayButtons[day].isChecked()) {
                    Calendar calComp = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, iteration);
                    if(cal.compareTo(calComp) > 0) {
                        break;
                    } else {
                        cal.add(Calendar.DAY_OF_MONTH, -iteration);
                    }
                }

                day = (dayOfWeek + 1) % Alarm.NUM_DAYS;
                iteration++;
            }
        }

        return cal.getTimeInMillis();
    }

    private Alarm getCurrentAlarm() {
        SparseBooleanArray days = Alarm.buildBaseDaysArray();
        for(int i = Alarm.SUN; i <= Alarm.SAT; i++) {
            if(dayButtons[i].isChecked()) {
                days.put(i, true);
            }
        }

        String name = alarmName.getText().toString();
        long time = getMillisTime();
        long id = alarm_id;
        alarm_id = Alarm.NO_ID;

        return new Alarm(id, time, name, days, true);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ALARM, getCurrentAlarm());
        setResult(MainActivity.ALARM_EDIT_ACTIVITY_REQUEST_CODE, intent);
        finish();
    }
}
