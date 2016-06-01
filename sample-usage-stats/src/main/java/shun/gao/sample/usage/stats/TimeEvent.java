package shun.gao.sample.usage.stats;

public class TimeEvent implements Comparable<TimeEvent> {
    private int type;
    private long timestamp;

    public TimeEvent(int type, long timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }

    public int getEventType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(TimeEvent another) {
        return (int) (timestamp - another.timestamp);
    }
}