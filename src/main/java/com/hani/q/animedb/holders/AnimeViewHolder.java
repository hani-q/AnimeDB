package com.hani.q.animedb.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hani.q.animedb.R;


/**
 * Created by hani Q on 2/18/2017.
 */

public  class AnimeViewHolder extends RecyclerView.ViewHolder {
    private static final String LOG_TAG = AnimeViewHolder.class.getSimpleName();
    public ImageView imageView;
    public TextView textView;
    public ImageView overflow;
    public ProgressBar progressBar;
    public RatingBar ratingBar;


    public AnimeViewHolder(View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.posterImageView);
        this.progressBar = (ProgressBar) itemView.findViewById(R.id.posterProgressBar);
        this.textView = (TextView) itemView.findViewById(R.id.posterTextView);
        this.overflow = (ImageView) itemView.findViewById(R.id.overflow);
        this.ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
    }
}
