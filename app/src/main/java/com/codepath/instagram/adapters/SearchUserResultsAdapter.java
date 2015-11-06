package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.PhotoGridActivity;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by lujiawang on 10/31/15.
 */
public class SearchUserResultsAdapter extends RecyclerView.Adapter<SearchUserResultsAdapter.SearchUserViewHolder> {
    private static final String TAG = "SearchUserResultsAdapter";

    private List<InstagramUser> users;
    private Context context;

    public SearchUserResultsAdapter(List<InstagramUser> users) {
        this.users = users;
    }

    @Override
    public void onBindViewHolder(SearchUserViewHolder holder, final int position) {
        InstagramUser user = users.get(position);

        Uri avatarUri = Uri.parse(user.profilePictureUrl);
        holder.dvUserSearchAvatar.setImageURI(avatarUri);
        holder.tvUserSearchName.setText(user.userName);
        holder.tvUserSearchIntro.setText(user.fullName);

        holder.dvUserSearchAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InstagramUser user = users.get(position);
                Intent intent = new Intent(context, PhotoGridActivity.class);
                intent.putExtra(PhotoGridActivity.EXTRA_USER_ID, user.userId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public SearchUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View searchUserView = inflater.inflate(R.layout.layout_item_user_search, parent, false);

        SearchUserViewHolder searchUserViewHolder = new SearchUserViewHolder(searchUserView);
        return searchUserViewHolder;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class SearchUserViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView dvUserSearchAvatar;
        public TextView tvUserSearchName;
        public TextView tvUserSearchIntro;

        public SearchUserViewHolder(View layoutView) {
            super(layoutView);
            dvUserSearchAvatar = (SimpleDraweeView) layoutView.findViewById(R.id.dvUserSearchAvatar);
            tvUserSearchName = (TextView) layoutView.findViewById(R.id.tvUserSearchName);
            tvUserSearchIntro = (TextView) layoutView.findViewById(R.id.tvUserSearchIntro);
        }
    }
}
