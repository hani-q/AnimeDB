package com.hani.q.animedb;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hani Q on 2/18/2017.
 */

public  class AnimeViewHolder extends RecyclerView.ViewHolder {
    private static final String LOG_TAG = AnimeViewHolder.class.getSimpleName();
    public ImageView imageView;
    public TextView textView;

    public AnimeViewHolder(View itemView) {
        super(itemView);
        this.imageView = (ImageView)itemView.findViewById(R.id.animeImageView);
        this.textView = (TextView) itemView.findViewById(R.id.animeTextView);
    }
}
