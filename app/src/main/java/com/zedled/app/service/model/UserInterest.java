package com.zedled.app.service.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserInterest {

    @PrimaryKey
    @NonNull
    private String userInterest;

    @NonNull
    private String interestTags;

    public UserInterest(@NonNull String userInterest, @NonNull String interestTags) {
        this.userInterest = userInterest;
        this.interestTags = interestTags;
    }

    @NonNull
    public String getUserInterest() {
        return userInterest;
    }

    @NonNull
    public String getInterestTags() {
        return interestTags;
    }
}
