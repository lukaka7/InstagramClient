package com.codepath.instagram.networking;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by lujiawang on 10/28/15.
 */
public class InstagramClient {
    private static final String clientId = "f114b56ba673446f9659510b9717f638";
    private static final String clientSecret = "ae3afd5099014c598d2f7eb45d33186f";
    private static final String baseUrl = "https://api.instagram.com/v1/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl;
    }

    // This method should use the AsyncHttpClient in the android-async-http library to send a JSON request to:
    //    https://api.instagram.com/v1/media/popular?client_id={client_id}
    public static void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        String url = "media/popular?client_id=" + clientId;
        get(url, null, responseHandler);
    }

    public static void getAllComments(String mediaId, JsonHttpResponseHandler responseHandler) {
        String url = "media/" + mediaId + "/comments?client_id=" + clientId;
        get(url, null, responseHandler);
    }
}
