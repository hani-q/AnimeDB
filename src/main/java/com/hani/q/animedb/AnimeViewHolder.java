package com.hani.q.animedb;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by hani Q on 2/18/2017.
 */

public  class AnimeViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView textView;

    public AnimeViewHolder(View itemView) {
        super(itemView);
        this.imageView = (ImageView)itemView.findViewById(R.id.animeImageView);
        this.textView = (TextView) itemView.findViewById(R.id.animeTextView);
    }
}
