package com.codepath.instagram.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.PostsFragment;
import com.codepath.instagram.fragments.ProfileFragment;
import com.codepath.instagram.fragments.SearchFragment;
import com.codepath.instagram.fragments.SearchTagsResultFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

/**
 * Created by lujiawang on 10/31/15.
 */
public class HomeFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {
    private static final String TAG = "StatePagerAdapter";
    private static int NUM_ITEMS = 5;
    private int[] imageResId = {
            R.drawable.ic_home,
            R.drawable.ic_search,
            R.drawable.ic_capture,
            R.drawable.ic_notifs,
            R.drawable.ic_profile
    };
    private Context context;

    public HomeFragmentStatePagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i(TAG, "Fragment getItem " + position);
        switch (position) {
            case 0:
                return new PostsFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new SearchTagsResultFragment();
            case 3:
            case 4:
                ProfileFragment profileFragment = ProfileFragment.newInstance("");
                return profileFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
