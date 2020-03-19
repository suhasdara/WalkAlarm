package com.suhasdara.walkalarm.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;
import android.text.Html;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.suhasdara.walkalarm.R;
import com.suhasdara.walkalarm.model.Alarm;
import com.suhasdara.walkalarm.utils.AlarmUtils;

import java.util.Calendar;

public class AlarmView extends LinearLayout {

    private TextView time;
    private String timeText;
    private TextView days;
    private SparseBooleanArray daysBool;
    private Switch enabler;
    private boolean enabled;

    private static final String[] DAY_LETTERS = {"S", "M", "T", "W", "T", "F", "S"};

    public AlarmView(Context context) {
        super(context);
        inflate(context, R.layout.alarm_view, this);
        initComponents();
        setTime();
        setDays();
        setEnabler();
    }

    public AlarmView(Context context, Alarm alarm) {
        super(context);
        inflate(context, R.layout.alarm_view, this);
        initComponents();

        timeText = AlarmUtils.getTextTime(alarm);
        daysBool = alarm.getDays();
        enabled = alarm.isEnabled();
        setTime();
        setDays();
        setEnabler();
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AlarmView(Context context, String timeText, String daysString, boolean enabled) {
        super(context);
        inflate(context, R.layout.alarm_view, this);
        initComponents();
        this.timeText = timeText;
        assert(daysString != null);
        this.daysBool = Alarm.convertStringToBoolArray(daysString);
        this.enabled = enabled;
        setTime();
        setDays();
        setEnabler();
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.alarm_view, this);

        initComponents();

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.AlarmView);
        try{
            for(int i = 0; i < arr.getIndexCount(); i++) {
                int attr = arr.getIndex(i);

                if(attr == R.styleable.AlarmView_timeText) {
                    timeText = arr.getString(attr);
                    setTime();
                } else if(attr == R.styleable.AlarmView_daysContent) {
                    String daysString = arr.getString(attr);
                    if(daysString != null) {
                        daysBool = Alarm.convertStringToBoolArray(daysString);
                    }
                    setDays();
                } else if(attr == R.styleable.AlarmView_enabled) {
                    enabled = arr.getBoolean(attr, false);
                    setEnabler();
                }
            }
        } finally {
            arr.recycle();
        }
    }

    private void initComponents() {
        time = findViewById(R.id.time_text);
        days = findViewById(R.id.days_text);
        enabler = findViewById(R.id.enabler);
    }

    public void setData(Alarm alarm) {
        timeText = AlarmUtils.getTextTime(alarm);
        daysBool = alarm.getDays();
        enabled = alarm.isEnabled();
        setTime();
        setDays();
        setEnabler();
    }

    private void setTime() {
        if(timeText == null) {
            timeText = getResources().getString(R.string.default_time);
        }
        time.setText(timeText);
    }

    private void setDays() {
        if(daysBool == null) {
            daysBool = Alarm.buildBaseDaysArray();
        }

        StringBuilder styleText = new StringBuilder();

        for(int i = 0; i < daysBool.size(); i++) {
            boolean day = daysBool.get(i);
            String day_l = DAY_LETTERS[i];

            if(day) {
                styleText.append("<font color=#3F51B5>");
            } else {
                styleText.append("<font color=#888888>");
            }
            styleText.append(day_l);
            styleText.append("</font> ");
        }
        styleText.deleteCharAt(styleText.length() - 1);

        days.setText(Html.fromHtml(styleText.toString()));
    }

    private void setEnabler() {
        enabler.setChecked(enabled);
    }

    public Switch getEnablerComponent() {
        return enabler;
    }
}
