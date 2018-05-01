package com.example.jtiet.redditclient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jtiet on 4/24/18.
 */

public class RedditListAdapter extends RecyclerView.Adapter<RedditListAdapter.RedditViewHolder> {

    private LayoutInflater mInflater;
    private List<RedditPost> mReddits;

    class RedditViewHolder extends RecyclerView.ViewHolder {
        private final TextView subredditView;
        private final TextView namePrefixView;
        private final TextView subscribersView;
        private RelativeLayout parentLayout;

        private RedditViewHolder(View itemView) {
            super(itemView);

            subredditView = itemView.findViewById(R.id.tvSubreddit);
            namePrefixView = itemView.findViewById(R.id.tvNamePrefix);
            subscribersView = itemView.findViewById(R.id.tvSubscribers);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    RedditListAdapter(Context context, ArrayList<RedditPost> reddits) {
        mReddits = reddits;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public RedditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.subreddit_row, parent, false);
        return new RedditViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RedditViewHolder holder, final int position) {
        if (mReddits != null) {
            RedditPost current = mReddits.get(position);
            holder.subredditView.setText(current.getSubreddit());
            holder.namePrefixView.setText(current.getSubredditNamePrefixed());
            holder.subscribersView.setText(String.valueOf(current.getNumSubscribers()));

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PostsActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, mReddits.get(position).getSubreddit());
                    v.getContext().startActivity(intent);
                }
            });
        } else {
            //Covers the case of data not being ready yet
            holder.subredditView.setText("No reddit available");
            holder.namePrefixView.setText("No reddit available");
            holder.subscribersView.setText("No reddit available");
        }
    }

    void setReddits(List<RedditPost> reddits) {
        mReddits = reddits;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mReddits != null) {
            return mReddits.size();
        } else {
            return 0;
        }
    }


}
