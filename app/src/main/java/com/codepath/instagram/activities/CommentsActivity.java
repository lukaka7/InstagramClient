package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramCommentsAdapter;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.networking.InstagramClient;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CommentsActivity extends AppCompatActivity {
    private static final String TAG = "CommentsActivity";
    private static final Integer VERTICAL_ITEM_SPACE = 16;

    private ArrayList<InstagramComment> comments;
    private InstagramCommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_comments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("COMMENTS");

        String mediaId = getIntent().getStringExtra("mediaId");
        Log.i(TAG, mediaId);

        // Get RecyclerView Reference
        RecyclerView rvComments = (RecyclerView) findViewById(R.id.rvComments);

        // Create Adapter
        comments = new ArrayList<InstagramComment>();
        adapter = new InstagramCommentsAdapter(comments);

        // Set Adapter
        rvComments.setAdapter(adapter);

        // Set Layout
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.addItemDecoration(new SimpleVerticalSpacerItemDecoration(VERTICAL_ITEM_SPACE));

        // get photos
        InstagramClient.getAllComments(mediaId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                comments.addAll(Utils.decodeCommentsFromJsonResponse(response));
                adapter.notifyDataSetChanged();
            }
        });

    }

}
