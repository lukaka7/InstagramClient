package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.HomeActivity;
import com.codepath.instagram.adapters.PhotoGridAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lujiawang on 11/4/15.
 */
public class PhotoGridFragment extends Fragment {
    private ArrayList<InstagramPost> posts;
    private PhotoGridAdapter adapter;
    private String userId;
    private String tagName;

    public PhotoGridFragment() {
    }

    public static PhotoGridFragment newInstance(String userId, String tagName) {
        PhotoGridFragment photoGridFragment = new PhotoGridFragment();
        Bundle args = new Bundle();
        args.putString("UserId", userId);
        args.putString("TagName", tagName);
        photoGridFragment.setArguments(args);
        return photoGridFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posts = new ArrayList<InstagramPost>();
        adapter = new PhotoGridAdapter(posts);

        userId = getArguments().getString("UserId");
        tagName = getArguments().getString("TagName");

        InstagramClient client = HomeActivity.getRestClient();
        if (userId != null) {
            // Fetch user posts
            client.getUserPosts(userId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    posts.clear();
                    posts.addAll(Utils.decodePostsFromJsonResponse(response));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        } else if (tagName != null) {
            // Fetch tag posts
            client.getTagPosts(tagName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    posts.clear();
                    posts.addAll(Utils.decodePostsFromJsonResponse(response));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_grid, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rvPhotoGrid = (RecyclerView) view.findViewById(R.id.rvPhotoGrid);
        rvPhotoGrid.setAdapter(adapter);
        // Set Layout
        rvPhotoGrid.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }
}
