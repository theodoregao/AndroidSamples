package gao.shun.sg.placeaware;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by Theodore on 2017/11/4.
 */

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();

    /***
     * Handles the Broadcast message sent when the Geofence Transition is triggered
     * Careful here though, this is running on the main thread so make sure you start an AsyncTask for
     * anything that takes longer than say 10 second to run
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive called");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            setRingMode(context, AudioManager.RINGER_MODE_SILENT);
        }
        else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            setRingMode(context, AudioManager.RINGER_MODE_NORMAL);
        }
        else {
            Log.e(TAG, "Unknown transition: " + geofenceTransition);
        }
    }

    private void setRingMode(Context context, int mode) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT < 24 || (android.os.Build.VERSION.SDK_INT >= 24 && !notificationManager.isNotificationPolicyAccessGranted())) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(mode);
        }
    }
}
