package com.gethighlow.highlowandroid.model.Managers.LiveDataModels;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import com.gethighlow.highlowandroid.model.Managers.HighLowManager;
import com.gethighlow.highlowandroid.model.Managers.UserManager;
import com.gethighlow.highlowandroid.model.Resources.FeedItem;
import com.gethighlow.highlowandroid.model.Resources.HighLow;
import com.gethighlow.highlowandroid.model.Resources.SearchItem;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Responses.FeedResponse;
import com.gethighlow.highlowandroid.model.Responses.FriendSuggestionsResponse;
import com.gethighlow.highlowandroid.model.Responses.FriendsResponse;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.InterestResponse;
import com.gethighlow.highlowandroid.model.Responses.PendingFriendshipsResponse;
import com.gethighlow.highlowandroid.model.Responses.SearchResponse;
import com.gethighlow.highlowandroid.model.Responses.UserHighLowsResponse;

import java.util.List;
import java.util.function.Consumer;

public class UserLiveData extends MutableLiveData<User> {
    public UserLiveData(User user) {
        super();
        this.setValue(user);
    }

    public User getUser() { return this.getValue(); }

    public void setProfile(String firstname, String lastname, String email, String bio, Bitmap profileimage, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.setProfile(firstname, lastname, email, bio, profileimage, onSuccess, onError);
    }

    public void getFriends(Consumer<FriendsResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.getFriends(friendsResponse -> {
            List<User> users = friendsResponse.getFriends();
            for (User thisUser: users) {
                UserManager.shared().saveUser(thisUser);
            }

            onSuccess.accept(friendsResponse);
        }, onError);
    }

    public void requestFriendship(Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.requestFriendship(onSuccess, onError);
    }

    public void acceptFriendship(Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.acceptFriendship(onSuccess, onError);
    }

    public void unFriend(Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.unFriend(onSuccess, onError);
    }

    public void getPendingFriendships(Consumer<PendingFriendshipsResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.getPendingFriendships(pendingFriendshipsResponse -> {
            List<User> requests = pendingFriendshipsResponse.getRequests();
            for (User thisUser: requests) {
                UserManager.shared().saveUser(thisUser);
            }
            onSuccess.accept(pendingFriendshipsResponse);
        }, onError);
    }

    public void getHighLows(int page, Consumer<List<HighLowLiveData>> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.getHighLows(page, onSuccess, onError);
    }

    public void getFeed(int page, Consumer<FeedResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.getFeed(page, feedResponse -> {
            List<FeedItem> feedItems = feedResponse.getFeed();
            for (FeedItem feedItem: feedItems) {
                HighLowManager.shared().saveHighLow(feedItem.getHighlow());
            }
            onSuccess.accept(feedResponse);
        }, onError);
    }

    public void getDate(String date, Consumer<HighLow> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.getDate(date, onSuccess, onError);
    }

    public void getToday(Consumer<HighLow> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.getToday(onSuccess, onError);
    }

    public void getInterests(Consumer<InterestResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.getInterests(onSuccess, onError);
    }

    public void createInterest(String name, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.createInterest(name, onSuccess, onError);
    }

    public void addInterest(String interestId, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.addInterest(interestId, onSuccess, onError);
    }

    public void removeInterest(String interestId, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.removeInterest(interestId, onSuccess, onError);
    }

    public void getAllInterests(Consumer<InterestResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.getAllInterests(onSuccess, onError);
    }

    public void getFriendSuggestions(Consumer<FriendSuggestionsResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.getFriendSuggestions(friendSuggestionsResponse -> {
            List<User> users = friendSuggestionsResponse.getUsers();
            for (User thisUser: users) {
                UserManager.shared().saveUser(thisUser);
            }
            onSuccess.accept(friendSuggestionsResponse);
        }, onError);
    }

    public void searchUsers(String search, Consumer<SearchResponse> onSuccess, Consumer<String> onError) {
        User user = getValue();
        if (user == null) { onError.accept("does-not-exist"); }
        user.searchUsers(search, searchResponse -> {
            List<SearchItem> users = searchResponse.getUsers();
            for (SearchItem searchItem: users) {
                UserManager.shared().saveUser(searchItem.getUser());
            }
            onSuccess.accept(searchResponse);
        }, onError);
    }
}
