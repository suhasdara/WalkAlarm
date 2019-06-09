package com.suhasdara.walkalarm.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.suhasdara.walkalarm.R;
import com.suhasdara.walkalarm.component.AlarmView;
import com.suhasdara.walkalarm.database.AlarmDatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private AlarmDatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new AlarmDatabaseHelper(this);
        ((LinearLayout) findViewById(R.id.alarm_box)).addView(new AlarmView(this, "11:00", "false_false_false_true_false_false_false", true));
    }
}
