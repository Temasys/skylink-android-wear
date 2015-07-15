package skylinkwear.temasys.com.sg;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

/**
 * Displays sensor information
 * Created by janidu on 8/7/15.
 */
public class SensorFragmentPagerAdapter extends FragmentGridPagerAdapter {

    private HealthSensorFragment healthSensorFragment;
    private GyroscopeSensorFragment gyroscopeFragment;

    public SensorFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getFragment(int row, int column) {
        Fragment fragment = null;
        switch (column) {
            case 0:
                fragment = healthSensorFragment;
                break;
            case 1:
                fragment = gyroscopeFragment;
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int row) {
        return 2;
    }

    public void setHealthSensorFragment(HealthSensorFragment healthSensorFragment) {
        this.healthSensorFragment = healthSensorFragment;
    }

    public void setGyroscopeFragment(GyroscopeSensorFragment gyroscopeFragment) {
        this.gyroscopeFragment = gyroscopeFragment;
    }
}