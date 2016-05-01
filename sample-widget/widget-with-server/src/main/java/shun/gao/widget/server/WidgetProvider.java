package shun.gao.widget.server;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;

/**
 * Created by Theodore on 2016/4/30.
 */
public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        String time = format.format(System.currentTimeMillis());

        for (int i = 0; i < appWidgetIds.length; ++i) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            Intent intent = new Intent("shun.gao.action.WIDGET_CLICKED");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.button, pendingIntent);
            rv.setTextViewText(R.id.text, time);
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
