package com.gethighlow.highlowandroid.model.Services;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Resources.User;
import com.gethighlow.highlowandroid.model.Responses.FeedResponse;
import com.gethighlow.highlowandroid.model.Responses.FriendSuggestionsResponse;
import com.gethighlow.highlowandroid.model.Responses.FriendsResponse;
import com.gethighlow.highlowandroid.model.Responses.GenericResponse;
import com.gethighlow.highlowandroid.model.Responses.InterestResponse;
import com.gethighlow.highlowandroid.model.Responses.PendingFriendshipsResponse;
import com.gethighlow.highlowandroid.model.Responses.SearchResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final UserService ourInstance = new UserService();
    public static UserService shared() { return ourInstance; }

    private Gson gson = new Gson();

    public UserService() {}

    public void getUser(String uid, final Consumer<User> onSuccess, final Consumer<String> onError) {
        String url = "/user/get";
        if (uid != null) {
            url += "?uid=" + uid;
        }

        APIService.shared().authenticatedRequest(url, Request.Method.POST, null, new Consumer<String>() {
            @Override
            public void accept(String response) {

                User user = gson.fromJson(response, User.class);

                String error = user.error();

                if (error != null) {
                    onError.accept(error);
                } else {

                    onSuccess.accept(user);

                }

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void getCurrentUser(Consumer<User> onSuccess, Consumer<String> onError) {
        getUser(null, onSuccess, onError);
    }

    public void setProfile(final String firstname, final String lastname, final String email, final String bio, Bitmap profileImage, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("firstname", firstname);
            put("lastname", lastname);
            put("email", email);
            put("bio", bio);
        }};

        APIService.shared().makeMultipartRequest("/user/set_profile", 1, params, profileImage, new Consumer<String>() {
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

    public void getFeed(int page, final Consumer<FeedResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/feed/page/" + page, 0, null, new Consumer<String>() {
            @Override
            public void accept(String response) {

                FeedResponse feedResponse = gson.fromJson(response, FeedResponse.class);

                String error = feedResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(feedResponse);
                }

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void getFriendsForUser(String uid, final Consumer<FriendsResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();
        if (uid != null) {
            params.put("uid", uid);
        }

        APIService.shared().authenticatedRequest("/user/friends", 0, params, new Consumer<String>() {
            @Override
            public void accept(String response) {

                FriendsResponse friendsResponse = gson.fromJson(response, FriendsResponse.class);

                String error = friendsResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(friendsResponse);
                }

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void getFriends(Consumer<FriendsResponse> onSuccess, Consumer<String> onError) {
        getFriendsForUser(null, onSuccess, onError);
    }

    public void unFriend(String uid, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/" + uid + "/unfriend", 1, null, new Consumer<String>() {
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

    public void requestFriend(String uid, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/" + uid + "/request_friend", 1, null, new Consumer<String>() {
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

    public void acceptFriend(String uid, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/accept_friend/" + uid, 1, null, new Consumer<String>() {
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

    public void searchUsers(String search, final Consumer<SearchResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("search", search);

        APIService.shared().authenticatedRequest("/user/search", 1, params, new Consumer<String>() {
            @Override
            public void accept(String response) {
                SearchResponse searchResponse = gson.fromJson(response, SearchResponse.class);
                String error = searchResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(searchResponse);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void getPendingFriendships(final Consumer<PendingFriendshipsResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/get_pending_friendships", 0, null, new Consumer<String>() {
            @Override
            public void accept(String response) {
                PendingFriendshipsResponse pendingFriendshipsResponse = gson.fromJson(response, PendingFriendshipsResponse.class);

                String error = pendingFriendshipsResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(pendingFriendshipsResponse);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void getInterests(final Consumer<InterestResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/interests", 0, null, new Consumer<String>() {
            @Override
            public void accept(String response) {
                InterestResponse interestResponse = gson.fromJson(response, InterestResponse.class);

                String error = interestResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(interestResponse);
                }
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void createInterest(String name, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        APIService.shared().authenticatedRequest("/user/interests/create", 1, params, new Consumer<String>() {
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

    public void addInterest(String interestId, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("interest", interestId);

        APIService.shared().authenticatedRequest("/user/interest/add", 1, params, new Consumer<String>() {
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

    public void removeInterest(String interestId, final Consumer<GenericResponse> onSuccess, final Consumer<String> onError) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("interest", interestId);

        APIService.shared().authenticatedRequest("/user/interest/remove", 1, params, new Consumer<String>() {
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

    public void getAllInterests(final Consumer<InterestResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/interests/all", 0, null, new Consumer<String>() {
            @Override
            public void accept(String response) {
                InterestResponse interestResponse = gson.fromJson(response, InterestResponse.class);

                String error = interestResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(interestResponse);
                }

            }
        }, new Consumer<String>() {
            @Override
            public void accept(String error) {
                onError.accept("network-error");
            }
        });
    }

    public void getFriendSuggestions(final Consumer<FriendSuggestionsResponse> onSuccess, final Consumer<String> onError) {
        APIService.shared().authenticatedRequest("/user/friends/suggestions", 0, null, new Consumer<String>() {
            @Override
            public void accept(String response) {
                FriendSuggestionsResponse friendSuggestionsResponse = gson.fromJson(response, FriendSuggestionsResponse.class);

                String error = friendSuggestionsResponse.getError();
                if (error != null) {
                    onError.accept(error);
                } else {
                    onSuccess.accept(friendSuggestionsResponse);
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
