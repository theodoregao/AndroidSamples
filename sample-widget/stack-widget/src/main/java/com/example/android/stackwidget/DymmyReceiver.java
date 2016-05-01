package com.example.android.stackwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DymmyReceiver extends BroadcastReceiver {
    public DymmyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent activityIntent = new Intent(context, DummyActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}
