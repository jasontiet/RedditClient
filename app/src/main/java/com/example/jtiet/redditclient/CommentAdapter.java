package com.example.jtiet.redditclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jtiet on 4/1/18.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.comment_row, parent, false);
        }

        TextView commentAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
        TextView commentScore = (TextView) convertView.findViewById(R.id.tvPoints);
        TextView commentContent = (TextView) convertView.findViewById(R.id.tvComment);

        if (commentContent.equals("")) {
            commentContent.setVisibility(View.GONE);
        } else {
            commentContent.setVisibility(View.VISIBLE);
        }

        convertView.setPadding(comment.getDepth() * 75, 0, 0, 0);

        commentAuthor.setText(comment.getAuthor());
        commentContent.setText(comment.getBodyText());

        if (comment.getScore() == 1) {
            commentScore.setText(comment.getScore() + " point");
        } else {
            commentScore.setText(comment.getScore() + " points");
        }

        return convertView;
    }
}
