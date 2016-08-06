package com.example.sample_nio_server;

import android.util.Log;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by shun on 8/2/16.
 */

public class ServerTcpEventSource {

    private static final String TAG = ServerTcpEventSource.class.getSimpleName();

    private static final int ERROR_COUNT_LIMIT = 64;
    private static final int PACKAGE_SIZE = 2 * 1024;
    private static final int BUFFER_SIZE = 2 * PACKAGE_SIZE;
    private static final int BYTE_SIZE_PAYLOAD = 2;

    private Thread mThread;
    private BlockingQueue<Event> mEvents;
    private EventListener mEventListener;
    private boolean mRunning;

    private ByteBuffer mByteBuffer;
    private int mErrorCount;

    private byte[] mBufferPayload;

    public ServerTcpEventSource() {
        mByteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        mByteBuffer.order(ByteOrder.BIG_ENDIAN);

        mEvents = new ArrayBlockingQueue<>(1024);
        mThread = new Thread() {
            @Override
            public void run() {
                Event event = null;
                do {
                    if (event != null && mEventListener != null) {
                        mEventListener.onEventReceived(event);
                    }
                    try {
                        event = getEvent();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (mRunning);
            }
        };
        Log.w(TAG, ServerTcpEventSource.class.getSimpleName() + " stopped");
    }

    public void setListener(EventListener listener) {
        mEventListener = listener;
    }

    public void feedBytes(byte[] bytes) {
        feedBytes(bytes, 0, bytes.length);
    }

    public void feedBytes(byte[] bytes, int offset) {
        feedBytes(bytes, offset, bytes.length - offset);
    }

    public void feedBytes(byte[] bytes, int offset, int length) {
        if (length > 0) {
            mByteBuffer.put(bytes, offset, length);
            mByteBuffer.flip();
            Log.v(TAG, "after flip: " + mByteBuffer.position() + ", " + mByteBuffer.remaining() + ", " + mByteBuffer.limit());

            List<Event> events = new ArrayList<>();

            while (mByteBuffer.remaining() >= BYTE_SIZE_PAYLOAD) {
                // read payload size
                byte hi = mByteBuffer.get(mByteBuffer.position());
                byte lo = mByteBuffer.get(mByteBuffer.position() + 1);
                int payloadSize = new BigInteger(new byte[] {hi, lo}).intValue();

                // check whether buffer is ready
                if (mByteBuffer.remaining() < payloadSize + BYTE_SIZE_PAYLOAD) {
                    mErrorCount++;
                    break;
                }

                mBufferPayload = new byte[payloadSize];
                mByteBuffer.get();
                mByteBuffer.get();
                mByteBuffer.get(mBufferPayload);

                String payload = new String(mBufferPayload, 0, payloadSize);

                try {
                    events.add(new Event(payload));
                    mErrorCount = 0;
                } catch (Event.InvalidEvent invalidEvent) {
                    invalidEvent.printStackTrace();
                    mErrorCount++;
                    continue;
                }
            }

            mByteBuffer.compact();
            Log.v(TAG, "after compact: " + mByteBuffer.position() + ", " + mByteBuffer.remaining() + ", " + mByteBuffer.limit());

            putEvents(events);
        }
        else mErrorCount++;

        if (mErrorCount > ERROR_COUNT_LIMIT && mEventListener != null) {
            mEventListener.onError();
            mErrorCount = 0;
            mByteBuffer.clear();
        }

    }

    private void putEvents(List<Event> events) {
        for (Event event: events) putEvent(event);
    }

    private void putEvent(Event event) {
        mEvents.add(event);
    }

    private Event getEvent() throws InterruptedException {
        return mEvents.take();
    }

    public void start() {
        mRunning = true;
        mThread.start();
    }

    public void stop() {
        mRunning = false;
    }

    public interface EventListener {
        void onEventReceived(Event event);
        void onError();
    }
}
