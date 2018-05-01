package com.example.jtiet.redditclient;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by jtiet on 4/22/18.
 */

public class RedditRepository {

    private RedditDao mRedditsDao;
    private LiveData<List<RedditPost>> mAllRedditPosts;

    public RedditRepository(Application application) {
        RedditDatabase database = RedditDatabase.getDatabase(application.getApplicationContext());
        mRedditsDao = database.redditPostDao();
        mAllRedditPosts = mRedditsDao.getSubreddits();
    }

    public void insert(RedditPost reddit) {
        new InsertAsyncTask(mRedditsDao).execute(reddit);
    }

    public LiveData<List<RedditPost>> getAllSubreddits() {
        return mAllRedditPosts;
    }

    private class InsertAsyncTask extends AsyncTask<RedditPost, Void, Void> {

        private RedditDao mAsyncTaskRedditDao;

        InsertAsyncTask(RedditDao dao) {
            mAsyncTaskRedditDao = dao;
        }

        @Override
        protected Void doInBackground(RedditPost... redditPosts) {
            mAsyncTaskRedditDao.insert(redditPosts[0]);
            return null;
        }
    }

}
