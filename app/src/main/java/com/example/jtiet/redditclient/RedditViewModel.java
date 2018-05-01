package com.example.jtiet.redditclient;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by jtiet on 4/24/18.
 */

public class RedditViewModel extends AndroidViewModel {

    RedditRepository mRepository;
    private LiveData<List<RedditPost>> mAllReddits;

    public RedditViewModel(Application application) {
        super(application);
        mRepository = new RedditRepository(application);
        mAllReddits = mRepository.getAllSubreddits();
    }

    LiveData<List<RedditPost>> getAllReddits() {
        return mAllReddits;
    }

    public void insert(RedditPost reddit) {
        mRepository.insert(reddit);
    }

}
