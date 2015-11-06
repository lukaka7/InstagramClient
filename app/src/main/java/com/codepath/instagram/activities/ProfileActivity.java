package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.ProfileFragment;

/**
 * Created by lujiawang on 11/5/15.
 */
public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    public static final String EXTRA_USER_ID = "UserID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userId = getIntent().getStringExtra(EXTRA_USER_ID);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Pass userId to ProfileFragment
        ProfileFragment profileFragment = ProfileFragment.newInstance(userId);
        ft.replace(R.id.flProfile, profileFragment);
        ft.commit();
    }
}
