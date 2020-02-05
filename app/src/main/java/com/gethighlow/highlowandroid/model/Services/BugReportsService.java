package com.gethighlow.highlowandroid.model.Services;

import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BugReportsService {
    private static final BugReportsService ourInstance = new BugReportsService();
    public static BugReportsService shared() { return ourInstance; }

    private Gson gson = new Gson();

    public void submitBugReport(String title, String message, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("title", title);
            put("message", message);
        }};
        APIService.shared().authenticatedRequest("/bug_reports/submit", 1, params, (response) -> {
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
