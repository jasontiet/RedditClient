package com.example.jtiet.redditclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {

    private ListView lvPosts;
    private RedditPostAdapter postsAdapter;
    private RedditClient client;
    private String subreddit;
    private ArrayList<RedditPost> redditPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        lvPosts = (ListView) findViewById(R.id.lvPosts);

        //Construct the data source
        ArrayList<RedditPost> subredditPosts = new ArrayList<RedditPost>();
        redditPosts = new ArrayList<RedditPost>();

        Intent intent = getIntent();
        subreddit = intent.getStringExtra(Intent.EXTRA_TEXT);

        //Create the adapter to convert the array to views
        postsAdapter = new RedditPostAdapter(this, subredditPosts);

        //Attach the adapter to a ListView
        lvPosts.setAdapter(postsAdapter);

        client = new RedditClient();
        fetchPosts();

        lvPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PostsActivity.this, SinglePostActivity.class);
                intent.putExtra("permalink", redditPosts.get(position).getPermaLink());
                intent.putExtra("subreddit", subreddit);
                intent.putExtra("title", redditPosts.get(position).getTitle());
                intent.putExtra("author", redditPosts.get(position).getAuthor());
                intent.putExtra("description", redditPosts.get(position).getDescription());
                intent.putExtra("url", redditPosts.get(position).getUrl());
                startActivity(intent);

            }
        });
    }

    public void fetchPosts() {
        client.getPosts(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject responseBody) {
                JSONArray items = null;
                try {
                    //Get the posts json array
                    JSONObject data = responseBody.getJSONObject("data");
                    items = data.getJSONArray("children");
                    //Parse the json array into array of model objects
                    ArrayList<RedditPost> posts = RedditPost.fromJson(items);
                    //Load the model objects into the adapter
                    for (RedditPost post: posts) {
                        redditPosts.add(post);
                        postsAdapter.add(post);
                    }
                    postsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, subreddit);
    }
}