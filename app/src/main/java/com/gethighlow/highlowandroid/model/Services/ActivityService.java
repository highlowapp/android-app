package com.gethighlow.highlowandroid.model.Services;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.gethighlow.highlowandroid.model.Managers.ActivityManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Resources.Activity;
import com.gethighlow.highlowandroid.model.Responses.ActivitiesResponse;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.MediaUrlResponse;
import com.gethighlow.highlowandroid.model.Responses.SharingPolicyResponse;
import com.gethighlow.highlowandroid.model.Responses.UserActivitiesResponse;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActivityService {
    private static final ActivityService ourInstance = new ActivityService();

    public static ActivityService shared() { return ourInstance; }

    private Gson gson = new Gson();

     /*String activityId, String uid, String type, String title, String timestamp, String date, Boolean flagged,*/
    public void createActivity(JSONObject data, String type, String date, final Consumer<ActivityLiveData> onSuccess, Consumer<String> onError){

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
                    onSuccess.accept( ActivityManager.shared().saveActivity(activity) );
                }

            }
        }, onError);
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
        }, onError);
    }



    public void getDiaryEntries(int page, final Consumer<List<ActivityLiveData>> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        APIService.shared().authenticatedRequest("/user/diaryEntries", Request.Method.GET, params, new Consumer<String>() {
            @Override
            public void accept(String response) {
                UserActivitiesResponse userActivitiesResponse = gson.fromJson(response, UserActivitiesResponse.class);

                String error = userActivitiesResponse.getError();

                if (error != null) {
                    onError.accept(error);

                } else {
                    List<ActivityLiveData> activityLiveDataList = new ArrayList<ActivityLiveData>();

                    for (Activity activity: userActivitiesResponse.getActivities()) {
                        activityLiveDataList.add( ActivityManager.shared().saveActivity(activity) );
                    }

                    onSuccess.accept(activityLiveDataList);
                }
            }
        }, onError);
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
        }, onError);
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
        }, onError);
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
        }, onError);
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
        }, onError);
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
        }, onError);
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
        }, onError);
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
        }, onError);
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
        }, onError);
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
        }, onError);
    }

    public void uploadImage(Bitmap image, final Consumer<String> onSuccess, final Consumer<String> onError) {

        //Create an empty params object
        Map<String, String> params = new HashMap<String, String>();

        //Make the request
        APIService.shared().makeMultipartRequest("/user/activities/addImage", Request.Method.POST, params, image, new Consumer<String>() {
            @Override
            public void accept(String response) {

                //Get the string as a media url response
                MediaUrlResponse mediaUrlResponse = gson.fromJson(response, MediaUrlResponse.class);

                //Check for an error
                String error = mediaUrlResponse.getError();

                //If there was an error...
                if (error != null) {

                    //Call onError and return
                    onError.accept(error);
                    return;

                }

                //Get the url from the response
                String url = mediaUrlResponse.getUrl();

                //Call onSuccess with the url
                onSuccess.accept(url);

            }
        }, onError);

    }
}
