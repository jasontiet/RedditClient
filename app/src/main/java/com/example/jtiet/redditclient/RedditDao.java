package com.example.jtiet.redditclient;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jtiet on 4/19/18.
 */

@Dao
public interface RedditDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RedditPost redditPost);

    @Query("SELECT * from subreddits_table ORDER BY subreddit ASC")
    LiveData<List<RedditPost>> getSubreddits();

}
