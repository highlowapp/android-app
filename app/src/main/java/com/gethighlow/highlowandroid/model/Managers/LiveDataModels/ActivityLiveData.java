package com.gethighlow.highlowandroid.model.Managers.LiveDataModels;

import com.gethighlow.highlowandroid.model.Resources.Activity;
import com.gethighlow.highlowandroid.model.Resources.HighLow;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Responses.SharingPolicyResponse;
import com.gethighlow.highlowandroid.model.Services.ActivityService;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class ActivityLiveData extends MutableLiveData<Activity> {
    private Activity activity;
    /* LiveData-specific stuff */
    public ActivityLiveData(Activity activity) {
        super();
        this.activity = activity;
        this.setValue(activity);
    }

    public Activity getActivity() {
        return this.getValue();
    }

    /* Action methods */
    public Activity delete(Consumer<Activity> onSuccess, Consumer<String> onError) {
        final Activity activity = this.getValue();
        assert activity != null;
        ActivityService.shared().deleteActivity(activity.getActivityId(), new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {
                //Update our value
                ActivityLiveData.this.setValue(activity);

                //Call onSuccess
                onSuccess.accept(activity);
            }
        }, onError); //Note that we simply pass the onError function through
        return activity;
    }


    public Activity create(JSONObject data, String type, String date, Consumer<Activity> onSuccess, Consumer<String> onError) {
        final Activity activity = this.getValue();
        assert activity != null;
        ActivityService.shared().createActivity(data, type, date, new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {
                //Update our value
                ActivityLiveData.this.setValue(activity);

                //Call onSuccess
                onSuccess.accept(activity);
            }
        }, onError); //Note that we simply pass the onError function through
        return activity;
    }


    public Activity get(Consumer<Activity> onSuccess, Consumer<String> onError) {
        final Activity activity = this.getValue();
        assert activity != null;
        ActivityService.shared().getActivity(activity.getActivityId(), new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {
                //Update our value
                ActivityLiveData.this.setValue(activity);

                //Call onSuccess
                onSuccess.accept(activity);
            }
        }, onError); //Note that we simply pass the onError function through
        return activity;
    }

    public void getDiaryEntries(int page, Consumer<List<ActivityLiveData>> onSuccess, Consumer<String> onError) {
        Activity activity = getValue();
        if (activity == null) { onError.accept("does-not-exist"); }
        activity.getDiaryEntries(page, onSuccess, onError);
    }

    public String getActivityId(){
        Activity activity = getValue();
        if(activity == null) return null;
        return activity.getActivityId();
    }

    public String getTitle(){
        Activity activity = getValue();
        if(activity == null) return null;
        return activity.getTitle();
    }

    public String getType(){
        Activity activity = getValue();
        if (activity == null) return null;
        return activity.getType();
    }

    public String getActivityImage() {
        Activity activity = getValue();
        if (activity == null) return null;
        return activity.getActivityImage();
    }

    public Activity update(JSONObject data, String type, String date, Consumer<Activity> onSuccess, Consumer<String> onError) {
        final Activity activity = this.getValue();
        assert activity != null;
        ActivityService.shared().updateActivity(activity.getActivityId(), data, new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {
                //Update our value
                ActivityLiveData.this.setValue(activity);

                //Call onSuccess
                onSuccess.accept(activity);
            }
        }, onError); //Note that we simply pass the onError function through
        return activity;
    }


    public Activity flag(Consumer<Activity> onSuccess, Consumer<String> onError) {
        final Activity activity = this.getValue();
        assert activity != null;
        ActivityService.shared().flag(activity.getActivityId(), new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {
                //Update our value
                ActivityLiveData.this.setValue(activity);

                //Call onSuccess
                onSuccess.accept(activity);
            }
        }, onError); //Note that we simply pass the onError function through
        return activity;
    }


    public Activity unFlag(Consumer<Activity> onSuccess, Consumer<String> onError) {
        final Activity activity = this.getValue();
        assert activity != null;
        ActivityService.shared().unFlag(activity.getActivityId(), new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {
                //Update our value
                ActivityLiveData.this.setValue(activity);

                //Call onSuccess
                onSuccess.accept(activity);
            }
        }, onError); //Note that we simply pass the onError function through
        return activity;
    }

    public Activity comment(String message, Consumer<Activity> onSuccess, Consumer<String> onError) {
        final Activity activity = this.getValue();
        assert activity != null;
        ActivityService.shared().comment(activity.getActivityId(), message, new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {
                //Update our value
                ActivityLiveData.this.setValue(activity);

                //Call onSuccess
                onSuccess.accept(activity);
            }
        }, onError); //Note that we simply pass the onError function through
        return activity;
    }


    public Activity getSharingPolicy(Consumer<SharingPolicyResponse> onSuccess, Consumer<String> onError) {
        final Activity activity = this.getValue();
        assert activity != null;
        ActivityService.shared().getSharingPolicy(activity.getActivityId(), new Consumer<SharingPolicyResponse>() {
            @Override
            public void accept(SharingPolicyResponse response){
                onSuccess.accept(response);
                ActivityLiveData.this.setValue(activity);
            }

        }, onError); //Note that we simply pass the onError function through
        return activity;
    }


    public Activity setSharingPolicy(String category, List<String> uids, Consumer<Activity> onSuccess, Consumer<String> onError) {
        final Activity activity = this.getValue();
        assert activity != null;
        if(!category.equals("uids")){
            uids = null;
        }
        ActivityService.shared().setSharingPolicy(activity.getActivityId(), category, uids, new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {
                //Update our value
                ActivityLiveData.this.setValue(activity);

                //Call onSuccess
                onSuccess.accept(activity);
            }
        }, onError); //Note that we simply pass the onError function through
        return activity;
    }



}
