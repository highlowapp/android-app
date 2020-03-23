package com.gethighlow.highlowandroid.model.Services;

import androidx.annotation.NonNull;

import com.gethighlow.highlowandroid.model.Responses.NotificationsRegisterResponse;
import com.gethighlow.highlowandroid.model.Services.NotificationsService;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    public MessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        NotificationsService.shared().register(s, new Consumer<NotificationsRegisterResponse>() {
            @Override
            public void accept(NotificationsRegisterResponse notificationsRegisterResponse) {

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {

            }
        });
    }
}
