package skylinkwear.temasys.com.sg.lib;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.squareup.otto.Bus;

/**
 * This services connects to Werable API
 * Created by janidu on 28/2/15.
 */
public class WearConnectionService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = WearConnectionService.class.getName();
    private GoogleApiClient googleClient;
    private static WearConnectionService wearConnectionService;
    private Bus bus;

    private WearConnectionService() {
    }

    public static synchronized WearConnectionService getInstance() {
        if (wearConnectionService == null) {
            wearConnectionService = new WearConnectionService();
        }
        return wearConnectionService;
    }

    public void init(Bus bus, Context context) {
        this.bus = bus;
        // Build a new GoogleApiClient for the Wearable API
        googleClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void connect() {
        if (googleClient != null && !googleClient.isConnected()) {
            Log.d(TAG, "Connecting");
            googleClient.connect();
        }
    }

    public void disconnect() {
        if (googleClient != null && googleClient.isConnected()) {
            Log.d(TAG, "Disconnecting");
            googleClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        bus.post(new WearConnectionServiceEvent(WearConnectionServiceEventType.CONNECTED));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
        bus.post(new WearConnectionServiceEvent(WearConnectionServiceEventType.SUSPENDED));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
        bus.post(new WearConnectionServiceEvent(WearConnectionServiceEventType.FAILED));
    }

    public GoogleApiClient getGoogleClient() {
        return googleClient;
    }
}

