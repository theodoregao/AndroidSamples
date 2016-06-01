package shun.gao.sample.usage.stats;

/**
 * Created by shun on 6/1/16.
 */

public class TimeInterval implements Comparable<TimeInterval> {

    private int mId;

    private long mStart;
    private long mEnd;

    public TimeInterval(int id, long start, long end) {
        mId = id;
        mStart = start;
        mEnd = end;
    }

    public long getInterval() {
        return mEnd - mStart;
    }

    public boolean isContains(long timestamp) {
        return mStart <= timestamp && timestamp <= mEnd;
    }

    public boolean isContains(TimeInterval timeInterval) {
        return mStart <= timeInterval.mStart && timeInterval.mEnd <= mEnd;
    }

    public boolean isForIdentity(int id) {
        return mId == id;
    }

    @Override
    public int compareTo(TimeInterval another) {
        return (int) (mStart - another.mStart);
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof TimeInterval && mId == ((TimeInterval) that).mId;
    }
}
