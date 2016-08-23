package jeromq;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

/**
 * Created by shun on 8/17/16.
 */

public enum Channel {

    STATE, APPLICATION, COMMAND, NONE;

    private static final String TAG = Channel.class.getSimpleName();

    private static final String PRE_FIX = "INFLIGHT_SDK_CHANNEL_";

    public static Channel getChannelByName(String name) {
        switch (name.toUpperCase()) {
            case "STATE":
            case PRE_FIX + "STATE":
                return STATE;

            case "APPLICATION":
            case PRE_FIX + "APPLICATION":
                return APPLICATION;

            case "COMMAND":
            case PRE_FIX + "COMMAND":
                return COMMAND;

            default:
                return NONE;
        }
    }

    public String toString() {
        return PRE_FIX + name();
    }

    public static boolean isChannel(String message) {
        return message.startsWith(PRE_FIX);
    }
}
