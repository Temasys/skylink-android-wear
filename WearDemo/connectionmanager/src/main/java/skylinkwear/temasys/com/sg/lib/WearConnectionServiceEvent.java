package skylinkwear.temasys.com.sg.lib;


enum WearConnectionServiceEventType {
    CONNECTED, FAILED, SUSPENDED
}

public class WearConnectionServiceEvent {

    private final WearConnectionServiceEventType eventType;

    public WearConnectionServiceEvent(WearConnectionServiceEventType eventType) {
        this.eventType = eventType;
    }

    public WearConnectionServiceEventType getEventType() {
        return eventType;
    }
}
