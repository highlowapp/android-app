package com.gethighlow.highlowandroid.model.Services;

import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.NotificationsRegisterResponse;
import com.gethighlow.highlowandroid.model.Responses.NotificationsSettingsResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class NotificationsService {
    private static final NotificationsService ourInstance = new NotificationsService();
    public static NotificationsService shared() { return ourInstance; }

    private Gson gson = new Gson();

    public void register(String deviceId, Consumer<NotificationsRegisterResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("platform", "1");
            put("device_id", deviceId);
        }};
        APIService.shared().authenticatedRequest("/notifications/register", 1, params, (response) -> {
            NotificationsRegisterResponse notificationsRegisterResponse = gson.fromJson(response, NotificationsRegisterResponse.class);

            String error = notificationsRegisterResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(notificationsRegisterResponse);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void deRegister(String deviceId, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/notifications/deregister/" + deviceId, 1, null, (response) -> {
            GenericResponse genericResponse = gson.fromJson(response, GenericResponse.class);

            String error = genericResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(genericResponse);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void getNotificationSettings(Consumer<NotificationsSettingsResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/notifications/settings", 0, null, (response) -> {
            NotificationsSettingsResponse notificationsSettingsResponse = gson.fromJson(response, NotificationsSettingsResponse.class);

            String error = notificationsSettingsResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(notificationsSettingsResponse);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void turnOn(String setting, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/notifications/" + setting + "/on", 1, null, (response) -> {
            GenericResponse genericResponse = gson.fromJson(response, GenericResponse.class);

            String error = genericResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(genericResponse);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void turnOff(String setting, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/notifications/" + setting + "/off", 1, null, (response) -> {
            GenericResponse genericResponse = gson.fromJson(response, GenericResponse.class);

            String error = genericResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(genericResponse);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }
}
