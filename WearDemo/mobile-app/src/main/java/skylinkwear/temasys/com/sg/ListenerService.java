package skylinkwear.temasys.com.sg;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import skylinkwear.temasys.com.sg.lib.MessagingConstants;


/**
 * This Service will receive messages from the wearable
 * Created by janidu on 28/2/15.
 */
public class ListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(MessagingConstants.MESSAGE_PATH)) {
            final String message = new String(messageEvent.getData());
            MessagingService.getInstance().getBus().post(
                    new WearSensorEvent(message));
        } else {
            super.onMessageReceived(messageEvent);
        }
    }
}
