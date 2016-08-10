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

public class EventSource {

    private static final String TAG = EventSource.class.getSimpleName();

    private static final int PACKAGE_SIZE = 2 * 1024;
    private static final int BUFFER_SIZE = 2 * PACKAGE_SIZE;
    private static final int BYTE_SIZE_PAYLOAD = 2;

    private Thread mThread;
    private BlockingQueue<Event> mEvents;
    private EventListener mEventListener;
    private boolean mRunning;

    private ByteBuffer mByteBuffer;

    private byte[] mBufferPayload;

    private int mPackageFormatErrorCount;
    private int mPayloadJsonFormatErrorCount;
    private int mEventCount;

    public enum EventSourceError {
        PackageFormatError,
        PayloadJsonFormatError
    }

    public EventSource() {
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
        Log.w(TAG, EventSource.class.getSimpleName() + " stopped");
    }

    public void setListener(EventListener listener) {
        mEventListener = listener;
    }

    public void feedBytesWithSequenceNumber(byte[] bytes, int offset, int length) {
        mByteBuffer.clear();
        mByteBuffer.put(bytes, offset, 4);
        mByteBuffer.flip();
        int sequenceNumber = mByteBuffer.asIntBuffer().get();
        feedBytes(sequenceNumber, bytes, offset + 4, length - 4);
    }

    public void feedBytes(byte[] bytes, int offset, int length) {
        feedBytes(Event.INVALID_SEQUENCE_NUMBER, bytes, offset, length);
    }

    public void feedBytes(int sequenceNumber, byte[] bytes, int offset, int length) {
        if (length > 0) {
            mByteBuffer.clear();
            mByteBuffer.put(bytes, offset, length);
            mByteBuffer.flip();

            List<Event> events = new ArrayList<>();

            while (mByteBuffer.remaining() >= BYTE_SIZE_PAYLOAD) {
                // read payload size
                byte hi = mByteBuffer.get(mByteBuffer.position());
                byte lo = mByteBuffer.get(mByteBuffer.position() + 1);
                int payloadSize = new BigInteger(new byte[] {hi, lo}).intValue();

                if (payloadSize <= 0 || payloadSize > BUFFER_SIZE) {
                    Log.w(TAG, "incorrect payload size: " + payloadSize);
                    onError(EventSourceError.PackageFormatError);
                    break;
                }

                Log.v(TAG, "payload size = " + payloadSize);

                // check whether buffer is ready
                if (mByteBuffer.remaining() < payloadSize + BYTE_SIZE_PAYLOAD) {
                    break;
                }

                mBufferPayload = new byte[payloadSize];
                mByteBuffer.get();
                mByteBuffer.get();
                mByteBuffer.get(mBufferPayload);

                String payload = new String(mBufferPayload, 0, payloadSize);

                try {
                    events.add(new Event(payload, sequenceNumber));
                } catch (Event.InvalidEvent invalidEvent) {
                    onError(EventSourceError.PayloadJsonFormatError);
                    continue;
                }
            }

            mByteBuffer.compact();
//            Log.v(TAG, "after compact: " + mByteBuffer.position() + ", " + mByteBuffer.remaining() + ", " + mByteBuffer.limit());

            putEvents(events);
        }
        else {
            onError(EventSourceError.PackageFormatError);
        }

    }

    private void onError(EventSourceError eventSourceError) {
        if (mEventListener != null) mEventListener.onError(eventSourceError);
        if (eventSourceError == EventSourceError.PackageFormatError) mByteBuffer.clear();

        switch (eventSourceError) {
            case PackageFormatError:
                mPackageFormatErrorCount++;
                break;

            case PayloadJsonFormatError:
                mPayloadJsonFormatErrorCount++;
                break;

            default:
                break;
        }
    }

    private void putEvents(List<Event> events) {
        for (Event event: events) putEvent(event);
    }

    private void putEvent(Event event) {
        mEvents.add(event);
        mEventCount++;
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
        void onError(EventSourceError eventSourceError);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(EventSource.class.getSimpleName());
        stringBuilder.append("{")
                .append("generated event count: ").append(mEventCount)
                .append(", package error count: ").append(mPackageFormatErrorCount)
                .append(", invalid jason format count: ").append(mPayloadJsonFormatErrorCount)
                .append("}");
        return stringBuilder.toString();
    }
}
