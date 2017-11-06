package docscan.gao.shun.sg.fmp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Theodore on 2017/5/12.
 */

public class SharedPreferenceManager {

    private static final String PREFERENCE_FIRE_BASE = "PREFERENCE_FIRE_BASE";
    private static final String INSTANCE_ID = "INSTANCE_ID";

    private SharedPreferences sharedPreferences;
    private InstanceIdListener instanceIdListener;

    public SharedPreferenceManager(Context context) {
        this(context, null);
    }

    public SharedPreferenceManager(Context context, final InstanceIdListener instanceIdListener) {
        this.instanceIdListener = instanceIdListener;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_FIRE_BASE, 0);
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (instanceIdListener != null) instanceIdListener.onInstanceIdReceived(getInstanceId());
            }
        });
    }

    public boolean hasInstanceId() {
        return sharedPreferences.contains(INSTANCE_ID);
    }

    public String getInstanceId() {
        if (!hasInstanceId()) return null;
        return sharedPreferences.getString(INSTANCE_ID, "");
    }

    public void putInstanceId(String instanceId) {
        if (instanceId == null) return;
        sharedPreferences.edit().putString(INSTANCE_ID, instanceId).commit();
    }

    public interface InstanceIdListener {
        void onInstanceIdReceived(String instanceId);
    }
}
