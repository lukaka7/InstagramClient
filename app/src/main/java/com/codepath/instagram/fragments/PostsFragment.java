package com.codepath.instagram.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.codepath.instagram.services.InstagramService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lujiawang on 10/31/15.
 */
public class PostsFragment extends Fragment {
    private static final String TAG = "PostsFragment";
    private static final Integer VERTICAL_ITEM_SPACE = 24;
    private ArrayList<InstagramPost> posts;
    private InstagramPostsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private InstagramClientDatabase database;
    private Handler handler = new Handler();

    // Define the callback for what to do when data is received
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra(InstagramService.KEY_RESULT_CODE, Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK) {
                InstagramPosts postsWrapper = (InstagramPosts) intent.getSerializableExtra(InstagramService.KEY_RESULTS);
                List<InstagramPost> posts = postsWrapper.getPosts();
                adapter.clear();
                adapter.addAll(posts);
                swipeContainer.setRefreshing(false);
                database.emptyAllTables();
                database.addInstagramPosts(posts);
            } else {
                Toast.makeText(getContext(), "Failed to fetch popular feeds from Instagram!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        posts = new ArrayList<InstagramPost>();
        // Create Adapter
        adapter = new InstagramPostsAdapter(posts);
        // Create database
        database = InstagramClientDatabase.getInstance(this.getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get SwipeRefreshLayout swipeContainer
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        // Get RecyclerView Reference
        RecyclerView rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);

        // Set Adapter
        rvPosts.setAdapter(adapter);

        // Set Layout
        rvPosts.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvPosts.addItemDecoration(new SimpleVerticalSpacerItemDecoration(VERTICAL_ITEM_SPACE));

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                fetchPosts();
                handler.post(refreshing);
            }
        });
        swipeContainer.setColorSchemeResources(R.color.blush_blue, R.color.blush_green,
                R.color.blush_orange, R.color.blush_red);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    private final Runnable refreshing = new Runnable() {
        @Override
        public void run() {
            try {
                fetchPosts();
                handler.postDelayed(this, 30000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void fetchPosts() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(this.getContext(), InstagramService.class);
            this.getContext().startService(intent);
        } else {
            List<InstagramPost> newPosts = database.getAllInstagramPosts();
            adapter.addAll(newPosts);
            swipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(InstagramService.ACTION);
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(receiver);
        // or `unregisterReceiver(testReceiver)` for a normal broadcast
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
