package com.example.jtiet.redditclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jtiet on 1/28/18.
 */

public class RedditPostAdapter extends ArrayAdapter<RedditPost> {

    public RedditPostAdapter(Context context, ArrayList<RedditPost> posts) {
        super(context, 0, posts);
    }

    public RedditPostAdapter(Context context, List<RedditPost> posts) {
        super(context, 0, posts);
    }

    //Takes each element of the ArrayList and converts it into a View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get the data item for this position
        RedditPost post = getItem(position);

        //Check if existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.reddit_post, parent, false);
        }

        //Lookup views within the subreddit_post
        TextView tvUpvotes = (TextView) convertView.findViewById(R.id.tvUpvotes);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvDomain = (TextView) convertView.findViewById(R.id.tvDomain);
        TextView tvComments = (TextView) convertView.findViewById(R.id.tvComments);
        TextView tvSubreddit = (TextView) convertView.findViewById(R.id.tvSubreddit);
        ImageView ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);

        //Populate the data
        tvUpvotes.setText("" + post.getUpvotes());
        tvTitle.setText(post.getTitle());
        tvDomain.setText("(" + post.getDomain() + ")");
        tvComments.setText(post.getNumComments() + " comments");
        tvSubreddit.setText(post.getSubreddit());

        //Display the thumbnail if there is one
        String thumbnailUrl = post.getThumbnail();
        if (thumbnailUrl.length() < 1 || thumbnailUrl.equals("default") || thumbnailUrl.equals("self")) {
            ivThumbnail.setImageResource(R.mipmap.reddit_logo);
        } else {
            Picasso.with(getContext()).load(post.getThumbnail()).into(ivThumbnail);
        }

        return convertView;
    }


}
