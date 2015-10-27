package com.codepath.instagram.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by lujiawang on 10/26/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.PostViewHolder> {
    private static final String TAG = "InstagramPostsAdapter";

    List<InstagramPost> posts;
    Context context;

    public InstagramPostsAdapter(List<InstagramPost> posts) {
        this.posts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View postView = inflater.inflate(R.layout.layout_item_post, parent, false);

        PostViewHolder postViewHolder = new PostViewHolder(postView);
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        InstagramPost post = posts.get(position);

        Uri avatarUri = Uri.parse(post.user.profilePictureUrl);
        Uri imageUri = Uri.parse(post.image.imageUrl);

        String createTime = (String) DateUtils.getRelativeTimeSpanString(post.createdTime * 1000);
        List<InstagramComment> comments = post.comments;
        if (comments.size() < 1) {
            holder.tvComment.setText("");
        } else {
            String commenterName = comments.get(0).user.userName;
            SpannableStringBuilder ssb = new SpannableStringBuilder(commenterName);
            ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(this.context.getResources().getColor(R.color.blue_text));
            ssb.setSpan(blueForegroundColorSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            TypefaceSpan sansMediumTypefaceSpan = new TypefaceSpan("sans-serif-medium");
            ssb.setSpan(sansMediumTypefaceSpan, 0, commenterName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            ssb.append(" ");
            String comment = comments.get(0).text;
            ssb.append(comment);
            ForegroundColorSpan grayForegroundColorSpan = new ForegroundColorSpan(this.context.getResources().getColor(R.color.gray_text));
            ssb.setSpan(grayForegroundColorSpan, ssb.length() - comment.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            TypefaceSpan sansTypefaceSpan = new TypefaceSpan("sans-serif");
            ssb.setSpan(sansTypefaceSpan, ssb.length() - comment.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.tvComment.setText(ssb, TextView.BufferType.EDITABLE);
        }

        holder.dvAvatar.setImageURI(avatarUri);
        holder.tvUserName.setText(post.user.userName);

        double aspectRatio = post.image.imageWidth * 1.0 / post.image.imageHeight;
        holder.dvImage.setImageURI(imageUri);
        holder.dvImage.setAspectRatio((float) aspectRatio);
        holder.tvCreatedTime.setText(createTime);
        holder.tvLikes.setText(Utils.formatNumberForDisplay(post.commentsCount) + " likes");
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView dvAvatar;
        public TextView tvUserName;
        public TextView tvCreatedTime;
        public SimpleDraweeView dvImage;
        public TextView tvLikes;
        public TextView tvComment;

        public PostViewHolder(View layoutView) {
            super(layoutView);
            dvAvatar = (SimpleDraweeView) layoutView.findViewById(R.id.dvAvatar);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
            dvImage = (SimpleDraweeView) layoutView.findViewById(R.id.dvImage);
            tvCreatedTime = (TextView) layoutView.findViewById(R.id.tvCreatedTime);
            tvLikes = (TextView) layoutView.findViewById(R.id.tvLikes);
            tvComment = (TextView) layoutView.findViewById(R.id.tvComment);
        }
    }
}
