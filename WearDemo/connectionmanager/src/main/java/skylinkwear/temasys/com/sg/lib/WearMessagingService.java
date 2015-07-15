package skylinkwear.temasys.com.sg.lib;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * This service connects to the DataLayerService
 *
 * Created by janidu on 28/2/15.
 */
public class WearMessagingService {

    private static final String TAG = WearMessagingService.class.getName();
    private static WearMessagingService wearMessagingService;

    private Bus bus;
    private WearConnectionService wearConnectionService;
    private boolean connected;
    private Gson gson;

    private WearMessagingService() {
    }

    public static synchronized WearMessagingService getInstance() {
        if (wearMessagingService == null) {
            wearMessagingService = new WearMessagingService();
        }
        return wearMessagingService;
    }

    public void init(Bus bus, Gson gson, Context context) {
        this.bus = bus;
        bus.register(this);

        wearConnectionService = WearConnectionService.getInstance();
        wearConnectionService.init(this.bus, context);

        this.gson = gson;
    }

    public void connect() {
        wearConnectionService.connect();
    }

    public void disconnect() {
        wearConnectionService.disconnect();
    }

    public void sendMessage(Object message, String path) {
        if (connected) {
            // Send the message
            Log.d(TAG, "Connected Sending Message");
            new DataLayerService(
                    wearConnectionService.getGoogleClient(), path, gson.toJson(message)).start();
        }
    }

    @Subscribe
    public void WearConnectionServiceEvent(WearConnectionServiceEvent event) {
        switch (event.getEventType()) {
            case CONNECTED:
                Log.d(TAG, "WearConnectionServiceEvent Connected");
                connected = true;
                break;
            case SUSPENDED:
                Log.d(TAG, "WearConnectionServiceEvent Suspended");
                connected = false;
                break;
            case FAILED:
                Log.d(TAG, "WearConnectionServiceEvent Failed");
                connected = false;
                break;
            default:
        }
    }
}
