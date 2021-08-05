package com.gethighlow.highlowandroid.model.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Resources.Activity;
import com.gethighlow.highlowandroid.model.Services.ActivityService;
import com.gethighlow.highlowandroid.model.util.Consumer;

import java.util.ArrayList;
import java.util.List;

public class DiaryViewModel extends ViewModel {

    //The entries for the Diary
    private MutableLiveData<List<ActivityLiveData>> entries;

    //This allows the activity to observe the list
    public LiveData<List<ActivityLiveData>> getEntries() {

        //If we don't have any entries yet...
        if (entries == null) {

            //Instantiate the entries list
            entries = new MutableLiveData<List<ActivityLiveData>>();

            //Load them asynchronously
            loadEntries(0);
        }

        //Return the entries
        return entries;

    }

    public void deleteEntry(ActivityLiveData entry) {

        //Get the activity id
        String activityId = entry.getActivityId();

        //Make the request to delete it
        ActivityService.shared().deleteActivity(activityId, new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {

                //Get the current items
                List<ActivityLiveData> entries = new ArrayList<ActivityLiveData>( DiaryViewModel.this.entries.getValue() );

                //Delete this item
                entries.remove(entry);

                //Now, update the list
                DiaryViewModel.this.entries.setValue(entries);

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String s) {

                //Show an alert or otherwise notify the user of the issue


            }
        });



    }

    public void loadEntries(int page) {

        //Make a request to the server for the diary entries at that page
        ActivityService.shared().getDiaryEntries(page, new Consumer<List<ActivityLiveData>>() {
            @Override
            public void accept(List<ActivityLiveData> activityLiveData) {

                //Once we have the new data, we need to update our list.

                //Create a placeholder list
                List<ActivityLiveData> entriesList;

                //If we're on page 0, start with an empty list
                if (page == 0) {
                    entriesList = new ArrayList<ActivityLiveData>();
                }

                //Otherwise, we'll start with the items we already have
                else {
                    entriesList = DiaryViewModel.this.entries.getValue();
                }

                //Next, we'll append all the new items
                entriesList.addAll(activityLiveData);

                //And finally, we'll update the value of our liveData entries list
                DiaryViewModel.this.entries.setValue(entriesList);

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String s) {

                //Not sure what to do yet if there's an error...we might not be able to show an alert since this isn't an activity or fragment
                Log.i("Debug", s);

            }
        });

    }
}
