package com.gethighlow.highlowandroid.model.Services;

import com.android.volley.Request;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Resources.Activity;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.SharingPolicyResponse;
import com.gethighlow.highlowandroid.model.Responses.UserActivitiesResponse;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActivityService {
    private static final ActivityService ourInstance = new ActivityService();

    public static ActivityService shared() { return ourInstance; }

    private Gson gson = new Gson();




     /*String activityId, String uid, String type, String title, String timestamp, String date, Boolean flagged,*/
    public void createActivity(JSONObject data, String type, String date, final Consumer<Activity> onSuccess, Consumer<String> onError){

        Map<String, String> params = new HashMap<String, String>();
        params.put("data", data.toString());
        params.put("type", type);
        params.put("date", date);
        params.put("request_id", UUID.randomUUID().toString());


        APIService.shared().authenticatedRequest("/user/activities", Request.Method.POST, params, new Consumer<String>() {
            @Override
            public void accept(String response) {
                Activity activity = gson.fromJson(response, Activity.class);


                String error = activity.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(activity);
                }

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }




    public void getActivity(String activityId, final Consumer<Activity> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/activities/" + activityId, Request.Method.GET, null, new Consumer<String>() {
            @Override
            public void accept(String response) {
                Activity activity = gson.fromJson(response, Activity.class);

                String error = activity.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(activity);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }



    public void getDiaryEntries(int page, final Consumer<UserActivitiesResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        APIService.shared().authenticatedRequest("/user/diaryEntries", Request.Method.GET, params, new Consumer<String>() {
            @Override
            public void accept(String response) {
                UserActivitiesResponse userActivitiesResponse = gson.fromJson(response, UserActivitiesResponse.class);


                String activityId = userActivitiesResponse.getActivityId();

                String error = userActivitiesResponse.getError();

                if (error != null) {
                    onError.accept(error);

                } else {
                    onSuccess.accept(userActivitiesResponse);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }



    public void updateActivity(String activityId, JSONObject data, final Consumer<Activity> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("data", data.toString());
        params.put("request_id", UUID.randomUUID().toString());
        APIService.shared().authenticatedRequest("/user/activities/" + activityId, Request.Method.POST, params, new Consumer<String>() {
            @Override
            public void accept(String response) {
                Activity activity = gson.fromJson(response, Activity.class);

                String error = activity.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(activity);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }
    public void deleteActivity(String activityId, final Consumer<Activity> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/activities/" + activityId, Request.Method.DELETE, null, new Consumer<String>() {
            @Override
            public void accept(String response) {
                Activity activity = gson.fromJson(response, Activity.class);

                String error = activity.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(activity);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }


    public void flag(String activityId, final Consumer<Activity> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/activities/" + activityId + "/flag", Request.Method.POST, null, new Consumer<String>() {
            @Override
            public void accept(String response) {

                Activity activity = gson.fromJson(response, Activity.class);

                String error = activity.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(activity);
                }

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }


    public void unFlag(String activityId, final Consumer<Activity> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/activities/" + activityId + "/flag", Request.Method.DELETE, null, new Consumer<String>() {
            @Override
            public void accept(String response) {

                Activity activity = gson.fromJson(response, Activity.class);

                String error = activity.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(activity);
                }

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }



    public void comment(String activityId, String message, final Consumer<Activity> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("message", message);
        params.put("request_id", UUID.randomUUID().toString());
        APIService.shared().authenticatedRequest("/user/activities/" + activityId + "/comments", Request.Method.POST, params, new Consumer<String>() {
            @Override
            public void accept(String response) {

                Activity activity = gson.fromJson(response, Activity.class);

                String error = activity.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(activity);
                }

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }


    public void updateComment(String commentId, String message, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("message", message);
        params.put("request_id", UUID.randomUUID().toString());
        APIService.shared().authenticatedRequest("/user/comments/" + commentId, Request.Method.POST, params, new Consumer<String>() {
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


    public void deleteComment(String commentId, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("request_id", UUID.randomUUID().toString());
        APIService.shared().authenticatedRequest("/user/comments/" + commentId, Request.Method.DELETE, params, new Consumer<String>() {
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

    public void getSharingPolicy(String activityId, final Consumer<SharingPolicyResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/activities/" + activityId + "/sharing", Request.Method.GET, null, new Consumer<String>() {

            @Override
            public void accept(String response) {
                SharingPolicyResponse sharingPolicyResponse = gson.fromJson(response, SharingPolicyResponse.class);

                String error = sharingPolicyResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(sharingPolicyResponse);
                }


            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void setSharingPolicy(String activityId, String category, List<String> uids, final Consumer<Activity> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("category", category);
        if(category.equals("uids")){
            if(uids != null){
                params.put("uids", uids.toString());
            }
        }
        params.put("request_id", UUID.randomUUID().toString());
        APIService.shared().authenticatedRequest("/user/activities/" + activityId + "/sharing", Request.Method.POST, params, new Consumer<String>() {
            @Override
            public void accept(String response) {

                Activity activity = gson.fromJson(response, Activity.class);

                String error = activity.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(activity);
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