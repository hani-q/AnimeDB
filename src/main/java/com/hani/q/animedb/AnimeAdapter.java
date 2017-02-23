package com.hani.q.animedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private final View.OnClickListener mOnClickListener = new AnimeViewClickListener();

    public AnimeAdapter (Context ctxt) {
        //setHasStableIds(true);
        this.mContext = ctxt;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mAnimeList = new ArrayList<>();
    }

    public Anime getItemfromAnimeList(int position) {
        return this.mAnimeList.get(position);
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_anime, parent, false);
        AnimeViewHolder viewHolder = new AnimeViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(AnimeViewHolder holder, int position) {
        Anime anime = mAnimeList.get(position);
        holder.textView.setText(anime.getTitle());

        Picasso.with(this.mContext)
                .load(anime.getPoster())
                .resize(600,600)
                .centerInside()
                .placeholder(R.color.colorAccent)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount()
    {
        return (this.mAnimeList == null) ? 0 : this.mAnimeList.size();
    }

    public void setAnimeList(List<Anime> animeList)
    {
        //this.mAnimeList.clear();
        int count = getItemCount();
        this.mAnimeList.addAll(animeList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyItemRangeInserted(count, animeList.size());
    }

    private class AnimeViewClickListener extends View.OnClickListener {
        @Override
        public void onClick(final View view) {
            Context mContext = view.getContext();
            RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);
            Anime item = mAnimeList.get(itemPosition);
            Toast.makeText(mContext, (CharSequence) item, Toast.LENGTH_LONG).show();
        }
    }
}
