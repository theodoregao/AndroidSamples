package gao.shun.sg.shareduserid.app1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import sg.shun.gao.libs.android.share.SharedComponentManager;

public class BackgroundService extends Service {

    private static final String TAG = BackgroundService.class.getSimpleName();

    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand() " + SharedComponentManager.getInstance().getDescription());
        TextView textView = new TextView(this);
        textView.setText("onStartCommand()");
        SharedComponentManager.getInstance().putTextView(textView);
        SharedComponentManager.getInstance().putText("text from BackgroundService");
        return super.onStartCommand(intent, flags, startId);
    }
}
