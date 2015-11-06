package com.codepath.instagram.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by lujiawang on 11/4/15.
 */
public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.PhotoGridViewHolder> {
    private static final String TAG = "PhotoGridAdapter";

    private List<InstagramPost> posts;
    private Context context;

    public PhotoGridAdapter(List<InstagramPost> posts) {
        this.posts = posts;
    }


    @Override
    public PhotoGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.layout_grid_photo, parent, false);

        PhotoGridViewHolder viewHolder = new PhotoGridViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoGridViewHolder holder, int position) {
        InstagramPost post = posts.get(position);
        Uri imageUri = Uri.parse(post.image.imageUrl);
        holder.dvGridPhoto.setImageURI(imageUri);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PhotoGridViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView dvGridPhoto;

        public PhotoGridViewHolder(View layoutView) {
            super(layoutView);
            dvGridPhoto = (SimpleDraweeView) layoutView.findViewById(R.id.dvGridPhoto);
        }
    }
}
