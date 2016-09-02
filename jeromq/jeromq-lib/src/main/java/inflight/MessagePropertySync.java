package inflight;

import org.json.JSONObject;

/**
 * Created by shun on 9/2/16.
 */

public class MessagePropertySync extends InFlightMessage {
    protected MessagePropertySync(JSONObject json) {
        super(json);
    }

    public MessagePropertySync() {
        setType(InFlightMessageType.PROPERTY_SYNC);
    }
}
