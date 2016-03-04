package com.example.phoebevu.gorepresent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements SensorEventListener{
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Button repbttn;
    private SensorManager mSensorManager;


    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    public void repView(View view) {
        Intent congressIntent = new Intent(this, CongressView.class);
        startActivity(congressIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        repbttn = (Button) findViewById(R.id.rep_btn);
        if (extras != null) {
            String zipcode = extras.getString("ZIPCODE");
            repbttn.setText("See " + zipcode);
        }

//        Intent shakeIntent = new Intent(this, SensorActivity.class);
//        startActivity(shakeIntent);


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            long curTime = System.currentTimeMillis();
            Log.d("SHAKE", "sending " + x + " &" + y + " &" + z);



            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speed = Math.abs(x - last_x )/ diffTime * 10000;
                Log.d("INFO SPEED", ":::   " + speed);
                if (speed > SHAKE_THRESHOLD) {
                    Log.d("SPEED", "OVER THRESHOLD ");
                    Intent randomZipcode = new Intent(this, WatchToPhoneService.class);
//                    startActivity(randomZipcode);
                    randomZipcode.putExtra("RANDOM", "90000");
                    repbttn.setText("See " + "9000");
                    startService(randomZipcode);

                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }
}
