package com.example.sample_nio_server;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shun on 8/2/16.
 */

public class Event implements Comparable<Event> {
    private static final String EVENT_NAME = "event_name";
    private static final String DATA = "data";

    public static final int INVALID_SEQUENCE_NUMBER = Integer.MIN_VALUE;
    private long mTimestamp;
    private int mSequenceNumber;
    private String mEventName;
    private String mData;

    public Event(String payload, int sequenceNumber) throws InvalidEvent {
        mSequenceNumber = sequenceNumber;
        mTimestamp = System.currentTimeMillis();
        try {
            JSONObject json = new JSONObject(payload);
            if (!json.has(EVENT_NAME)) throw new InvalidEvent();
            mEventName = json.getString(EVENT_NAME);
            if (json.has(DATA)) mData = json.getString(DATA);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new InvalidEvent();
        }
    }

    public Event(String payload) throws InvalidEvent {
        this(payload, INVALID_SEQUENCE_NUMBER);
    }

    public String getEventName() {
        return mEventName;
    }

    public String getData() {
        return mData;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(Event.class.getSimpleName());
        stringBuilder.append("{")
                .append("timestamp=").append(mTimestamp)
                .append(", sequenceNumber=").append(mSequenceNumber)
                .append(", eventName=").append(mEventName)
                .append(", data=").append(mData)
                .append("}");
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(Event another) {
        return (mSequenceNumber == INVALID_SEQUENCE_NUMBER || another.mSequenceNumber == INVALID_SEQUENCE_NUMBER || mSequenceNumber == another.mSequenceNumber) ?
                (int) (mTimestamp - another.mTimestamp) : mSequenceNumber - another.mSequenceNumber;
    }

    public static class InvalidEvent extends Exception {

    }
}
