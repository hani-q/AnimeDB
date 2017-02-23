package com.hani.q.animedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO(HaniQ): Adding a darn desctription
 */

public class AnimeAdapter extends RecyclerView.Adapter<AnimeViewHolder> {
    private List<Anime> mAnimeList;
    private LayoutInflater mInflater;
    private Context mContext;
    private AnimeItemClickListener listener;

    public AnimeAdapter (Context ctxt, AnimeItemClickListener listener) {
        //setHasStableIds(true);
        this.mContext = ctxt;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mAnimeList = new ArrayList<>();
        this.listener = listener;
    }


    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_anime, parent, false);
        final AnimeViewHolder viewHolder = new AnimeViewHolder(view);
        view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getLayoutPosition());
            }
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(AnimeViewHolder holder, int position) {
        Anime anime = mAnimeList.get(position);
        holder.textView.setText(anime.getTitle());

        //TODO(HaniQ): Add PlaceHolder and Error Image
        Picasso.with(this.mContext)
                .load(anime.getPoster())
                .error(R.drawable.plus)
                .placeholder(R.drawable.plus)
                .resize(600,600)
                .centerInside()
                //.transform(new RoundedCornersTransformation(5, 0))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return (this.mAnimeList == null) ? 0 : this.mAnimeList.size();
    }

    public void setAnimeList(List<Anime> animeList) {
        //this.mAnimeList.clear();
        int count = getItemCount();
        this.mAnimeList.addAll(animeList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyItemRangeInserted(count, animeList.size());
    }
}
