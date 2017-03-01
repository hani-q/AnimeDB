package com.hani.q.animedb;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by hani Q on 2/22/2017.
 */

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    public static String LOG_TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int current_page = 1;
    GridLayoutManager mlayoutManager;
    Context mContext;

    public EndlessRecyclerOnScrollListener(
            GridLayoutManager gridLayoutManager, Context context) {
        this.mlayoutManager = gridLayoutManager;
        this.mContext = context;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        FloatingActionButton fab = (FloatingActionButton) recyclerView.getRootView().findViewById(R.id.fab);
        if (dy > 0 ||dy<0 && fab.isShown())
        {
            fab.hide();
        }
        visibleItemCount = mlayoutManager.getChildCount();
        totalItemCount = mlayoutManager.getItemCount();
        firstVisibleItem = mlayoutManager.findFirstVisibleItemPosition();
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading
                && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}