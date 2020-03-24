package com.suhasdara.walkalarm.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.suhasdara.walkalarm.R;
import com.suhasdara.walkalarm.movementdetection.StepDetector;
import com.suhasdara.walkalarm.utils.AlarmUtils;

public class AlarmLandingActivity extends AppCompatActivity implements SensorEventListener, StepDetector.StepListener {
    private Button dismissButton;
    private TextView dismissText;
    private TextView counterText;

    private Vibrator vibrator;

    private StepDetector stepDetector;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private int numSteps;
    private static final int NUM_STEPS = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_landing);

        dismissButton = findViewById(R.id.dismiss);
        dismissText = findViewById(R.id.target);
        counterText = findViewById(R.id.counter);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if(Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createWaveform(AlarmUtils.getVibratePattern(), 0));
        } else {
            vibrator.vibrate(AlarmUtils.getVibratePattern(), 0);
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepDetector = new StepDetector();
        stepDetector.registerListener(this);

        setDismissButtonListeners();
    }

    private void setDismissButtonListeners() {
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.cancel();
                finish();
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            stepDetector.updateAcc(event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        counterText.setText(numSteps + "");

        if(numSteps >= NUM_STEPS) {
            sensorManager.unregisterListener(this);
            dismissText.setText(R.string.target_dismiss);
            dismissButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onBackPressed() {}
}
