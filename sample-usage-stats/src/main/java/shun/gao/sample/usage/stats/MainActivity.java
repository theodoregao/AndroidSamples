package shun.gao.sample.usage.stats;

import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void requestPermission() {
        Toast.makeText(this, "Need to request permission", Toast.LENGTH_SHORT).show();
        startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
    }

    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
//        return ContextCompat.checkSelfPermission(this,
//                Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED;
    }

    private void getStats(String className) {
        Log.v(TAG, "get stats: ");
        UsageStatsManager lUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> lUsageStatsList = lUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, System.currentTimeMillis()- TimeUnit.DAYS.toMillis(1),System.currentTimeMillis()+ TimeUnit.DAYS.toMillis(1));

        TextView lTextView = (TextView) findViewById(R.id.usage_stats);

        StringBuilder lStringBuilder = new StringBuilder();

        for (UsageStats lUsageStats:lUsageStatsList){
            lStringBuilder.append(lUsageStats.getPackageName());
            lStringBuilder.append(" - ");
            lStringBuilder.append(lUsageStats.getLastTimeUsed());
            lStringBuilder.append(" - ");
            lStringBuilder.append(lUsageStats.getTotalTimeInForeground());
            lStringBuilder.append("\r\n");
        }

        lTextView.setText(lStringBuilder.toString());

        UsageEvents usageEvents = lUsageStatsManager.queryEvents(System.currentTimeMillis()- TimeUnit.DAYS.toMillis(1),System.currentTimeMillis()+ TimeUnit.DAYS.toMillis(1));
        UsageEvents.Event event = new UsageEvents.Event();
        long timeStamp = 0;
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getPackageName().equals(getPackageName()) && TextUtils.equals(event.getClassName(), className)) {
                Log.v(TAG, event.getPackageName() + " - " + event.getClassName() + " - " + event.getEventType());
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) Log.v(TAG, "interval: " + (event.getTimeStamp() - timeStamp));
            }
            timeStamp = event.getTimeStamp();
        }

        if (event.getPackageName().equals(getPackageName()) && TextUtils.equals(event.getClassName(), className) && event.getEventType() == 1) {
            Log.v(TAG, "interval: " + (System.currentTimeMillis() - timeStamp));
        }

        Log.v(TAG, "get stats done");

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_activity:
                startActivity(new Intent(this, SecondActivity.class));
                break;

            case R.id.show_stats:
                getStats(MainActivity.class.getName());
                break;

            default:
                break;
        }
    }
}
