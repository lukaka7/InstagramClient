package com.codepath.instagram.networking;

import android.content.Context;

import com.codepath.instagram.helpers.Constants;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.scribe.builder.api.Api;

/**
 * Created by lujiawang on 10/28/15.
 */
public class InstagramClient extends OAuthBaseClient {
    private static final String REST_URL = "https://api.instagram.com/v1";
    private static final Class<? extends Api> REST_API_CLASS = InstagramApi.class;
    private static final String REST_CONSUMER_KEY = "7f5321002cc04089b778e463cd87953f";
    private static final String REST_CONSUMER_SECRET = "a9980e6933814fd3848dba9f6b370b63";
    private static final String REDIRECT_URI = Constants.REDIRECT_URI;
    private static final String SCOPE = Constants.SCOPE;

    private static final String SELF_FEED_URL = "https://api.instagram.com/v1/users/self/feed";

    private static final String my_own_clientId = "f114b56ba673446f9659510b9717f638";
    private static final String my_own_clientSecret = "ae3afd5099014c598d2f7eb45d33186f";

    public InstagramClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REDIRECT_URI, SCOPE);
    }

    public void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        client.get(SELF_FEED_URL, null, responseHandler);
    }

    public void getPopularFeedSync(JsonHttpResponseHandler responseHandler) {
        AsyncHttpClient aClient = new SyncHttpClient();
        aClient.get(SELF_FEED_URL, new RequestParams("access_token", client.getAccessToken().getToken()), responseHandler);
    }

    public void getAllComments(String mediaId, JsonHttpResponseHandler responseHandler) {
        String partUrl = "media/" + mediaId + "/comments?client_id=" + REST_CONSUMER_KEY;
        String url = getApiUrl(partUrl);
        client.get(url, null, responseHandler);
    }

    public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        client.get(apiUrl, params, handler);
    }

    public void getSelfFeeds(JsonHttpResponseHandler responseHandler) {
        client.get(SELF_FEED_URL, null, responseHandler);
    }

    public void getSearchUsersResult(String searchTerm, JsonHttpResponseHandler responseHandler) {
        client.get("https://api.instagram.com/v1/users/search?q=" + searchTerm, null, responseHandler);
    }

    public void getSearchTagsResult(String searchTerm, JsonHttpResponseHandler responseHandler) {
        client.get("https://api.instagram.com/v1/tags/search?q=" + searchTerm, null, responseHandler);
    }
}
