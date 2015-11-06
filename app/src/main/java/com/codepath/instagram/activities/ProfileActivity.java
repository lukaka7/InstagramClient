package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.ProfileFragment;

/**
 * Created by lujiawang on 11/5/15.
 */
public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    public static final String EXTRA_USER_ID = "UserID";
    public static final String EXTRA_USER_NAME = "UserName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);

        String userId = getIntent().getStringExtra(EXTRA_USER_ID);
        String userName = getIntent().getStringExtra(EXTRA_USER_NAME);
        tvToolbarTitle.setText(userName);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Pass userId to ProfileFragment
        ProfileFragment profileFragment = ProfileFragment.newInstance(userId);
        ft.replace(R.id.flProfile, profileFragment);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
