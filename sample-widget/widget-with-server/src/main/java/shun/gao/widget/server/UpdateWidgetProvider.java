package shun.gao.widget.server;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;

public class UpdateWidgetProvider extends AppWidgetProvider {

    private static final String TAG = UpdateWidgetProvider.class.getSimpleName();


    private static int clickCount = 0;

    public static void pushWidgetUpdate(Context context, RemoteViews views) {

        System.out.println("Inside pushwidget");
        ComponentName myWidget = new ComponentName(context, UpdateWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.i("onUpdate method call", "Called");
        ComponentName thisWidget = new ComponentName(context, UpdateWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        //built intent to call service
        Intent intent = new Intent(context.getApplicationContext(), UpdateService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        Log.i("LOG", "before service");
        //update widget via service
        context.startService(new Intent(context, UpdateService.class));
    }

    public static class UpdateService extends Service {
        private Handler mHandler;
        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
                String time = format.format(System.currentTimeMillis());

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

                ComponentName thisWidget = new ComponentName(getApplicationContext(), UpdateWidgetProvider.class);
                int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

                if (allWidgetIds != null) {
                    Log.i(TAG, "widgets: " + String.valueOf(allWidgetIds.length));

                    for (int widgetId : allWidgetIds) {
                        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.widget_layout);
                        Intent broadCastIntent = new Intent("shun.gao.action.WIDGET_CLICKED");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateService.this, 0, broadCastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.button, pendingIntent);
                        rv.setTextViewText(R.id.text, time);
                        appWidgetManager.updateAppWidget(widgetId, rv);
                    }
                }

                mHandler.postDelayed(this, 1000);
            }
        };

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (mHandler == null) mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(runnable);
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            // We don't need to bind to this service
            return null;
        }
    }
}