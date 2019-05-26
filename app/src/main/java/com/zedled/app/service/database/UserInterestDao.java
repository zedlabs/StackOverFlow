package com.zedled.app.service.database;

import com.zedled.app.service.model.UserInterest;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface UserInterestDao {

    @Query("SELECT * FROM UserInterest")
    LiveData<List<UserInterest>> getUserInterest();

    @Insert
    void setUserInterests(List<UserInterest> interests);

    @Query("SELECT interestTags FROM UserInterest WHERE userInterest = :userInterest")
    LiveData<List<String>> getInterestTag(String userInterest);

    @Query("UPDATE UserInterest SET interestTags = :tags WHERE userInterest = :userInterest")
    void setInterestTags(String userInterest, List<String> tags);

}
