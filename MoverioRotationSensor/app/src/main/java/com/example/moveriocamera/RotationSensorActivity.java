package com.example.moveriocamera;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.epson.moverio.hardware.sensor.SensorData;
import com.epson.moverio.hardware.sensor.SensorDataListener;
import com.epson.moverio.hardware.sensor.SensorManager;

import java.io.IOException;

public class RotationSensorActivity extends Activity implements SensorDataListener {

    private SensorManager mSensorManager;
    private TextView mTextView_rvResult = null;
    private float[] values = new float[3];
    private float[] rotations = new float[9];
    private static float yawDegrees, pitchDegrees, rollDegrees;
    private float[] tempRotations = new float[9];
    private static final String TAG = RotationSensorActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation_sensor2);
        mTextView_rvResult = findViewById(R.id.textView_rvResult);
        mSensorManager = new SensorManager(this);
        Log.d(TAG, "oncreate");

    }

    @Override
    public void onSensorDataChanged(SensorData data) {

        android.hardware.SensorManager.getRotationMatrixFromVector(rotations, data.values);
        android.hardware.SensorManager.remapCoordinateSystem(rotations, android.hardware.SensorManager.AXIS_Y, android.hardware.SensorManager.AXIS_X, tempRotations);
        rotations = tempRotations;
        android.hardware.SensorManager.getOrientation(rotations, values);

        yawDegrees = (float) Math.toDegrees((double) values[0]);
        pitchDegrees = (float) Math.toDegrees((double) values[1]);
        rollDegrees = (float) Math.toDegrees((double) values[2]);
        Log.d(TAG, "onSensordataChanged " + yawDegrees + " " + pitchDegrees + " " + rollDegrees + " " + getSensorData());
        mTextView_rvResult.setText(getSensorData());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.close(this);
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        try {
            mSensorManager.open(SensorManager.TYPE_ROTATION_VECTOR, this);
        } catch (IOException e) {
            Log.d(TAG, "onResume " + e.getMessage());
        }
    }

    public static String getSensorData() {
        return yawDegrees + " " + pitchDegrees + " " + rollDegrees;
    }
}