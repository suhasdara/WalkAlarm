package com.suhasdara.walkalarm.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.suhasdara.walkalarm.R;
import com.suhasdara.walkalarm.model.Alarm;

public class AlarmView extends LinearLayout {

    private TextView time;
    private TextView days;
    private Switch enabler;

    private static final String[] DAY_LETTERS = {"S", "M", "T", "W", "T", "F", "S"};

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AlarmView(Context context, String time_text, String days_text, boolean enabler) {
        super(context);
        inflate(context, R.layout.alarm_view, this);
        initComponents();
        setTime(time_text);
        setDays(days_text);
        setEnabler(enabler);
    }

    private void init(Context context, AttributeSet attrs) {
        @StyleableRes
        int index0 = 0;
        @StyleableRes
        int index1 = 1;
        @StyleableRes
        int index2 = 2;

        inflate(context, R.layout.alarm_view, this);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.AlarmView);
        CharSequence timeText = arr.getText(index2);
        CharSequence daysText = arr.getText(index0);
        boolean enabled = arr.getBoolean(index1, false);
        arr.recycle();

        initComponents();

        setTime(timeText);
        setDays(daysText);
        setEnabler(enabled);
    }

    private void initComponents() {
        time = findViewById(R.id.time_text);
        days = findViewById(R.id.days_text);
        enabler = findViewById(R.id.enabler);
    }

    public void setTime(CharSequence timeText) {
        time.setText(timeText);
    }

    public void setDays(CharSequence daysText) {
        boolean[] days_bool = Alarm.convertStringToBoolArray(daysText.toString());
        StringBuilder styleText = new StringBuilder();

        for(int i = 0; i < days_bool.length; i++) {
            boolean day = days_bool[i];
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

    public void setEnabler(boolean enabled) {
        enabler.setChecked(enabled);
    }
}
