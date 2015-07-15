package skylinkwear.temasys.com.sg.lib;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * This service connects to the wearable data layer
 * Created by janidu on 28/2/15.
 */
public class DataLayerService extends Thread {

    private static final String TAG = DataLayerService.class.getName();
    private final String path;
    private final String message;
    private final GoogleApiClient googleApiClient;

    // Constructor to send a message to the data layer
    DataLayerService(GoogleApiClient googleApiClient, String path, String message) {
        this.googleApiClient = googleApiClient;
        this.path = path;
        this.message = message;
    }

    public void run() {
        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
        for (Node node : nodes.getNodes()) {
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(googleApiClient,
                    node.getId(), path, message.getBytes()).await();
            if (result.getStatus().isSuccess()) {
                Log.d(TAG, "Message: {" + message + "} sent to: " + node.getDisplayName());
            } else {
                // Log an error
                Log.e(TAG, "ERROR: failed to send Message");
            }
        }
    }
}
