package inflight;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jeromq.Channel;
import jeromq.JeroMessage;
import jeromq.Publisher;
import jeromq.Subscriber;

/**
 * Created by shun on 9/2/16.
 */

public class InFlightMessageSource implements Subscriber.OnJeroMessageReceiveListener {

    private static final int THREAD_POOL_SIZE = 5;
    private static final int INCOMING_QUEUE_SIZE = 1024;
    private static final int PORT = 6000;

    private boolean mRunning;
    private Publisher mPublisher;
    private Map<String, Subscriber> mSubscribers;
    private ExecutorService mExecutorService;
    private BlockingQueue<JeroMessage> mIncomingMessage;

//    private OnInFlightMessageReceiveListener mOnInFlightMessageReceiveListener;
//    private OnRequestMessageReceiveListener mOnRequestMessageReceiveListener;
//    private OnPropertySyncMessageReceiveListener mOnPropertySyncMessageReceiveListener;
//    private OnEventMessageReceiveListener mOnEventMessageReceiveListener;

    private InFlightSourceListener mInFlightSourceListener;

    public enum InFlightMessageSourceError {
        ERROR_PAYLOAD_JSON_FORMAT,
        ERROR_SUBSCRIBER_SIZE_LIMIT,
        ERROR_PUBLISHING_INVALID_MESSAGE
    }

    public InFlightMessageSource(String ip) {
        mExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        mPublisher = new Publisher(ip, PORT);
        mSubscribers = new HashMap<>();
        mIncomingMessage = new ArrayBlockingQueue<>(INCOMING_QUEUE_SIZE);

        mExecutorService.execute(mMessageHandler);
        mExecutorService.execute(mPublisher);
    }

    public void subscribe(Channel channel) {
        String ip = channel.getChannelName().substring("INFLIGHT_SDK_CHANNEL_".length());
        if (!mSubscribers.containsKey(ip)) {
            if (mSubscribers.size() + 2 >= THREAD_POOL_SIZE) {
                if (mInFlightSourceListener != null) mInFlightSourceListener.onInFlightSourceError(InFlightMessageSourceError.ERROR_SUBSCRIBER_SIZE_LIMIT);
                return;
            }
            Subscriber subscriber = new Subscriber(ip, PORT);
            subscriber.setOnJeroMessageReceiveListener(this);
            mExecutorService.execute(subscriber);
            mSubscribers.put(ip, subscriber);
        }
        mSubscribers.get(ip).subscribe(channel);
    }

    @Override
    public void onJeroMessageReceived(JeroMessage jeroMessage) {
        mIncomingMessage.add(jeroMessage);
    }

    public void unSubscribe(Channel channel) {
        for (Subscriber subscriber: mSubscribers.values()) subscriber.unsubscribe(channel);
    }

    public void publishMessage(Channel channel, InFlightMessage inFlightMessage) {
        if (inFlightMessage == null) {
            if (mInFlightSourceListener != null) mInFlightSourceListener.onInFlightSourceError(InFlightMessageSourceError.ERROR_PUBLISHING_INVALID_MESSAGE);
        }
        else mPublisher.publishMessage(channel, inFlightMessage.toJson());
    }

    public void stop() {
        mRunning = false;
        for (String ip: mSubscribers.keySet()) {
            mSubscribers.remove(ip).terminate();
        }
    }

    private Runnable mMessageHandler = new Runnable() {
        @Override
        public void run() {
            mRunning = true;
            while (mRunning) {
                JeroMessage jeroMessage = null;
                try {
                    jeroMessage = mIncomingMessage.take();
                    JSONObject json = new JSONObject(jeroMessage.getMessage());
                    InFlightMessage inFlightMessage = InFlightMessage.toInFlightMessage(json);
                    if (mInFlightSourceListener != null) {
                        if (mInFlightSourceListener instanceof OnRequestMessageReceiveListener)
                            ((OnRequestMessageReceiveListener) mInFlightSourceListener).onRequestMessageReceived(jeroMessage.getChannel(), (MessageRequest) inFlightMessage);
                        else if (mInFlightSourceListener instanceof OnPropertySyncMessageReceiveListener)
                            ((OnPropertySyncMessageReceiveListener) mInFlightSourceListener).onPropertySyncMessageReceived(jeroMessage.getChannel(), (MessagePropertySync) inFlightMessage);
                        else if (mInFlightSourceListener instanceof OnEventMessageReceiveListener)
                            ((OnEventMessageReceiveListener) mInFlightSourceListener).onEventMessageReceived(jeroMessage.getChannel(), (MessageEvent) inFlightMessage);
                        else if (mInFlightSourceListener instanceof OnInFlightMessageReceiveListener)
                            ((OnInFlightMessageReceiveListener) mInFlightSourceListener).onInFlightMessageReceived(jeroMessage.getChannel(), inFlightMessage);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (mInFlightSourceListener != null) mInFlightSourceListener.onInFlightSourceError(InFlightMessageSourceError.ERROR_PAYLOAD_JSON_FORMAT);
                }
            }
        }
    };

//    public void setOnInFlightMessageReceiveListener(OnInFlightMessageReceiveListener onInFlightMessageReceiveListener) {
//        mOnInFlightMessageReceiveListener = onInFlightMessageReceiveListener;
//    }
//
//    public void setOnRequestMessageReceiveListener(OnRequestMessageReceiveListener onRequestMessageReceiveListener) {
//        mOnRequestMessageReceiveListener = onRequestMessageReceiveListener;
//    }
//
//    public void setOnPropertySyncMessageReceiveListener(OnPropertySyncMessageReceiveListener onPropertySyncMessageReceiveListener) {
//        mOnPropertySyncMessageReceiveListener = onPropertySyncMessageReceiveListener;
//    }
//
//    public void setOnEventMessageReceiveListener(OnEventMessageReceiveListener onEventMessageReceiveListener) {
//        mOnEventMessageReceiveListener = onEventMessageReceiveListener;
//    }

    public void setInFlightSourceListener(InFlightSourceListener inFlightSourceListener) {
        mInFlightSourceListener = inFlightSourceListener;
    }

    public interface InFlightSourceListener {
        void onInFlightSourceError(InFlightMessageSourceError error);
    }

    public interface OnInFlightMessageReceiveListener extends InFlightSourceListener {
        void onInFlightMessageReceived(Channel channel, InFlightMessage inFlightMessage);
    }

    public interface OnRequestMessageReceiveListener extends InFlightSourceListener {
        void onRequestMessageReceived(Channel channel, MessageRequest request);
    }

    public interface OnPropertySyncMessageReceiveListener extends InFlightSourceListener {
        void onPropertySyncMessageReceived(Channel channel, MessagePropertySync propertySync);
    }

    public interface OnEventMessageReceiveListener extends InFlightSourceListener {
        void onEventMessageReceived(Channel channel, MessageEvent event);
    }

}
