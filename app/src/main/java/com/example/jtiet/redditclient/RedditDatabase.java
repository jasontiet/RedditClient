package com.example.jtiet.redditclient;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by jtiet on 4/19/18.
 */

@Database(entities = {RedditPost.class}, version = 2)
public abstract class RedditDatabase extends RoomDatabase {

    private static RedditDatabase INSTANCE;
    public abstract RedditPostDao redditPostDao();

    public static RedditDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RedditDatabase.class) {
                if (INSTANCE == null) {
                    //Create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RedditDatabase.class, "subreddits_database")
                    .allowMainThreadQueries()
                    .build();
                }
            }
        }

        return INSTANCE;
    }

}
