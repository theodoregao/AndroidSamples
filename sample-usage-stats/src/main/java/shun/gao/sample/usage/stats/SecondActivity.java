package shun.gao.sample.usage.stats;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = SecondActivity.class.getSimpleName();

    private AnalyticService serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        serviceConnection = new AnalyticService();

        bindService(new Intent(this, SimpleAnalyticService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serviceConnection.untimestamp();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.print:
                print();
                break;

            case R.id.add_stamp:
                addStamp();
                break;

            case R.id.remove_stamp:
                removeStamp();
                break;
        }
    }

    private void print() {
        ((TextView) findViewById(R.id.textView)).setText(serviceConnection.getTime() + " seconds");
    }

    private void addStamp() {
        serviceConnection.timestamp();
    }

    private void removeStamp() {
        serviceConnection.untimestamp();
    }

    class AnalyticService implements ServiceConnection {

        ISimpleAnalytics analytics;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            analytics = ISimpleAnalytics.Stub.asInterface(service);
            if (analytics != null) timestamp();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        public void timestamp() {
            try {
                analytics.timestamp(SecondActivity.TAG);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void untimestamp() {
            try {
                analytics.timestamp(SecondActivity.TAG + "OFF");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public int getTime() {
            try {
                return analytics.getTimeCount(SecondActivity.TAG);
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
    };
}
