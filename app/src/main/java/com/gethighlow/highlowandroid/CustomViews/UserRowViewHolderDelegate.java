package com.gethighlow.highlowandroid.CustomViews;

public interface UserRowViewHolderDelegate {
    void unFriend(int position);
    default void addFriend(int position) {
    };
}
