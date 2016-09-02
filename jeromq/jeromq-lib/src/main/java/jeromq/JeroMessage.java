package jeromq;

import java.io.Serializable;

/**
 * Created by shun on 8/17/16.
 */

public class JeroMessage implements Serializable {
    private Channel mChannel;
    private String mMessage;

    public JeroMessage(Channel channel, String message) {
        this.mChannel = channel;
        this.mMessage = message;
    }

    public Channel getChannel() {
        return mChannel;
    }

    public String getMessage() {
        return mMessage;
    }
}
