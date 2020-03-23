package com.gethighlow.highlowandroid.model.Services;

import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.NotificationsRegisterResponse;
import com.gethighlow.highlowandroid.model.Responses.NotificationsSettingsResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class NotificationsService {
    private static final NotificationsService ourInstance = new NotificationsService();
    public static NotificationsService shared() { return ourInstance; }

    private Gson gson = new Gson();

    public void register(final String deviceId, final Consumer<NotificationsRegisterResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("platform", "1");
            put("device_id", deviceId);
        }};
        APIService.shared().authenticatedRequest("/notifications/register", 1, params, new Consumer<String>() {
            @Override
            public void accept(String response) {
                NotificationsRegisterResponse notificationsRegisterResponse = gson.fromJson(response, NotificationsRegisterResponse.class);

                String error = notificationsRegisterResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(notificationsRegisterResponse);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void deRegister(String deviceId, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/notifications/deregister/" + deviceId, 1, null, new Consumer<String>() {
            @Override
            public void accept(String response) {
                GenericResponse genericResponse = gson.fromJson(response, GenericResponse.class);

                String error = genericResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(genericResponse);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void getNotificationSettings(final Consumer<NotificationsSettingsResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/notifications/settings", 0, null, new Consumer<String>() {
            @Override
            public void accept(String response) {
                NotificationsSettingsResponse notificationsSettingsResponse = gson.fromJson(response, NotificationsSettingsResponse.class);

                String error = notificationsSettingsResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(notificationsSettingsResponse);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void turnOn(String setting, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/notifications/" + setting + "/on", 1, null, new Consumer<String>() {
            @Override
            public void accept(String response) {
                GenericResponse genericResponse = gson.fromJson(response, GenericResponse.class);

                String error = genericResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(genericResponse);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void turnOff(String setting, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/notifications/" + setting + "/off", 1, null, new Consumer<String>() {
            @Override
            public void accept(String response) {
                GenericResponse genericResponse = gson.fromJson(response, GenericResponse.class);

                String error = genericResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(genericResponse);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }
}
