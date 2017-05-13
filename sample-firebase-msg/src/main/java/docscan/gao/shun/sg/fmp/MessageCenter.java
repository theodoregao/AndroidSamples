package docscan.gao.shun.sg.fmp;

import android.util.Log;

/**
 * Created by Theodore on 2017/5/12.
 */

public class MessageCenter {
    private static final String TAG = MessageCenter.class.getSimpleName();
    private static MessageCenter INSTANCE = new MessageCenter();
    private MessageListener messageListener;

    public static MessageCenter getInstance() { return INSTANCE; }

    public void setMessageListener(MessageListener listener) {
        messageListener = listener;
    }

    public void pushMessage(String message) {
        Log.v(TAG, "pushMessage() " + message);
        Log.v(TAG, "messageListener == null ? " + (messageListener == null));
        if (messageListener != null) messageListener.onMessageReceived(message);
    }

    public interface MessageListener {
        public void onMessageReceived(String message);
    }
}
