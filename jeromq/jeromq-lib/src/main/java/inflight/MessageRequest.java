package inflight;

import org.json.JSONObject;

/**
 * Created by shun on 9/2/16.
 */

public class MessageRequest extends InFlightMessage {
    protected MessageRequest(JSONObject json) {
        super(json);
    }

    public MessageRequest() {
        setType(InFlightMessageType.REQUEST);
    }
}
