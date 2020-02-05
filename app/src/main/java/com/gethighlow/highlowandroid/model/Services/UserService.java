package com.gethighlow.highlowandroid.model.Services;

import android.graphics.drawable.Drawable;

import com.gethighlow.highlowandroid.model.Responses.FeedResponse;
import com.gethighlow.highlowandroid.model.Responses.FriendSuggestionsResponse;
import com.gethighlow.highlowandroid.model.Responses.FriendsResponse;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.InterestResponse;
import com.gethighlow.highlowandroid.model.Responses.PendingFriendshipsResponse;
import com.gethighlow.highlowandroid.model.Responses.SearchResponse;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UserService {

    private static final UserService ourInstance = new UserService();
    public static UserService shared() { return ourInstance; }

    private Gson gson = new Gson();

    public UserService() {}

    public void getUser(String uid, Consumer<User> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        if (uid != null) {
            params.put("uid", uid);
        }

        APIService.shared().authenticatedRequest("/user/get", 0, params, (response) -> {

            User user = gson.fromJson(response, User.class);

            String error = user.error();

            if (error != null) {
                onError.accept(error);
            } else {

                onSuccess.accept(user);

            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void getCurrentUser(Consumer<User> onSuccess, Consumer<String> onError) {
        getUser(null, onSuccess, onError);
    }

    public void setProfile(String firstname, String lastname, String email, String bio, Drawable profileImage, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("firstname", firstname);
            put("lastname", lastname);
            put("email", email);
            put("bio", bio);
        }};

        APIService.shared().makeMultipartRequest("/user/set_profile", 1, params, profileImage, (response) -> {

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

    public void getFeed(int page, Consumer<FeedResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/feed/page/" + page, 0, null, (response) -> {

            FeedResponse feedResponse = gson.fromJson(response, FeedResponse.class);

            String error = feedResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(feedResponse);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void getFriendsForUser(String uid, Consumer<FriendsResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        if (uid != null) {
            params.put("uid", uid);
        }

        APIService.shared().authenticatedRequest("/user/friends", 0, params, (response) -> {

            FriendsResponse friendsResponse = gson.fromJson(response, FriendsResponse.class);

            String error = friendsResponse.getError();
            if (error != null) {
                onError.accept(error);
            }
            else {
                onSuccess.accept(friendsResponse);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void getFriends(Consumer<FriendsResponse> onSuccess, Consumer<String> onError) {
        getFriendsForUser(null, onSuccess, onError);
    }

    public void unFriend(String uid, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/" + uid + "/unfriend", 1, null, (response) -> {
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

    public void requestFriend(String uid, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/" + uid + "/request_friend", 1, null, (response) -> {
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

    public void acceptFriend(String uid, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/accept_friend/" + uid, 1, null, (response) -> {
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

    public void searchUsers(String search, Consumer<SearchResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("search", search);

        APIService.shared().authenticatedRequest("/user/search", 1, params, (response) -> {
            SearchResponse searchResponse = gson.fromJson(response, SearchResponse.class);
            String error = searchResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(searchResponse);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void getPendingFriendships(Consumer<PendingFriendshipsResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/get_pending_friendships", 0, null, (response) -> {
            PendingFriendshipsResponse pendingFriendshipsResponse = gson.fromJson(response, PendingFriendshipsResponse.class);

            String error = pendingFriendshipsResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(pendingFriendshipsResponse);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void getInterests(Consumer<InterestResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/interests", 0, null, (response) -> {
            InterestResponse interestResponse = gson.fromJson(response, InterestResponse.class);

            String error = interestResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(interestResponse);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void createInterest(String name, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        APIService.shared().authenticatedRequest("/user/interests/create", 1, params, (response) -> {
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

    public void addInterest(String interestId, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("interests", "[" + interestId + "]");

        APIService.shared().authenticatedRequest("/user/interests/add", 1, params, (response) -> {
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

    public void removeInterest(String interestId, Consumer<GenericResponse> onSuccess, Consumer<String> onError) {
        Map<String, String> params = new HashMap<>();
        params.put("interests", "[" + interestId + "]");

        APIService.shared().authenticatedRequest("/user/interests/remove", 1, params, (response) -> {
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

    public void getAllInterests(Consumer<InterestResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/interests/all", 0, null, (response) -> {
            InterestResponse interestResponse = gson.fromJson(response, InterestResponse.class);

            String error = interestResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(interestResponse);
            }

        }, (error) -> {
            onError.accept("network-error");
        });
    }

    public void getFriendSuggestions(Consumer<FriendSuggestionsResponse> onSuccess, Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/friends/suggestions", 0, null, (response) -> {
            FriendSuggestionsResponse friendSuggestionsResponse = gson.fromJson(response, FriendSuggestionsResponse.class);

            String error = friendSuggestionsResponse.getError();
            if (error != null) {
                onError.accept(error);
            } else {
                onSuccess.accept(friendSuggestionsResponse);
            }
        }, (error) -> {
            onError.accept("network-error");
        });
    }

}
