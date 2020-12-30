package com.gethighlow.highlowandroid.model.Managers;

import com.gethighlow.highlowandroid.model.Resources.Activity;
import android.content.Context;

import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Services.ActivityService;
import com.gethighlow.highlowandroid.model.util.Consumer;

import java.util.HashMap;
import java.util.Map;

public class ActivityManager {

    private static final ActivityManager ourInstance = new ActivityManager();
    public static ActivityManager shared() { return ourInstance; }

    private Map<String, ActivityLiveData> cachedActivities = new HashMap<String, ActivityLiveData>();

    private Context context;

    public void attachToContext(Context context) {
        this.context = context;
    };



    public void getActivity(final String activityId, final Consumer<ActivityLiveData> onSuccess, Consumer<String> onError) {
        ActivityLiveData activity = null;
        if (cachedActivities.containsKey(activityId)) {
            activity = cachedActivities.get(activityId);
        }

        if (activity == null) {
            ActivityService.shared().getActivity(activityId, new Consumer<Activity>() {
                @Override
                public void accept(Activity newActivity) {
                    ActivityLiveData liveDataObject = new ActivityLiveData(newActivity);
                    cachedActivities.put(activityId, liveDataObject);
                    onSuccess.accept(liveDataObject);
                }
            }, onError);
        } else {
            onSuccess.accept(activity);
        }
    }



    public ActivityLiveData saveActivity(Activity activity) {
        if ( cachedActivities.containsKey(activity.getActivityId()) ) {
            ActivityLiveData liveData = cachedActivities.get(activity.getActivityId());
            return liveData;
        } else {
            ActivityLiveData liveData = new ActivityLiveData(activity);
            cachedActivities.put(activity.getActivityId(), liveData);
            return liveData;
        }
    }




}
