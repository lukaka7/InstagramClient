package com.codepath.instagram.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.codepath.instagram.activities.HomeActivity;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by lujiawang on 11/3/15.
 */
public class InstagramService extends IntentService {
    static final String TAG = "InstagramService";
    public static final String INTENT_URL = "INTENT_URL";

    private final String url = "https://api.instagram.com/v1/users/self/feed";
    public static final String KEY_RESULTS = "KeyResults";
    public static final String KEY_RESULT_CODE = "KeyResultCode";
    public static final String ACTION = "com.codepath.instagram.services.instagramservice";

    public InstagramService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent!!!!!!!!");
        InstagramClient client = HomeActivity.getRestClient();
        client.getPopularFeedSync(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, "InstagramService success!!!!!!!!");
                List<InstagramPost> posts = Utils.decodePostsFromJsonResponse(response);
                Log.i(TAG, "InstagramService success!!!!!!!! post size = " + posts.size());
                InstagramPosts postsWrapper = new InstagramPosts(posts);
                Intent in = new Intent(ACTION);
                in.putExtra(KEY_RESULT_CODE, Activity.RESULT_OK);
                in.putExtra(KEY_RESULTS, postsWrapper);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(in);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
