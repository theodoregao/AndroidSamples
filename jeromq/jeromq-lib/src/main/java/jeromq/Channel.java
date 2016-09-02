package jeromq;

import android.text.TextUtils;

/**
 * Created by shun on 8/17/16.
 */

public class Channel {

    private static final String PRE_FIX = "INFLIGHT_SDK_CHANNEL_";

    private String mChannelName;

    public Channel(String channelName) {
        if (isChannel(channelName)) mChannelName = channelName;
        else mChannelName = PRE_FIX + channelName;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public static boolean isChannel(String channel) {
        return channel.startsWith(PRE_FIX) && channel.length() > PRE_FIX.length();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Channel && equals((Channel) obj);
    }

    private boolean equals(Channel channel) {
        return TextUtils.equals(channel.mChannelName, mChannelName);
    }

    @Override
    public int hashCode() {
        return mChannelName.hashCode();
    }
}
