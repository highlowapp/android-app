package com.gethighlow.highlowandroid.model.Resources;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.ImageManager;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.HighLowLiveData;
import com.gethighlow.highlowandroid.model.Managers.LiveDataModels.UserLiveData;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Responses.FeedResponse;
import com.gethighlow.highlowandroid.model.Responses.FriendSuggestionsResponse;
import com.gethighlow.highlowandroid.model.Responses.FriendsResponse;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.InterestResponse;
import com.gethighlow.highlowandroid.model.Responses.PendingFriendshipsResponse;
import com.gethighlow.highlowandroid.model.Responses.SearchResponse;
import com.gethighlow.highlowandroid.model.Responses.UserHighLowsResponse;
import com.gethighlow.highlowandroid.model.Services.HighLowService;
import com.gethighlow.highlowandroid.model.Services.UserService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class User {
    private String uid;

    private String firstname;

    private String lastname;

    private String email;

    private String profileimage;

    private int streak;

    private String bio;

    private List<String> interests;

    private String error;

    private Boolean notify_new_friend_req;
    private Boolean notify_new_friend_acc;
    private Boolean notify_new_feed_item;
    private Boolean notify_new_like;
    private Boolean notify_new_comment;

    public String uid() { return uid; }
    public String name() { return firstname + " " + lastname; }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getProfileImageUrl() {
        String url = profileimage;
        if (url == null) return null;
        if (!url.startsWith("http")) {
            return "https://storage.googleapis.com/highlowfiles/" + url;
        }
        return url;
    }

    public String email() { return email; }
    public String profileimage() { return profileimage; }
    public int streak() { return streak; }
    public String bio() { return bio; }
    public List<String> interests() { return interests; }
    public String error() { return error; }

    public String toString() {
        return "Name: " + name();
    }

    public void fetchProfileImage(Consumer<Bitmap> onSuccess, Consumer<String> onError) {
        String url = profileimage;
        if (!url.startsWith("http")) url = "https://storage.googleapis.com/highlowfiles/" + url;

        ImageManager.shared().getImage(url, onSuccess, onError);
    }

    public void setProfile(String firstname, String lastname, String email, String bio, Bitmap profileimage, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        UserService.shared().setProfile(firstname, lastname, email, bio, profileimage, (response) -> {

            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.bio = bio;

            onSuccess.accept(response);

        }, (error) -> {
            onError.accept(error);
        });
    }

    public void getFriends(Consumer<List<UserLiveData>> onSuccess, Consumer<String> onError) {
        UserService.shared().getFriendsForUser(this.uid, friendsResponse -> {
            List<User> friends = friendsResponse.getFriends();
            List<UserLiveData> liveData = new ArrayList<>();
            for (User friend: friends) {
                liveData.add( UserManager.shared().saveUser(friend) );
            }
            onSuccess.accept(liveData);
        }, onError);
    }

    public void requestFriendship(Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        UserService.shared().requestFriend(this.uid, onSuccess, onError);
    }

    public void acceptFriendship(Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        UserService.shared().acceptFriend(this.uid, onSuccess, onError);
    }

    public void unFriend(Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        UserService.shared().unFriend(this.uid, onSuccess, onError);
    }

    public void getPendingFriendships(Consumer<List<UserLiveData>> onSuccess, Consumer<String> onError) {
        UserService.shared().getPendingFriendships(pendingFriendshipsResponse -> {
            List<User> requests = pendingFriendshipsResponse.getRequests();
            List<UserLiveData> liveData = new ArrayList<>();
            for (User request: requests) {
                liveData.add( UserManager.shared().saveUser(request) );
            }
            onSuccess.accept(liveData);
        }, onError);
    }

    public void getHighLows(int page, Consumer<List<HighLowLiveData>> onSuccess, Consumer<String> onError) {
        HighLowService.shared().getHighLowsForUser(this.uid, page, (response) -> {

            List<HighLow> highLows = response.getHighlows();
            List<HighLowLiveData> liveDataList = new ArrayList<>();
            for (HighLow highLow: highLows) {
                liveDataList.add( HighLowManager.shared().saveHighLow(highLow.getHighlowid(), highLow) );
            }

            onSuccess.accept(liveDataList);

        }, onError);
    }

    public static void getFeed(int page, Consumer<List<HighLowLiveData>> onSuccess, Consumer<String> onError) {
        UserService.shared().getFeed(page, (response) -> {
            List<HighLowLiveData> liveData = new ArrayList<>();
            List<FeedItem> highLows = response.getFeed();
            for (FeedItem feedItem: highLows) {
                liveData.add( HighLowManager.shared().saveHighLow(feedItem.getHighlow()) );
            }
            onSuccess.accept(liveData);
        }, onError);
    }

    public void getDate(String date, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().getDate(date, (highLow) -> {
            HighLowManager.shared().saveHighLowForDate(date, highLow);

            onSuccess.accept(highLow);
        }, onError);
    }

    public void getToday(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        HighLowService.shared().getToday(highLow -> {
            HighLowManager.shared().saveTodayHighLow(highLow);

            onSuccess.accept(highLow);
        }, onError);
    }

    public void getInterests(Consumer<InterestResponse> onSuccess, Consumer<String> onError) {
        UserService.shared().getInterests(onSuccess, onError);
    }

    public void createInterest(String name, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        UserService.shared().createInterest(name, onSuccess, onError);
    }

    public void addInterest(String interestId, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        UserService.shared().addInterest(interestId, onSuccess, onError);
    }

    public void removeInterest(String interestId, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        UserService.shared().removeInterest(interestId, onSuccess, onError);
    }

    public void getAllInterests(Consumer<InterestResponse> onSuccess, Consumer<String> onError) {
        UserService.shared().getAllInterests(onSuccess, onError);
    }

    public void getFriendSuggestions(Consumer<List<UserLiveData>> onSuccess, Consumer<String> onError) {
        UserService.shared().getFriendSuggestions(friendSuggestionsResponse -> {
            List<User> users = friendSuggestionsResponse.getUsers();
            List<UserLiveData> friends = new ArrayList<>();
            for (User user: users) {
                friends.add( UserManager.shared().saveUser(user) );
            }
            onSuccess.accept(friends);
        }, onError);
    }

    public void searchUsers(String search, Consumer<List<UserLiveData>> onSuccess, Consumer<String> onError) {
        UserService.shared().searchUsers(search, searchResponse -> {
            List<SearchItem> results = searchResponse.getUsers();
            List<UserLiveData> users = new ArrayList<>();
            Collections.sort(results, (searchItem, t1) -> t1.getRank() - searchItem.getRank());

            for (SearchItem item: results) {
                User user = item.getUser();
                users.add( UserManager.shared().saveUser(user) );
            }
            onSuccess.accept(users);
        }, onError);
    }

}
