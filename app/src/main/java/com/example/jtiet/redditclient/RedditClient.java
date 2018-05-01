package com.example.jtiet.redditclient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class RedditClient {

    private final String API_BASE_URL = "http://www.reddit.com";
    private AsyncHttpClient client;

    public RedditClient() {
        this.client = new AsyncHttpClient();
    }

    //Retrieves the JSON data from the requested subreddit
    //If an empty string is given, we will get data from the front page
    private String getJsonFromSubreddit(String subreddit) {
        String url = API_BASE_URL;

        if (!subreddit.equals("")) {
            url += "/r/" + subreddit;
        }
        return url + ".json";
    }

    //Retrieve JSON data from the requested URL
    private String getJsonFromUrl(String url) {
        if (url.equals("")) {
            return null;
        }
        return url + ".json";
    }

    //Retrieve posts from the given subreddit
    public void getPosts(JsonHttpResponseHandler handler, String subreddit) {
        String url = getJsonFromSubreddit(subreddit);
        client.get(url, handler);
    }

    //Retrieve JSON data from the given url
    public void getPostData(JsonHttpResponseHandler handler, String permalink) {
        String jsonUrl = API_BASE_URL + getJsonFromUrl(permalink);
        client.get(jsonUrl, handler);
    }
}
