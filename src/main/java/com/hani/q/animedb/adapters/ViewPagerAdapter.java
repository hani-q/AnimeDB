package com.hani.q.animedb.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hani.q.animedb.R;
import com.hani.q.animedb.fragments.NewReleaseFragment;
import com.hani.q.animedb.fragments.PopularFragment;
import com.hani.q.animedb.fragments.WatchedFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context ctxt = null;
    public ViewPagerAdapter(FragmentManager fm, Context ctxt) {
        super(fm);
        this.ctxt = ctxt;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new PopularFragment();
        }
        if (position == 1)
        {
            fragment = new NewReleaseFragment();
        }
        if (position == 2)
        {
            fragment = new WatchedFragment();
        }
        return fragment;
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title =  this.ctxt.getString(R.string.tab_popular_title);
        }
        else if (position == 1)
        {
            title = this.ctxt.getString(R.string.tab_new_releases_title);
        }
        else if (position == 2)
        {
            title = this.ctxt.getString(R.string.tab_watched_title);
        }
        return title;
    }


}