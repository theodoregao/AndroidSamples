package shun.gao.widget.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WidgetReceiver extends BroadcastReceiver {

    private static final String TAG = WidgetReceiver.class.getSimpleName();

    public WidgetReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "onReceive() " + intent.getAction());
        Intent activityIntent = new Intent(context, WidgetActivcity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}
