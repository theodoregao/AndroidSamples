package com.example.samplenotification;

import android.app.NotificationManager;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NotificationSample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_sample);
    }

    public void onClick(View view) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        for (StatusBarNotification statusBarNotification: notificationManager.getActiveNotifications()) {

        }
    }
}
