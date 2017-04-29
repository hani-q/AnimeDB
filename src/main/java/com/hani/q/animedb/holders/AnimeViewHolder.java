package com.hani.q.animedb.holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hani.q.animedb.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public  class AnimeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_view)
    public CardView cardView;

    @BindView(R.id.posterImageView)
    public ImageView imageView;

    @BindView(R.id.posterTextView)
    public TextView textView;

    @BindView(R.id.overflow)
    public ImageView overflow;

//    @BindView(R.id.posterProgressBar)
//    public ImageView progressBar;

    @BindView(R.id.rating)
    public RatingBar ratingBar;


    public AnimeViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
