package skylinkwear.temasys.com.sg;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Service that is used to send messages
 * <p/>
 * Created by janidu on 1/3/15.
 */
public class MessagingService {

    private Bus bus;
    private static MessagingService messagingService;

    public static synchronized MessagingService getInstance() {
        if (messagingService == null) {
            messagingService = new MessagingService();
            messagingService.bus = new Bus(ThreadEnforcer.ANY);
        }
        return messagingService;
    }

    public Bus getBus() {
        return bus;
    }
}


class WearSensorEvent {

    private String message;

    public WearSensorEvent(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}