package com.example.jtiet.redditclient;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by jtiet on 4/19/18.
 */

@Database(entities = {RedditPost.class}, version = 2)
public abstract class RedditDatabase extends RoomDatabase {

    private static RedditDatabase INSTANCE;
    public abstract RedditDao redditPostDao();

    public static RedditDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RedditDatabase.class) {
                if (INSTANCE == null) {
                    //Create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RedditDatabase.class, "subreddits_database")
                            .addCallback(redditDatabaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static RedditDatabase.Callback redditDatabaseCallback =
            new RedditDatabase.Callback() {

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
                new PopulateDatabase(INSTANCE).execute();
            }
    };

    private static class PopulateDatabase extends AsyncTask<Void, Void, Void> {

        private final RedditDao dao;

        PopulateDatabase(RedditDatabase db) {
            dao = db.redditPostDao();
        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }
    }

}
