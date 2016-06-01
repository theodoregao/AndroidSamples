package shun.gao.sample.usage.stats;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by shun on 6/1/16.
 */

public class AnalyticsTimeInterval {

    private static final int INTERVAL_DAYS = 7;
    private static final int BACKGROUND = "BACKGROUND_INTERNAL".hashCode();

    private Context mContext;
    private List<TimeInterval> mTimeIntervals;
    private long mTimestamp = System.currentTimeMillis();
    private int mTargetId = AnalyticsTimeInterval.class.getCanonicalName().hashCode();

    public AnalyticsTimeInterval(Context context) {
        mContext = context;
        mTimeIntervals = new ArrayList<>();

        if (!hasPermission()) requestPermission();
    }

    public void addTimeStamp(String id) {
        addTimeStamp(id.hashCode());
    }

    public void addTimeStamp(int id) {
        long newTimestamp = System.currentTimeMillis();
        mTimeIntervals.add(new TimeInterval(mTargetId, mTimestamp, newTimestamp));
        mTimestamp = newTimestamp;
        mTargetId = id;
    }

    public long calculateTimeInterval(String id) {
        return calculateTimeInterval(id.hashCode());
    }

    public long calculateTimeInterval(int id) {
        List<TimeInterval> foregroundTimeIntervals = getForegroundTimeIntervals(id);
        List<TimeInterval> backgroundTimeIntervals = getBackgroundTimeIntervals();

        long timeInterval = 0;
        int position = 0;
        TimeInterval[] timeIntervals = new TimeInterval[foregroundTimeIntervals.size() + backgroundTimeIntervals.size()];
        for (TimeInterval interval: foregroundTimeIntervals) timeIntervals[position++] = interval;
        for (TimeInterval interval: backgroundTimeIntervals) timeIntervals[position++] = interval;
        Arrays.sort(timeIntervals);

        TimeInterval foregroundTimeInterval = null;
        for (int i = 0; i < timeIntervals.length; i++) {
            if (timeIntervals[i].isForIdentity(id)) {
                timeInterval += timeIntervals[i].getInterval();
                foregroundTimeInterval = timeIntervals[i];
            }
            else if (foregroundTimeInterval != null
                    && timeIntervals[i].isForIdentity(BACKGROUND)
                    && foregroundTimeInterval.isContains(timeIntervals[i])) {
                timeInterval -= timeIntervals[i].getInterval();
            }
        }

        return timeInterval;
    }

    private List<TimeInterval> getForegroundTimeIntervals(int id) {
        List<TimeInterval> timeIntervals = new ArrayList<>();
        for (TimeInterval timeInterval: mTimeIntervals) {
            if (timeInterval.isForIdentity(id)) timeIntervals.add(timeInterval);
        }
        if (mTargetId == id) timeIntervals.add(new TimeInterval(id, mTimestamp, System.currentTimeMillis()));

        return timeIntervals;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private List<TimeInterval> getBackgroundTimeIntervals() {
        List<TimeInterval> timeIntervals = new ArrayList<>();

        UsageStatsManager usageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
        if (usageStatsManager != null) {

            UsageEvents usageEvents = usageStatsManager.queryEvents(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(INTERVAL_DAYS), System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
            UsageEvents.Event event = new UsageEvents.Event();
            String packageName = mContext.getPackageName();

            List<TimeEvent> relatedEvents = new ArrayList<>();
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                int eventType = event.getEventType();
                if (event.getPackageName().equals(packageName) &&
                        (eventType == UsageEvents.Event.MOVE_TO_FOREGROUND || eventType == UsageEvents.Event.MOVE_TO_BACKGROUND)) {
                    TimeEvent timeEvent = new TimeEvent(event.getEventType(), event.getTimeStamp());
                    relatedEvents.add(timeEvent);
                }
            }

            TimeEvent[] events = new TimeEvent[relatedEvents.size()];
            int size = 0;
            for (TimeEvent timeEvent: relatedEvents) events[size++] = timeEvent;
            Arrays.sort(events);

            for (int i = 1; i < relatedEvents.size(); i++) {
                if (events[i].getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND && events[i - 1].getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND)
                    timeIntervals.add(new TimeInterval(BACKGROUND, events[i - 1].getTimestamp(), events[i].getTimestamp()));
            }
        }

        return timeIntervals;
    }

    private void requestPermission() {
        mContext.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), mContext.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

}
