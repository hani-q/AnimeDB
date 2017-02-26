package com.hani.q.animedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


/**
 * Created by hani Q on 2/18/2017.
 */

public  class AnimeViewHolder extends RecyclerView.ViewHolder {
    private static final String LOG_TAG = AnimeViewHolder.class.getSimpleName();
    public ImageView imageView;
    public TextView textView;
    public ImageView overflow;

    public AnimeViewHolder(View itemView) {
        super(itemView);
        this.imageView = (ImageView)itemView.findViewById(R.id.animeImageView);
        this.textView = (TextView) itemView.findViewById(R.id.animeTextView);
        this.overflow = (ImageView) itemView.findViewById(R.id.overflow);

    }

    public void setImage(final Context ctxt, final Anime anime) {

        Picasso.with(ctxt)
                .load(anime.getPoster())
                .placeholder(R.drawable.animate_rotate)
                //.resize(800,600)
                //.fit()
                //.transform(new RoundedTransformation(10, 10))
                .error(R.drawable.image)
                .into(this.imageView);
    }
}
