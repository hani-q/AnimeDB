package com.hani.q.animedb;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

/**
 * Created by hani Q on 2/21/2017.
 */

public class NewReleaseFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private AnimeAdapter mAdapter;
    private int page = 1;
    private static String LOG_TAG = NewReleaseFragment.class.getSimpleName();

    public NewReleaseFragment() {
    }
}