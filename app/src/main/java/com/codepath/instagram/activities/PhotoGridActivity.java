package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.PhotoGridFragment;

/**
 * Created by lujiawang on 11/4/15.
 */
public class PhotoGridActivity extends AppCompatActivity {
    private static final String TAG = "PhotoGridActivity";
    public static final String EXTRA_USER_ID = "UserID";
    public static final String EXTRA_SEARCH_TAG = "TagName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);

        String userId = getIntent().getStringExtra(EXTRA_USER_ID);
        String searchTag = getIntent().getStringExtra(EXTRA_SEARCH_TAG);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Pass userId/searchTag to PhotoGridFragment
        PhotoGridFragment photoGridFragment = PhotoGridFragment.newInstance(userId, searchTag);
        ft.replace(R.id.flPhotoGrid, photoGridFragment);
        ft.commit();
    }
}
