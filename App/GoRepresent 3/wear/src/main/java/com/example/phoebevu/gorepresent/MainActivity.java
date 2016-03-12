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

import java.util.Random;


public class MainActivity extends Activity implements SensorEventListener{
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Button repbttn;
    private SensorManager mSensorManager;


    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    private String jStr = null;
    private String county = null;
    private String state = null;
    private String voteStr = null;

    public void repView(View view) {
        if (jStr == null) {
            return;
        }
        Intent congressIntent = new Intent(this, CongressView.class);
        congressIntent.putExtra("JSON", jStr);
        congressIntent.putExtra("COUNTY", county);
        congressIntent.putExtra("STATE", state);
        congressIntent.putExtra("VOTE", voteStr);
        startActivity(congressIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        repbttn = (Button) findViewById(R.id.rep_btn);
        if (extras != null) {
//            String zipcode = extras.getString("ZIPCODE");
            jStr = extras.getString("JSON");
            county = extras.getString("COUNTY");
            state = extras.getString("STATE");
            voteStr = extras.getString("VOTE");
            repbttn.setText("Updated");
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
//            Log.d("SHAKE", "sending " + x + " &" + y + " &" + z);



            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speed = Math.abs(x - last_x )/ diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    Log.d("SPEED", "OVER THRESHOLD ");
                    String[] rlatlon = randomLatLon();
                    String rlat = rlatlon[0];
                    String rlon = rlatlon[1];
                    Intent randomZipcode = new Intent(this, WatchToPhoneService.class);
                    randomZipcode.putExtra("RANDOM", "RANDOM" + "|" + rlat + "|" + rlon);
                    repbttn.setText("Randomizing...");
                    startService(randomZipcode);

                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    public String[] randomLatLon() {
        Random random = new Random();
        String[] latlon = new String[2];

        double minLat = 35.0;
        double maxLat = 45.00;
        double minLon = -120.0;
        double maxLon = -90.0;

        double rangeLat = maxLat - minLat;
        double scaledLat = random.nextDouble() * rangeLat;
        double shiftedLat = scaledLat + minLat;
        latlon[0] = String.valueOf(shiftedLat);

        double rangeLon = maxLon - minLon;
        double scaledLon = random.nextDouble() * rangeLon;
        double shiftedLon = scaledLon + minLon;
        latlon[1] = String.valueOf(shiftedLon);
        return latlon;
    }
}
