package com.example.jtiet.redditclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RedditClient client;
    SubredditAdapter subredditAdapter;
    EditText etSearchSubreddit;
    ArrayList<RedditPost> subredditList;
    ArrayList<String> subredditNames;
    SharedPreferences sharedPreferences;

    final String SUBREDDIT_LIST = "subreddit_list";
    final String SHARED_PREFERENCES = "shared_preferences";

    boolean isBlocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView subreddits = (ListView) findViewById(R.id.lvSubreddits);
        etSearchSubreddit = (EditText) findViewById(R.id.etSearchSubreddit);
        Button searchSubreddit = (Button) findViewById(R.id.btnSearchSubreddit);

        subredditList = new ArrayList<RedditPost>();
        subredditNames = new ArrayList<String>();

        subredditAdapter = new SubredditAdapter(this, subredditList);

        subreddits.setAdapter(subredditAdapter);

        client = new RedditClient();

        retrieveSavedData();

        searchSubreddit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fetchPost(etSearchSubreddit.getText().toString());
                etSearchSubreddit.setText("");
            }
        });

        subreddits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, PostsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, subredditNames.get(position));
                startActivity(intent);
            }
        });
    }

    public void fetchPost(String url) {
        client.getPosts(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject responseBody) {
                try {
                    //Get the posts json array
                    JSONObject data = responseBody.getJSONObject("data");
                    JSONObject post = data.getJSONArray("children").getJSONObject(0);
                    JSONObject redditPost = post.getJSONObject("data");
                    //Parse the json array into array of model objects
                    RedditPost subredditPost = RedditPost.fromJson(redditPost);

                    isBlocked = false;


                    if (!subredditNames.contains(subredditPost.getSubreddit())) {
                        subredditNames.add(subredditPost.getSubreddit().toString());
                        saveListData();
                    }

                    subredditAdapter.add(subredditPost);
                    subredditAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, url);
    }

    public void saveListData() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(subredditNames);
        editor.putString(SUBREDDIT_LIST, json);
        editor.apply();
    }

    public void retrieveSavedData() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SUBREDDIT_LIST, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        subredditNames = gson.fromJson(json, type);

        if (subredditNames == null) {
            subredditNames = new ArrayList<String>();
        } else {
            for (int i = 0; i < subredditNames.size(); i++) {
                if (isBlocked == false) {
                    isBlocked = true;
                    fetchPost(subredditNames.get(i));

                while (isBlocked == true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                }

            }
        }
    }

}
