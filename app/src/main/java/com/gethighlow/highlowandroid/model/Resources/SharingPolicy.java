package com.gethighlow.highlowandroid.model.Resources;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SharingPolicy {

    @SerializedName("sharing_policy")
    private String sharingPolicy;

    private List<String> uids;

    private String error;

    public SharingPolicy(String sharingPolicy, List<String> uids) {
        this.sharingPolicy = sharingPolicy;
        this.uids = uids;
    }

    public String getSharingPolicy() {
        return sharingPolicy;
    }

    public List<String> getUids() {
        return uids;
    }

    public void setUids(List<String> uids) { this.uids = uids; }

    public void addToUids(String uid) { uids.add(uid); }

    public void removeFromUids(String uid) { while (uids.remove(uid)) {} }

    public String getError() {
        return error;
    }
}
