package skylinkwear.temasys.com.sg;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Displays heart sensor and step sensor information
 * Created by janidu on 8/7/15.
 */
public class HealthSensorFragment extends Fragment {

    private TextView txtHeartRate;
    private TextView txtStepCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.sensor_health, container, false);
        txtHeartRate = (TextView) layout.findViewById(R.id.txtHeartRate);
        txtStepCounter = (TextView) layout.findViewById(R.id.txtStepCount);
        return layout;
    }

    public void setHeartRate(String heartRate) {
        if (txtHeartRate != null) {
            txtHeartRate.setText(heartRate);
        }
    }

    public void setStepCounter(String stepCounter) {
        if (txtStepCounter != null) {
            txtStepCounter.setText(stepCounter);
        }
    }
}
