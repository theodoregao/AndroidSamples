package shun.gao.widget.server;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;

class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mAppWidgetId;

    public RemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        String time = format.format(System.currentTimeMillis());

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_layout);
        Intent intent = new Intent("shun.gao.action.WIDGET_CLICKED");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.button, pendingIntent);
        rv.setTextViewText(R.id.text, time);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {

    }
}