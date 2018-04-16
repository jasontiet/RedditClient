package com.example.jtiet.redditclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jtiet on 3/23/18.
 */

public class SubredditAdapter extends ArrayAdapter<RedditPost> {

    public SubredditAdapter(Context context, ArrayList<RedditPost> subreddit_list) {
        super(context, 0, subreddit_list);
    }

    //Takes each element of the ArrayList and converts it into a View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get data item for this position
        RedditPost subreddit = getItem(position);

        //Check if existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.subreddit_row, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvSubreddit);
        TextView tvNamePrefix = (TextView) convertView.findViewById(R.id.tvNamePrefix);
        TextView tvSubscribers = (TextView) convertView.findViewById(R.id.tvSubscribers);

        tvTitle.setText(subreddit.getSubreddit());
        tvNamePrefix.setText(subreddit.getSubredditNamePrefixed());
        tvSubscribers.setText(subreddit.getNumSubscribers() + " Subscribers");

        return convertView;
    }
}
