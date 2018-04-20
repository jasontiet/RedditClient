package com.example.jtiet.redditclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RedditClient client;
    private SubredditAdapter subredditAdapter;
    private EditText etSearchSubreddit;
    private List<RedditPost> subredditList;
    private ArrayList<String> subredditNames;
    private RedditDatabase subredditDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView subreddits = (ListView) findViewById(R.id.lvSubreddits);
        etSearchSubreddit = (EditText) findViewById(R.id.etSearchSubreddit);
        Button searchSubreddit = (Button) findViewById(R.id.btnSearchSubreddit);

        subredditDatabase = RedditDatabase.getDatabase(getApplicationContext());

        subredditList = new ArrayList<RedditPost>();
        subredditNames = new ArrayList<String>();

        subredditList = subredditDatabase.redditPostDao().getSubreddits();

        for (int i = 0; i < subredditList.size(); i++) {
            subredditNames.add(subredditList.get(i).getSubreddit());
        }

        subredditAdapter = new SubredditAdapter(this, new ArrayList<RedditPost>(subredditList));

        subreddits.setAdapter(subredditAdapter);

        client = new RedditClient();

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
                    subredditDatabase.redditPostDao().insert(subredditPost);

                    if (!subredditNames.contains(subredditPost.getSubreddit())) {
                        subredditNames.add(subredditPost.getSubreddit().toString());
                    }

                    subredditAdapter.add(subredditPost);
                    subredditAdapter.notifyDataSetChanged();
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
