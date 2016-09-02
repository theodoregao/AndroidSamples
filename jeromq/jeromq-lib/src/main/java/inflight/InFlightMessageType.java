package inflight;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shun on 9/2/16.
 */

public enum InFlightMessageType {
    UNKNOWN(""), REQUEST("request"), PROPERTY_SYNC("propertySync"), EVENT("event");

    private String value;
    InFlightMessageType(String value) {
        this.value = value;
    }

    private static Map<String, InFlightMessageType> map = new HashMap<>();
    static {
        map.put(REQUEST.toString(), REQUEST);
        map.put(PROPERTY_SYNC.toString(), PROPERTY_SYNC);
        map.put(EVENT.toString(), EVENT);
    }

    public static InFlightMessageType getMessageType(String type) {
        if (map.containsKey(type)) return map.get(type);
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return value;
    }
}
