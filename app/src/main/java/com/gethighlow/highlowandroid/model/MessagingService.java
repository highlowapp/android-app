package com.gethighlow.highlowandroid.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gethighlow.highlowandroid.model.Services.NotificationsService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    public MessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.w("Debug", "RECEIVED MESSAGE");
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        NotificationsService.shared().register(s, notificationsRegisterResponse -> {

        }, error -> {

        });
    }
}
