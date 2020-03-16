package com.gethighlow.highlowandroid.model.Services;

import android.graphics.Bitmap;

import com.gethighlow.highlowandroid.model.Resources.HighLow;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.UserHighLowsResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HighLowService {
    private static final HighLowService ourInstance = new HighLowService();

    public static HighLowService shared() { return ourInstance; }

    private Gson gson = new Gson();

    public void setHigh(String high, String date, String highlowid, Boolean isPrivate, Bitmap image, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("high", high);
        params.put("private", (isPrivate) ? "true": "false");
        if (date != null) {
            params.put("date", date);
        }
        if (highlowid != null) {
            params.put("highlowid", highlowid);
        }

        APIService.shared().makeMultipartRequest("/highlow/set/high", 1, params, image, (response) -> {
            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void setLow(String low, String date, String highlowid, Boolean isPrivate, Bitmap image, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("low", low);
        params.put("private", (isPrivate) ? "true": "false");
        if (date != null) {
            params.put("date", date);
        }
        if (highlowid != null) {
            params.put("highlowid", highlowid);
        }

        APIService.shared().makeMultipartRequest("/highlow/set/low", 1, params, image, (response) -> {

            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void makePrivate(String highlowid, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/highlow/" + highlowid + "/private/1", 0, null, (response) -> {

            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void makePublic(String highlowid, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/highlow/" + highlowid + "/private/0", 0, null, (response) -> {

            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void like(String highlowid, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/highlow/like/" + highlowid, 1, null, (response) -> {

            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void unLike(String highlowid, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/highlow/unlike/" + highlowid, 1, null, (response) -> {

            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void comment(String highlowid, String message, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("message", message);
        APIService.shared().authenticatedRequest("/highlow/comment/" + highlowid, 1, params, (response) -> {

            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void getToday(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/highlow/get/today", 0, null, (response) -> {
            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void get(String highlowid, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/highlow/" + highlowid, 0, null, (response) -> {
            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void getDate(String date, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("date", date);

        APIService.shared().authenticatedRequest("/highlow/get/date", 1, params, (response) -> {
            HighLow highLow = gson.fromJson(response, HighLow.class);
            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void getHighLowsForUser(String uid, int page, Consumer<UserHighLowsResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        if (uid != null) {
            params.put("uid", uid);
        }
        APIService.shared().authenticatedRequest("/highlow/get/user/page/" + page, 0, params, (response) -> {
            UserHighLowsResponse userHighLowsResponse = gson.fromJson(response, UserHighLowsResponse.class);

            String error = userHighLowsResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(userHighLowsResponse);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void flag(String highlowid, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/highlow/flag/" + highlowid, 1, null, (response) -> {

            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void unFlag(String highlowid, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/highlow/unflag/" + highlowid, 1, null, (response) -> {

            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void getComments(String highlowid, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/highlow/get_comments/" + highlowid, 1, null, (response) -> {

            HighLow highLow = gson.fromJson(response, HighLow.class);

            String error = highLow.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(highLow);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void updateComment(String commentid, String message, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("message", message);
        APIService.shared().authenticatedRequest("/comment/update/" + commentid, 1, params, (response) -> {
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

    public void deleteComment(String commentid, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/comment/delete/" + commentid, 1, null, (response) -> {
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
