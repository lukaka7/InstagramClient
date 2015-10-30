package com.codepath.instagram.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramComment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by lujiawang on 10/29/15.
 */
public class InstagramCommentsAdapter extends RecyclerView.Adapter<InstagramCommentsAdapter.CommentViewHolder> {
    private static final String TAG = "InstagramCommentsAdapter";

    private List<InstagramComment> comments;
    private Context context;

    public InstagramCommentsAdapter(List<InstagramComment> comments) {
        this.comments = comments;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View postView = inflater.inflate(R.layout.layout_item_comment, parent, false);

        CommentViewHolder cmtViewHolder = new CommentViewHolder(postView);
        return cmtViewHolder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        InstagramComment comment = comments.get(position);
        Uri buddyIconUri = Uri.parse(comment.user.profilePictureUrl);
        String createTime = (String) DateUtils.getRelativeTimeSpanString(comment.createdTime * 1000);

        holder.dvBuddyIcon.setImageURI(buddyIconUri);
        holder.tvCommenterName.setText(comment.user.userName);
        holder.tvComment.setText(comment.text);
        holder.tvCommentTime.setText(createTime);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView dvBuddyIcon;
        public TextView tvCommenterName;
        public TextView tvComment;
        public TextView tvCommentTime;

        public CommentViewHolder(View layoutView) {
            super(layoutView);
            dvBuddyIcon = (SimpleDraweeView) layoutView.findViewById(R.id.dvBuddyIcon);
            tvCommenterName = (TextView) layoutView.findViewById(R.id.tvCommenterName);
            tvComment = (TextView) layoutView.findViewById(R.id.tvComment);
            tvCommentTime = (TextView) layoutView.findViewById(R.id.tvCommentTime);
        }
    }
}
