package skylinkwear.temasys.com.sg;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Displays Gyroscope sensor information
 * Created by janidu on 12/7/15.
 */
public class GyroscopeSensorFragment extends Fragment {

    private TextView txtGyroscope;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.sensor_gyroscope, container, false);
        txtGyroscope = (TextView) layout.findViewById(R.id.text_values);
        return layout;
    }

    public void setGyroscopeValue(String gyroscopeValue) {
        if (txtGyroscope != null) {
            txtGyroscope.setText(gyroscopeValue);
        }
    }
}
