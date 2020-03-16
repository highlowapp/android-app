package com.gethighlow.highlowandroid.model.Resources;

import com.google.gson.annotations.SerializedName;

public class Interest {
    @SerializedName("interest_id")
    private String interestid;
    private String name;

    public String getInterestid() {
        return interestid;
    }

    public String getName() {
        return name;
    }

    public Interest(String name) {
        this.name = name;
    }
}
