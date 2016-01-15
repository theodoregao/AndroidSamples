package sg.shun.gao.libs.android.share;

import android.os.Process;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Theodore on 2016/1/14.
 */
public final class SharedComponentManager {

    private static final String TAG = SharedComponentManager.class.getSimpleName();

    private static SharedComponentManager sSharedComponentManager;

    private String description;

    private TextView textView;

    private SharedComponentManager() {
        Log.v(TAG, toString() + ": SharedComponentManager:");
        description = "timestamp=" + System.currentTimeMillis() + ", pid=" + Process.myPid() + ", uid=" + Process.myUid() + ", tid" + Process.myTid();
    }

    public static SharedComponentManager getInstance() {
        if (sSharedComponentManager == null) sSharedComponentManager = new SharedComponentManager();
        return sSharedComponentManager;
    }

    public String getDescription() {
        return description;
    }

    public void putTextView(TextView textView) {
        Log.v(TAG, toString() + ": putTextView()");
        this.textView = textView;
    }

    public TextView getTextView() {
        Log.v(TAG, toString() + ": getTextView()");
        return textView;
    }

    public void putText(String text) {
        System.setProperty("text", text);
    }

    public String getText() {
        return System.getProperty("text");
    }
}
