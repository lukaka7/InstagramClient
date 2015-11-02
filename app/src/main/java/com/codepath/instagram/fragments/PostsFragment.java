package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.HomeActivity;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lujiawang on 10/31/15.
 */
public class PostsFragment extends Fragment {
    private static final String TAG = "PostsFragment";
    private static final Integer VERTICAL_ITEM_SPACE = 24;
    private ArrayList<InstagramPost> posts;
    private InstagramPostsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        posts = new ArrayList<InstagramPost>();
        // Create Adapter
        adapter = new InstagramPostsAdapter(posts);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Get RecyclerView Reference
        RecyclerView rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);

        // Set Adapter
        rvPosts.setAdapter(adapter);

        // Set Layout
        rvPosts.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvPosts.addItemDecoration(new SimpleVerticalSpacerItemDecoration(VERTICAL_ITEM_SPACE));

        // get photos
        InstagramClient client = HomeActivity.getRestClient();
        client.getPopularFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, Integer.toString(statusCode));
                posts.addAll(Utils.decodePostsFromJsonResponse(response));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, Integer.toString(statusCode));
                Log.e(TAG, responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }
}
