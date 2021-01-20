package com.gethighlow.highlowandroid.model.Responses;

import com.gethighlow.highlowandroid.model.Resources.SharingPolicy;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SharingPolicyResponse {

    @SerializedName("sharing_policy")
    private SharingPolicy sharingPolicy;

    private String error;

    public SharingPolicyResponse(SharingPolicy sharingPolicy) {

        //Set the items
        this.sharingPolicy = sharingPolicy;

    }

    public SharingPolicy getSharingPolicy(){
        return sharingPolicy;
    }

    public String getError(){
        return error;
    }
}
