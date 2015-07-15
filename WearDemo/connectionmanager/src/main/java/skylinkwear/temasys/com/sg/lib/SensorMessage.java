package skylinkwear.temasys.com.sg.lib;

/**
 * Contains information obtained by sensors
 * <p/>
 * Created by janidu on 3/7/15.
 */
public class SensorMessage {

    private int heartRate;
    private int stepCount;
    private float gyroscopeX;
    private float gyroscopeY;
    private float gyroscopeZ;

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public float getGyroscopeX() {
        return gyroscopeX;
    }

    public void setGyroscopeX(float gyroscopeX) {
        this.gyroscopeX = gyroscopeX;
    }

    public float getGyroscopeY() {
        return gyroscopeY;
    }

    public void setGyroscopeY(float gyroscopeY) {
        this.gyroscopeY = gyroscopeY;
    }

    public float getGyroscopeZ() {
        return gyroscopeZ;
    }

    public void setGyroscopeZ(float gyroscopeZ) {
        this.gyroscopeZ = gyroscopeZ;
    }
}
