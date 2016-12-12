package sg.shun.gao.sample.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

public class ActivityLifecycleObserver implements Application.ActivityLifecycleCallbacks {
        private static Object LOCK = new Object();
        private static ActivityLifecycleObserver INSTANCE = null;
        private static final String TAG = "Test";

        private int activities = 0;
        private boolean isInBackground = false;

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d(TAG, "onActivityPaused " + activity.getClass().getName());

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d(TAG, "onActivityResumed " + activity.getClass().getName());

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG, "onActivityDestroyed" + activity.getClass().getName());
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.d(TAG, "onActivitySaveInstanceState " + activity.getClass().getName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG, "onActivityStarted " + activity.getClass().getName());
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d(TAG, "onActivityCreated" + activity.getClass().getName());
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG, "onActivityStopped " + activity.getClass().getName());

        }


}