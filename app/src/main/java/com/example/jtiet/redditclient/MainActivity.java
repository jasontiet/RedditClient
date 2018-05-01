package com.example.jtiet.redditclient;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener {

    private RedditClient client;
    private EditText etSearchSubreddit;
    private List<RedditPost> subreddits;
    private RedditViewModel mRedditViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        subreddits = new ArrayList<RedditPost>();

        etSearchSubreddit = (EditText) findViewById(R.id.etSearchSubreddit);
        Button searchSubreddit = (Button) findViewById(R.id.btnSearchSubreddit);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lvSubreddits);

        final RedditListAdapter adapter = new RedditListAdapter(this, new ArrayList<RedditPost>(subreddits));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRedditViewModel = ViewModelProviders.of(this).get(RedditViewModel.class);
        mRedditViewModel.getAllReddits().observe(this, new Observer<List<RedditPost>>() {
            @Override
            public void onChanged(final List<RedditPost> reddits) {
                //Pass in existing list of subreddits to adapter
                //Update the cached copy of the reddits in the adapter
                subreddits = reddits;
                adapter.setReddits(reddits);
            }
        });

        client = new RedditClient();

        searchSubreddit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fetchPost(etSearchSubreddit.getText().toString());
                mRedditViewModel.getAllReddits().observe(MainActivity.this, new Observer<List<RedditPost>>() {
                    @Override
                    public void onChanged(final List<RedditPost> reddits) {
                        //Update the cached copy of the reddits in the adapter
                        adapter.setReddits(reddits);
                    }
                });
                etSearchSubreddit.setText("");
            }
        });
    }

    @Override
    public void recyclerItemClicked(View view, int position) {

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

                    mRedditViewModel.insert(subredditPost);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Subreddit does not exist.", Toast.LENGTH_LONG).show();
            }
        }, url);
    }

}
