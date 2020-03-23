package com.gethighlow.highlowandroid.model.Services;

import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class BugReportsService {
    private static final BugReportsService ourInstance = new BugReportsService();
    public static BugReportsService shared() { return ourInstance; }

    private Gson gson = new Gson();

    public void submitBugReport(final String title, final String message, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("title", title);
            put("message", message);
        }};
        APIService.shared().authenticatedRequest("/bug_reports/submit", 1, params, new Consumer<String>() {
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
