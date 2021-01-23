package com.gethighlow.highlowandroid.model.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.ActivityLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Resources.Activity;
import com.gethighlow.highlowandroid.model.Resources.SharingPolicy;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Resources.UserSearchResult;
import com.gethighlow.highlowandroid.model.Responses.FriendsResponse;
import com.gethighlow.highlowandroid.model.Responses.SharingPolicyResponse;
import com.gethighlow.highlowandroid.model.Services.ActivityService;
import com.gethighlow.highlowandroid.model.Services.UserService;
import com.gethighlow.highlowandroid.model.util.Consumer;

import org.apache.commons.lang3.mutable.Mutable;

import java.util.ArrayList;
import java.util.List;

public class SharingOptionsViewModel extends ViewModel {

    //The activity id
    private String activityId;

    //The activity
    private ActivityLiveData activityLiveData;

    //The current selection
    private MutableLiveData<SharingPolicy> selection;

    //The sharing policy
    private MutableLiveData<SharingPolicy> sharingPolicy;

    //The friends
    private MutableLiveData<List<UserLiveData>> friends;

    //Set the activity id
    public void setActivityId(String activityId) {

        this.activityId = activityId;

    }

    //Get the current selection
    public LiveData<SharingPolicy> getSelection() {

        //If there isn't a selection yet, create one
        if (selection == null) selection = new MutableLiveData<SharingPolicy>();

        //Return a selection
        return selection;

    }

    //Set the current selection
    public void setSelected(SharingPolicy selection) {

        //If the selection has no uids, give it the ones from the previous selection
        if (selection.getUids() == null && this.selection.getValue() != null) {

            SharingPolicy currentSelection = this.selection.getValue();
            selection.setUids( currentSelection.getUids() );

        }

        //Set the value
        this.selection.setValue(selection);

    }

    //Uid updating methods
    public void addToUids(String uid) {

        //Get the current selection
        SharingPolicy selection = this.selection.getValue();

        if (selection != null) {

            //If the uids are null, create an empty array for them
            if (selection.getUids() == null) selection.setUids( new ArrayList<String>() );

            //Add the uid
            selection.getUids().add(uid);

        }
    }

    public void removeFromUids(String uid) {

        //Get the current selection
        SharingPolicy selection = this.selection.getValue();

        //If the selection is not null...
        if (selection != null) {

            //Remove all occurrences of that uid
            while ( selection.getUids().remove(uid) ) {}

        }

    }

    //Allow the activity to observe the sharing policy
    public LiveData<SharingPolicy> getSharingPolicy() {

        //If the policy is null...
        if (sharingPolicy == null) {

            //Create an empty livedata
            sharingPolicy = new MutableLiveData<SharingPolicy>();

            //Load the sharing policy
            loadSharingPolicy();

        }

        //Return the policy
        return sharingPolicy;

    }

    //Load the sharing policy from the API
    private void loadSharingPolicy() {

        //Make a request to the API to get the policy
        ActivityService.shared().getSharingPolicy(activityId, new Consumer<SharingPolicy>() {
            @Override
            public void accept(SharingPolicy sharingPolicyResponse) {

                //Set the sharing policy
                sharingPolicy.setValue(sharingPolicyResponse);

                //Set the current selection as well
                selection.setValue(sharingPolicyResponse);

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String s) {

                //TODO: Decide what to do with the error...
                Log.e("Error", s);

            }
        });

    }

    //Set the sharing policy
    public void setSharingPolicy(Runnable onCompletion) {

        //Get the sharing policy from the selection
        SharingPolicy sharingPolicy = selection.getValue();

        //Make an API request to set the selection
        ActivityService.shared().setSharingPolicy(activityId, sharingPolicy.getSharingPolicy(), sharingPolicy.getUids(), new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) {

                //Set the sharing policy
                SharingOptionsViewModel.this.sharingPolicy.setValue(sharingPolicy);

                //Set the current selection as well
                selection.setValue(sharingPolicy);

                //Run the completion
                onCompletion.run();

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String s) {

                Log.e("Error", s);

                //Run the completion
                onCompletion.run();

            }
        });

    }

    public LiveData<List<UserLiveData>> getFriends() {

        //If there are no friends loaded yet, create an empty live data and load them
        if (friends == null) {

            //Create an empty livedata
            friends = new MutableLiveData<List<UserLiveData>>();

            //Load the friends
            loadFriends();

        }

        //Return the friends
        return friends;
    }

    public void loadFriends() {

        //Make the API request to get the friends
        UserService.shared().getFriends(new Consumer<FriendsResponse>() {
            @Override
            public void accept(FriendsResponse friendsResponse) {

                //Get the list of users
                List<User> users = friendsResponse.getFriends();

                //Convert it to livedata
                List<UserLiveData> friendsList = new ArrayList<UserLiveData>();

                for (User user: users) {
                    friendsList.add( UserManager.shared().saveUser(user) );
                }

                //Update the value of the friends list
                friends.setValue(friendsList);

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String s) {

                //TODO: Handle error
                Log.e("Error", s);

            }
        });

    }

}
