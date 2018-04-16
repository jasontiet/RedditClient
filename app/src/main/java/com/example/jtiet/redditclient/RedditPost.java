package com.example.jtiet.redditclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

//This class defines the data model for reddit posts given the JSON data
//This class allows you to serialize JSON data
public class RedditPost {

    private String title;
    private String domain;
    private int numComments;
    private String subreddit;
    private String thumbnail;
    private String permaLink;
    private int upvotes;
    private int numSubscribers;
    private String subredditNamePrefixed;
    private String description;
    private String author;
    private String url;

    public String getTitle() {
        return title;
    }

    public String getDomain() {
        return domain;
    }

    public int getNumComments() {
        return numComments;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getPermaLink() {
        return permaLink;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getNumSubscribers() { return numSubscribers; }

    public String getSubredditNamePrefixed() { return subredditNamePrefixed; }

    public String getSelfText() { return description; }

    public String getAuthor() { return author; }

    public String getUrl() { return url; }

    //Returns a reddit post from the given JSON data
    //Serialize the JSON response
    public static RedditPost fromJson(JSONObject jsonObject) {
        RedditPost p = new RedditPost();

        try {
            p.title = jsonObject.getString("title");
            p.domain = jsonObject.getString("domain");
            p.numComments = jsonObject.getInt("num_comments");
            p.subreddit = jsonObject.getString("subreddit");
            p.permaLink = jsonObject.getString("permalink");
            p.upvotes = jsonObject.getInt("score");
            p.numSubscribers = jsonObject.getInt("subreddit_subscribers");
            p.subredditNamePrefixed = jsonObject.getString("subreddit_name_prefixed");
            p.description = jsonObject.getString("selftext");
            p.author = jsonObject.getString("author");
            p.url = jsonObject.getString("url");

            //Attempt to retrieve a thumbnail. Not all posts have a thumbnail
            try {
                p.thumbnail = jsonObject.getString("thumbnail");
            } catch (JSONException e) {
                //There is no thumbnail
                p.thumbnail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return p;
    }

    //Store the data from given JSONArray into an ArrayList of RedditPosts
    public static ArrayList<RedditPost> fromJson(JSONArray jsonArray) {
        ArrayList<RedditPost> posts = new ArrayList<RedditPost>(jsonArray.length());

        //Convert each element in the json array to a json object, then to a RedditPost
        //Deserialize the resulting JSON data
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject postJson = null;

            try {
                postJson = jsonArray.getJSONObject(i).getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            RedditPost post = RedditPost.fromJson(postJson);
            if (post != null) {
                posts.add(post);
            }
        }

        return posts;
    }
}
