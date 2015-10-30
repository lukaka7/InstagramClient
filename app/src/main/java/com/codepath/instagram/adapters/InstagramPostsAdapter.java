package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by lujiawang on 10/26/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.PostViewHolder> {
    private static final String TAG = "InstagramPostsAdapter";

    List<InstagramPost> posts;
    Context context;
    String mediaId;
    Bitmap bitmap;

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
        final InstagramPost post = posts.get(position);
        this.mediaId = post.mediaId;
        Uri avatarUri = Uri.parse(post.user.profilePictureUrl);
        Uri imageUri = Uri.parse(post.image.imageUrl);

        String createTime = (String) DateUtils.getRelativeTimeSpanString(post.createdTime * 1000);

        // get caption ssb
        if (post.caption != null) {
            SpannableStringBuilder captionSsb = getCaptionSpannableStringBuilder(post.user.userName, post.caption);
            holder.tvCaption.setText(captionSsb, TextView.BufferType.EDITABLE);
        }

        // set comments
        List<InstagramComment> comments = post.comments;
        holder.llComments.removeAllViews();
        if (comments.size() > 0 && comments.size() <= 2) {
            for (InstagramComment comment : comments) {
                Log.i(TAG, "CreatedTime!!!!!!!!!!!!!!!!!! = " + comment.createdTime);
                SpannableStringBuilder commentSsb = getCaptionSpannableStringBuilder(comment.user.userName, comment.text);
                View commentView = LayoutInflater.from(this.context).inflate(R.layout.layout_item_text_coment, holder.llComments, false);
                ((TextView) commentView).setText(commentSsb, TextView.BufferType.EDITABLE);
                holder.llComments.addView(commentView);
            }
        } else if (comments.size() > 2) {
            int cmtCount = post.commentsCount;
            SpannableStringBuilder cmtCountSsb = getCommentCountSpannableStringBuilder(cmtCount);
            View cmtCountView = LayoutInflater.from(this.context).inflate(R.layout.layout_item_text_coment, holder.llComments, false);
            ((TextView) cmtCountView).setText(cmtCountSsb, TextView.BufferType.EDITABLE);
            ((TextView) cmtCountView).setText("View all " + Integer.toString(cmtCount) + " comments");
            ((TextView) cmtCountView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Start Comment Activity
                    Intent intent = new Intent(view.getContext(), CommentsActivity.class);
                    intent.putExtra("mediaId", mediaId);
                    view.getContext().startActivity(intent);
                }
            });
            holder.llComments.addView(cmtCountView);

            int count = 2;
            int idx = comments.size() - 1;
            while (count > 0) {
                InstagramComment comment = comments.get(idx);
                Log.i(TAG, "Comment created time 0 " + comments.get(idx).createdTime);
                SpannableStringBuilder commentSsb = getCaptionSpannableStringBuilder(comments.get(idx).user.userName, comments.get(idx).text);
                View commentView = LayoutInflater.from(this.context).inflate(R.layout.layout_item_text_coment, holder.llComments, false);
                ((TextView) commentView).setText(commentSsb, TextView.BufferType.EDITABLE);
                holder.llComments.addView(commentView);
                idx--;
                count--;
            }
        }

        holder.dvAvatar.setImageURI(avatarUri);
        holder.tvUserName.setText(post.user.userName);

        double aspectRatio = post.image.imageWidth * 1.0 / post.image.imageHeight;
        holder.dvImage.setImageURI(imageUri);
        holder.dvImage.setAspectRatio((float) aspectRatio);
        holder.tvCreatedTime.setText(createTime);
        holder.tvLikes.setText(Utils.formatNumberForDisplay(post.commentsCount) + " likes");

        holder.ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterPopup(view, post);
            }
        });
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
        public TextView tvCaption;
        public LinearLayout llComments;
        public ImageButton ibShare;

        public PostViewHolder(View layoutView) {
            super(layoutView);
            dvAvatar = (SimpleDraweeView) layoutView.findViewById(R.id.dvAvatar);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
            dvImage = (SimpleDraweeView) layoutView.findViewById(R.id.dvImage);
            tvCreatedTime = (TextView) layoutView.findViewById(R.id.tvCreatedTime);
            tvLikes = (TextView) layoutView.findViewById(R.id.tvLikes);
            tvCaption = (TextView) layoutView.findViewById(R.id.tvCaption);
            llComments = (LinearLayout) layoutView.findViewById(R.id.llComments);
            ibShare = (ImageButton) layoutView.findViewById(R.id.ibShare);
        }
    }

    public SpannableStringBuilder getCaptionSpannableStringBuilder(String left, String right) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(left);
        ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(this.context.getResources().getColor(R.color.blue_text));
        ssb.setSpan(blueForegroundColorSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TypefaceSpan sansMediumTypefaceSpan = new TypefaceSpan("sans-serif-medium");
        ssb.setSpan(sansMediumTypefaceSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ssb.append(" ");
        ssb.append(right);
        ForegroundColorSpan grayForegroundColorSpan = new ForegroundColorSpan(this.context.getResources().getColor(R.color.gray_text));
        ssb.setSpan(grayForegroundColorSpan, ssb.length() - right.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TypefaceSpan sansTypefaceSpan = new TypefaceSpan("sans-serif");
        ssb.setSpan(sansTypefaceSpan, ssb.length() - right.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    public SpannableStringBuilder getCommentCountSpannableStringBuilder(int commentCount) {
        String str = "View all " + commentCount + " comments";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);

        ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(this.context.getResources().getColor(R.color.light_gray_text));
        ssb.setSpan(blueForegroundColorSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TypefaceSpan sansMediumTypefaceSpan = new TypefaceSpan("sans-serif");
        ssb.setSpan(sansMediumTypefaceSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    // Display anchored popup menu based on view selected
    private void showFilterPopup(View v, InstagramPost post) {
        final Context context = this.context;
        final InstagramPost p = post;
        PopupMenu popup = new PopupMenu(context, v);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.popup_share, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        getBitmapFromUri(Uri.parse(p.image.imageUrl));
                        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Image Description", null);
                        Uri bmpUri = Uri.parse(path);
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.setType("image/*");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }

    private void setBitmap(Bitmap bm) {
        this.bitmap = bm;
    }

    private void getBitmapFromUri(Uri imageUri) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(imageUri)
                .setRequestPriority(Priority.HIGH)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();

        DataSource<CloseableReference<CloseableImage>> dataSource =
                imagePipeline.fetchDecodedImage(imageRequest, this);

        try {
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                public void onNewResultImpl(@Nullable Bitmap bitmap) {
                    if (bitmap == null) {
                        Log.d("", "Bitmap data source returned success, but bitmap null.");
                        return;
                    }
                    // The bitmap provided to this method is only guaranteed to be around
                    // for the lifespan of this method. The image pipeline frees the
                    // bitmap's memory after this method has completed.
                    //
                    // This is fine when passing the bitmap to a system process as
                    // Android automatically creates a copy.
                    //
                    // If you need to keep the bitmap around, look into using a
                    // BaseDataSubscriber instead of a BaseBitmapDataSubscriber.
                    setBitmap(bitmap);
                }

                @Override
                public void onFailureImpl(DataSource dataSource) {
                    // No cleanup required here
                }
            }, CallerThreadExecutor.getInstance());
        } finally {
            if (dataSource != null) {
                dataSource.close();
            }
        }
    }
}
