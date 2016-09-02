package inflight;

import org.json.JSONObject;

/**
 * Created by shun on 9/2/16.
 */

public class MessageEvent extends InFlightMessage {
    protected MessageEvent(JSONObject json) {
        super(json);
    }

    public MessageEvent() {
        setType(InFlightMessageType.EVENT);
    }
}
