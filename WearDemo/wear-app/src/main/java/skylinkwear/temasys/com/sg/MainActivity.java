package skylinkwear.temasys.com.sg;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.squareup.otto.Bus;

import skylinkwear.temasys.com.sg.lib.MessagingConstants;
import skylinkwear.temasys.com.sg.lib.SensorMessage;
import skylinkwear.temasys.com.sg.lib.WearMessagingService;

public class MainActivity extends Activity implements SensorEventListener {

    private final String TAG = MainActivity.class.getName();

    private SensorManager sensorManager;
    private WearMessagingService wearMessagingService;
    private Bus bus;
    private HealthSensorFragment healthSensorFragment;
    private GyroscopeSensorFragment gyroscopeSensorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(skylinkwear.temasys.com.sg.R.layout.activity_main);

        // Keep the Wear screen always on (for testing only!)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);

                SensorFragmentPagerAdapter adapter =
                        new SensorFragmentPagerAdapter(getFragmentManager());
                adapter.setHealthSensorFragment(healthSensorFragment);
                adapter.setGyroscopeFragment(gyroscopeSensorFragment);

                pager.setAdapter(adapter);

                DotsPageIndicator indicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
                indicator.setPager(pager);
            }
        });

        bus = new Bus();

        sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));

        // Register heart rate sensor_health
        Sensor heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        Sensor stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

        healthSensorFragment = new HealthSensorFragment();
        gyroscopeSensorFragment = new GyroscopeSensorFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (wearMessagingService == null) {
            wearMessagingService = WearMessagingService.getInstance();
            wearMessagingService.init(bus, new Gson(), this);
        }
        wearMessagingService.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
        wearMessagingService.disconnect();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged" + event);

        // If sensor is unreliable, then just return
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        SensorMessage sensorMessage = new SensorMessage();

        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {

            sensorMessage.setHeartRate((int) event.values[0]);

            if (sensorMessage.getHeartRate() != 0) {
                // Update UI on watch face
                String msg = "" + sensorMessage.getHeartRate();
                healthSensorFragment.setHeartRate(msg);
                Log.d(TAG, "Heart Rate " + msg);
            }

        } else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            sensorMessage.setStepCount((int) event.values[0]);
            if (sensorMessage.getStepCount() != 0) {
                // Update UI on watch face
                String msg = "" + sensorMessage.getStepCount();
                Log.d(TAG, "Step count " + msg);
                healthSensorFragment.setStepCounter(msg);
            }
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            sensorMessage.setGyroscopeX(event.values[0]);
            sensorMessage.setGyroscopeY(event.values[1]);
            sensorMessage.setGyroscopeZ(event.values[2]);

            String msg = "x = " + Float.toString(sensorMessage.getGyroscopeX()) + "\n" +
                    "y = " + Float.toString(sensorMessage.getGyroscopeY()) + "\n" +
                    "z = " + Float.toString(sensorMessage.getGyroscopeZ()) + "\n";

            // Update UI on watch face
            Log.d(TAG, "Gyroscope value " + msg);
            gyroscopeSensorFragment.setGyroscopeValue(msg);
        }

        if (wearMessagingService != null) {
            // Send information to the handheld
            wearMessagingService.sendMessage(sensorMessage, MessagingConstants.MESSAGE_PATH);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged" + accuracy);
    }
}
