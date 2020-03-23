package com.gethighlow.highlowandroid.model.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.gethighlow.highlowandroid.Activities.Other.TabActivity;
import com.gethighlow.highlowandroid.R;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

public class AlarmNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "com.gethighlow.dailyReminder");

        Intent myIntent = new Intent(context, TabActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                FLAG_ONE_SHOT );

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.notif_logo)
                .setContentTitle("Your Daily Reminder")
                .setContentIntent(pendingIntent)
                .setContentText("Reflect on today and enter a High/Low")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }
}