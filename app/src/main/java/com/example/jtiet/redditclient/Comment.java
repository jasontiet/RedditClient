package com.example.jtiet.redditclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jtiet on 4/3/18.
 */

public class Comment {

    private String body;
    private String author;
    private int score;
    private String time;
    private int depth;

    public String getBodyText() { return body; }

    public String getAuthor() { return author; }

    public int getScore()  { return score; }

    public String getTime() { return time; }

    public int getDepth() { return depth; }

    //Populate data and return Comment from given JSON object
    public static Comment commentFromJson(JSONObject jsonObject) {

        Comment comment = new Comment();

        try {
            comment.body = jsonObject.getString("body");
            comment.author = jsonObject.getString("author");
            comment.score = jsonObject.getInt("score");
            comment.depth = jsonObject.getInt("depth");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return comment;
    }

    //Store the comment data to an ArrayList from the given JSONArray
    public static ArrayList<Comment> addComments(ArrayList<Comment> comments, JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject commentJson = null;

            try {
                commentJson = jsonArray.getJSONObject(i).getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            //Populate comment with JSON data
            Comment comment = Comment.commentFromJson(commentJson);
            if (comment != null) {
                comments.add(comment);
                addReplies(comments, commentJson, comment.depth+1);
            }
        }

        return comments;
    }

    public static void addReplies(ArrayList<Comment> comments, JSONObject jsonObject, int level) {
        try {
            if (jsonObject.getString("replies").equals("")) {
                return;
            }

            JSONArray replies = jsonObject.getJSONObject("replies").getJSONObject("data").getJSONArray("children");
            addComments(comments, replies);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
