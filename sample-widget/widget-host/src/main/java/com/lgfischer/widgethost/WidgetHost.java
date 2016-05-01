package com.lgfischer.widgethost;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

public class WidgetHost extends Activity {

    private static final String TAG = WidgetHost.class.getSimpleName();

    private static final int BIND_WIDGET_REQUEST_CODE = 0XFF01;

    AppWidgetManager mAppWidgetManager;
    AppWidgetHost mAppWidgetHost;
    int mAppWidgetId;
    AppWidgetProviderInfo mAppWidgetProviderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_host);

        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetHost = new AppWidgetHost(this, hashCode());

        mAppWidgetId = mAppWidgetHost.allocateAppWidgetId();

        Intent bindWidgetIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_BIND);
        bindWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

        for (AppWidgetProviderInfo appWidgetProviderInfo : mAppWidgetManager.getInstalledProviders()) {
            if (appWidgetProviderInfo.toString().contains("shun.gao.widget.server")) {
                mAppWidgetProviderInfo = appWidgetProviderInfo;
                bindWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER,
                        appWidgetProviderInfo.provider);
            }
        }

        startActivityForResult(bindWidgetIntent, BIND_WIDGET_REQUEST_CODE);

        mAppWidgetHost.startListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v(TAG, "requestCode: " + requestCode);
        Log.v(TAG, "resultCode: " + requestCode);

        if (resultCode == RESULT_OK) {
            AppWidgetHostView hostView = mAppWidgetHost.createView(this, mAppWidgetId, mAppWidgetProviderInfo);
            hostView.setAppWidget(mAppWidgetId, mAppWidgetProviderInfo);
            ((ViewGroup) findViewById(R.id.content)).addView(hostView);
        } else if (requestCode == RESULT_CANCELED) {
            Log.e(TAG, "canceled");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAppWidgetHost.stopListening();
    }
}
