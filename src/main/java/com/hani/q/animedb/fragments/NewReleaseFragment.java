package com.hani.q.animedb.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.hani.q.animedb.adapters.PopularViewAdapter;

/**
 * Created by hani Q on 2/21/2017.
 */

public class NewReleaseFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private PopularViewAdapter mAdapter;
    private int page = 1;
    private static String LOG_TAG = NewReleaseFragment.class.getSimpleName();

    public NewReleaseFragment() {
    }
}