package com.avichai98.farmroad.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.avichai98.farmroad.Interfaces.MoveCallback;

public class MoveDetector {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    private long timestamp = 0l;

    private MoveCallback moveCallback;

    public MoveDetector(Context context, MoveCallback moveCallback) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.moveCallback = moveCallback;
        initEventListener();
    }


    private void initEventListener() {
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float z = event.values[2];
                calculateMove(x, z);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }

    private void calculateMove(float x, float z) {
        if (System.currentTimeMillis() - timestamp > 350){
            timestamp = System.currentTimeMillis();

            if (x > 3){
                if (moveCallback != null){
                    moveCallback.moveLeftX();
                }
            }

            if (x < -3){
                if (moveCallback != null){
                    moveCallback.moveRightX();
                }
            }
            if (z > 8.5){
                if (moveCallback != null){
                    moveCallback.speedUpZ();
                }
            }

            if (z < 4){
                if (moveCallback != null){
                    moveCallback.speedDownZ();
                }
            }
        }
    }


    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop(){
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
