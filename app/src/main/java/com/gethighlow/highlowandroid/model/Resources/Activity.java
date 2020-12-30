package com.gethighlow.highlowandroid.model.Resources;

import com.gethighlow.highlowandroid.model.Managers.ActivityManager;

import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Responses.UserActivitiesResponse;
import com.gethighlow.highlowandroid.model.Services.ActivityService;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private String error;
    private String activityId;
    private String uid;
    private String type;
    private String title;
    private String timestamp;
    JSONObject data = new JSONObject();
    private String date;
    private Boolean flagged = false;

    @SerializedName("activity_image")
    private String activityImage;

    private List<ActivityComment> comments;


    public String toString() {
        return "{\n\tactivityId: " + (activityId == null ? "null":activityId) + "\n\tuid: " + (uid == null ? "null":uid) + "\n\ttype: " + (type == null ? "null": type) + "\n\ttitle: " + (title == null ? "null":title) + "\n\ttimestamp: " + (timestamp == null ? "null": timestamp) + "\n\tdate: " + (date == null ? "null": date) +"\n\tflagged: " + (flagged == null ? "null": flagged);
    }


    public String getError(){
        return error;
    }

    public String getActivityId(){
        return activityId;
    }

    public String getUid(){
        return uid;
    }

    public String getType(){
        return type;
    }

    public String getActivityImage() {
        return activityImage;
    }

    public String getTitle(){
        return title;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public String getDate(){
        return date;
    }

    public Boolean getFlagged(){
        return flagged;
    }

    public List<ActivityComment> getComments(){
        return comments;
    }



    public void update(String key, Object value){
        if (value == null) return;
        if ("activityId".equals(key)){
            this.activityId = value.toString();
            /*data.put("activityId", activityId);*/

        }else if("uid".equals(key)){
            this.uid = value.toString();
            /*data.put("uid", uid);*/

        }else if("type".equals(key)){
            this.type = value.toString();
            /*data.put("type", type);*/

        }else if("activityImage".equals(key)){
            this.activityImage = value.toString();

        }else if("title".equals(key)){
            this.title = value.toString();
            /*data.put("title", title);*/

        }else if("timestamp".equals(key)){
            this.timestamp = value.toString();
            /*data.put("timestamp", timestamp);*/

        }else if("date".equals(key)){
            this.date = value.toString();
            /*data.put("date", date);*/

        }else if("flagged".equals(key)){
            this.flagged = (Boolean) value;
            /*data.put("flagged", flagged);*/

        }else if ("comments".equals(key)) {
            this.comments = (List<ActivityComment>) value;
        }

    }



    public void flag(final Consumer<Activity> onSuccess, Consumer<String> onError) {
        ActivityService.shared().flag(this.activityId, new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {
                Activity.this.update("flagged", true);
                ActivityManager.shared().saveActivity(Activity.this);
                onSuccess.accept(activity);
            }
        }, onError);
    }

    public void unFlag(final Consumer<Activity> onSuccess, Consumer<String> onError) {
        ActivityService.shared().unFlag(this.activityId, new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {
                Activity.this.update("flagged", false);
                ActivityManager.shared().saveActivity(Activity.this);
                onSuccess.accept(activity);
            }
        }, onError);
    }


    public void comment(String message, final Consumer<Activity> onSuccess, Consumer<String> onError) {
        ActivityService.shared().comment(this.activityId, message, new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {
                Activity.this.update("comments", comments);
                ActivityManager.shared().saveActivity(Activity.this);
                onSuccess.accept(activity);
            }
        }, onError);
    }



    public void getDiaryEntries(int page, final Consumer<List<ActivityLiveData>> onSuccess, Consumer<String> onError){

        ActivityService.shared().getDiaryEntries(page, new Consumer<UserActivitiesResponse>() {
            @Override
            public void accept(UserActivitiesResponse response) {
                List<Activity> activities = response.getActivities();

                List<ActivityLiveData> liveDataList = new ArrayList<ActivityLiveData>();

                for(Activity activity : activities) {
                    liveDataList.add(ActivityManager.shared().saveActivity(activity));
                }


                onSuccess.accept(liveDataList);

            }

        }, onError);
    }

    public void updateDataWithActivity(Activity activity){
        update("activityId", activity.getActivityId());
        update("uid", activity.getUid());
        update("type", activity.getType());
        update("activityImage", activity.getActivityImage());
        update("title", activity.getTitle());
        update("timestamp", activity.getTimestamp());
        update("date", activity.getDate());
        update("flagged", activity.getFlagged());
    }


}

