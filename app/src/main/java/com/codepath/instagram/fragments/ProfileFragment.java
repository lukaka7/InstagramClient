package com.codepath.instagram.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.HomeActivity;
import com.codepath.instagram.networking.InstagramClient;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by lujiawang on 11/4/15.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private String userId = "";

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String userId) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("UserId", userId);
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("UserId");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final SimpleDraweeView dvAvatar = (SimpleDraweeView) view.findViewById(R.id.dvAvatar);
        final TextView tvMedia = (TextView) view.findViewById(R.id.tvPostsCount);
        final TextView tvFollowedBy = (TextView) view.findViewById(R.id.tvFollowersCount);
        final TextView tvFollows = (TextView) view.findViewById(R.id.tvFollowingCount);

        String searchTag = null;

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        // Pass userId/searchTag to PhotoGridFragment
        PhotoGridFragment photoGridFragment = PhotoGridFragment.newInstance(userId, searchTag);
        ft.replace(R.id.flPhotoGrid, photoGridFragment);
        ft.commit();

        InstagramClient client = HomeActivity.getRestClient();
        Log.i(TAG, "userId = " + userId);

        // Get profile avatar
        client.getUser(userId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, "Get user succeed!!!!!!!!!!!!!");
                if (response != null) {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        Log.i(TAG, data.toString());
                        JSONObject count = data.getJSONObject("counts");
                        Log.i(TAG, count.toString());
                        Uri avatarUri = Uri.parse(data.getString("profile_picture"));
                        Log.i(TAG, avatarUri.toString());
                        dvAvatar.setImageURI(avatarUri);
                        tvMedia.setText(count.getString("media"));
                        tvFollowedBy.setText(count.getString("followed_by"));
                        tvFollows.setText(count.getString("follows"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i(TAG, "getUser failed! " + responseString);

            }
        });
    }

}
