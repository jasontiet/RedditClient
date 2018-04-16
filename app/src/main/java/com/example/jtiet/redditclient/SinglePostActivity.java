package com.example.jtiet.redditclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jtiet on 4/2/18.
 */

public class SinglePostActivity extends AppCompatActivity {

    private RedditClient client;
    private CommentAdapter commentAdapter;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subreddit_post);
        ArrayList<Comment> comments = new ArrayList<Comment>();

        ListView lvComments = (ListView) findViewById(R.id.lvComments);
        TextView postTitle = (TextView) findViewById(R.id.tvTitle);
        TextView postAuthor = (TextView) findViewById(R.id.tvAuthor);
        TextView postDescription = (TextView) findViewById(R.id.tvDescription);
        ImageView postImage = (ImageView) findViewById(R.id.ivUrlImage);

        Intent intent = getIntent();

        url = intent.getStringExtra("permalink");
        String subreddit = intent.getStringExtra("subreddit");
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String description = intent.getStringExtra("description");
        String urlImage = intent.getStringExtra("url");

        Picasso.with(this.getApplicationContext()).load(urlImage).into(postImage);

        postTitle.setText(title);
        postAuthor.setText(author);
        postDescription.setText(description);

        commentAdapter = new CommentAdapter(this, comments);
        lvComments.setAdapter(commentAdapter);

        client = new RedditClient();
        fetchComments();
    }

    public void fetchComments() {
        client.getPostData(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray responseBody) {
                ArrayList<Comment> commentList = new ArrayList<Comment>();
                JSONArray items = null;
                try {
                    //Get the comments json array
                    JSONObject data = responseBody.getJSONObject(1).getJSONObject("data");
                    items = data.getJSONArray("children");

                    //Parse the json array into array of model objects
                    commentList = Comment.addComments(commentList, items);

                    //Load the model objects into the adapter
                    for (Comment comment: commentList) {
                        commentAdapter.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, url);
    }
}
