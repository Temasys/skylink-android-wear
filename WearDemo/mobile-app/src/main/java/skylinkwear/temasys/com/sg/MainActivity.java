package skylinkwear.temasys.com.sg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import sg.com.temasys.skylink.sdk.config.SkylinkConfig;
import sg.com.temasys.skylink.sdk.listener.LifeCycleListener;
import sg.com.temasys.skylink.sdk.listener.MessagesListener;
import sg.com.temasys.skylink.sdk.listener.RemotePeerListener;
import sg.com.temasys.skylink.sdk.rtc.SkylinkConnection;
import sg.com.temasys.skylink.sdk.rtc.SkylinkException;
import skylinkwear.temasys.com.sg.lib.SensorMessage;


public class MainActivity extends AppCompatActivity
        implements LifeCycleListener, MessagesListener, RemotePeerListener {

    private static final String TAG = MainActivity.class.getName();

    private static final String API_KEY = "";
    private static final String SECRET = "";
    private static final String USER_NAME = "Handheld Device";
    private static final String ROOM_NAME = "MY_DEFAULT_ROOM";

    private SkylinkConnection skylinkConnection;
    private boolean connectedToRoom;
    private int peerCount;
    private TextView txtHeartRate;
    private TextView txtStepCount;
    private TextView txtGyroscope;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MessagingService.getInstance().getBus().register(this);

        // initialize Skylink Config
        final SkylinkConfig skylinkConfig = new SkylinkConfig();
        skylinkConfig.setAudioVideoSendConfig(SkylinkConfig.AudioVideoConfig.NO_AUDIO_NO_VIDEO);
        skylinkConfig.setAudioVideoReceiveConfig(SkylinkConfig.AudioVideoConfig.NO_AUDIO_NO_VIDEO);
        skylinkConfig.setHasPeerMessaging(true);

        txtHeartRate = (TextView) findViewById(R.id.txt_heart_rate);
        txtStepCount = (TextView) findViewById(R.id.txt_step_count);
        txtGyroscope = (TextView) findViewById(R.id.txt_gyroscope);

        gson = new Gson();

        final Button joinRoomBtn = (Button) findViewById(R.id.btn_join_room);
        joinRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectedToRoom) {

                    // Initialize Skylink Connection
                    skylinkConnection = SkylinkConnection.getInstance();
                    skylinkConnection.init(API_KEY, skylinkConfig, MainActivity.this);
                    skylinkConnection.setLifeCycleListener(MainActivity.this);
                    skylinkConnection.setMessagesListener(MainActivity.this);
                    skylinkConnection.setRemotePeerListener(MainActivity.this);

                    // Connect to room
                    Toast.makeText(MainActivity.this, "Connecting to Room", Toast.LENGTH_LONG).show();
                    skylinkConnection.connectToRoom(SECRET, ROOM_NAME, USER_NAME);
                    joinRoomBtn.setText(getText(R.string.leave_room));
                } else {
                    Toast.makeText(MainActivity.this, "Disconnecting from Room", Toast.LENGTH_LONG).show();

                    skylinkConnection.disconnectFromRoom();
                    skylinkConnection.setLifeCycleListener(null);
                    skylinkConnection.setMessagesListener(null);
                    skylinkConnection.setRemotePeerListener(null);

                    joinRoomBtn.setText(getText(R.string.join_room));
                    connectedToRoom = false;
                }
            }
        });
    }

    /**
     * Triggered by the Listener Service
     *
     * @param event
     */
    @Subscribe
    public void WearSensorEvent(final WearSensorEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String message = event.getMessage();
                Log.d(TAG, "WearConnectionServiceEvent" + event.getMessage());

                SensorMessage sensorMessage = gson.fromJson(message, SensorMessage.class);

                if (sensorMessage.getGyroscopeX() != 0 && sensorMessage.getGyroscopeY() != 0 &&
                        sensorMessage.getGyroscopeZ() != 0) {
                    String gyroScopeValue = "x = " + Float.toString(sensorMessage.getGyroscopeX()) + "\n" +
                            "y = " + Float.toString(sensorMessage.getGyroscopeY()) + "\n" +
                            "z = " + Float.toString(sensorMessage.getGyroscopeZ()) + "\n";
                    txtGyroscope.setText(gyroScopeValue);
                }

                if (sensorMessage.getHeartRate() != 0) {
                    txtHeartRate.setText(String.valueOf(sensorMessage.getHeartRate()));
                }

                if (sensorMessage.getStepCount() != 0) {
                    txtStepCount.setText(String.valueOf(sensorMessage.getStepCount()));
                }

                sendData(message);
            }
        });

    }

    private void sendData(String message) {
        if (connectedToRoom && peerCount > 0) {
            try {
                Log.d(TAG, "Sending P2P Message !");
                skylinkConnection.sendP2PMessage(null, message);
            } catch (SkylinkException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @Override
    public void onConnect(boolean status, String message) {
        Log.d(TAG, "onConnect " + status + " " + message);
        connectedToRoom = status;
        if (status) {
            Toast.makeText(this, "Connected successfully", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDisconnect(int status, String message) {
        Log.d(TAG, "onDisconnect " + status + " " + message);
        connectedToRoom = false;
    }

    @Override
    public void onWarning(int status, String message) {
        Log.d(TAG, "onDisconnect " + status + " " + message);
    }

    @Override
    public void onRemotePeerJoin(String remotePeerId, Object o, boolean b) {
        Log.d(TAG, "onRemotePeerJoin " + remotePeerId);
        Toast.makeText(this, "Remote peer joined", Toast.LENGTH_LONG).show();
        peerCount++;
    }

    @Override
    public void onRemotePeerLeave(String remotePeerId, String message) {
        Log.d(TAG, "onRemotePeerLeave " + remotePeerId);
        Toast.makeText(this, "Remote peer left", Toast.LENGTH_LONG).show();
        if (peerCount != 0) {
            peerCount--;
        }
    }

    @Override
    public void onReceiveLog(String message) {
        Log.d(TAG, "onReceiveLog " + message);
    }

    @Override
    public void onLockRoomStatusChange(String message, boolean lockStatus) {
        Log.d(TAG, "onLockRoomStatusChange " + message);
    }

    @Override
    public void onServerMessageReceive(String remotePeerId, Object message, boolean isPrivate) {
        Log.d(TAG, "onServerMessageReceive " + remotePeerId);
    }

    @Override
    public void onP2PMessageReceive(String remotePeerId, Object message, boolean isPrivate) {
        Log.d(TAG, "onP2PMessageReceive " + remotePeerId);
    }

    @Override
    public void onRemotePeerUserDataReceive(String remotePeerId, Object o) {
        Log.d(TAG, "onRemotePeerUserDataReceive " + remotePeerId);
    }

    @Override
    public void onOpenDataConnection(String remotePeerId) {
        Log.d(TAG, "onOpenDataConnection " + remotePeerId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
