package shun.gao.sample.usage.stats;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class SimpleAnalyticService extends Service {

    AnalyticsTimeInterval mAnalyticsTimeInterval;

    public SimpleAnalyticService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAnalyticsTimeInterval = new AnalyticsTimeInterval(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IBinder binder = new ISimpleAnalytics.Stub() {

        @Override
        public void timestamp(String key) throws RemoteException {
            mAnalyticsTimeInterval.addTimeStamp(key);
        }

        @Override
        public int getTimeCount(String key) throws RemoteException {
            return (int) mAnalyticsTimeInterval.calculateTimeInterval(key) / 1000;
        }
    };
}
