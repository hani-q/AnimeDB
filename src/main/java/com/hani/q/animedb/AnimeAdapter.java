package com.hani.q.animedb;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
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
    public void onBindViewHolder(final AnimeViewHolder holder, int position) {
        Anime anime = mAnimeList.get(position);
        holder.textView.setText(anime.getTitle());

        /*//TODO(HaniQ): Add PlaceHolder and Error Image
        Picasso.with(this.mContext)
                .load(anime.getPoster())
                .placeholder(R.drawable.plus)
                .resize(600,600)
                .centerInside()
                //.transform(new RoundedCornersTransformation(5, 0))
                .into(holder.imageView);
*/
        holder.setImage(this.mContext, anime);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });


    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenuClickListener());
        popup.show();
    }

    @Override
    public int getItemCount() {
        return (this.mAnimeList == null) ? 0 : this.mAnimeList.size();
    }

    public Anime getItem(int position) {
        return  this.mAnimeList.get(position);
    }

    public void setAnimeList(List<Anime> animeList) {
        //this.mAnimeList.clear();
        int count = getItemCount();
        this.mAnimeList.addAll(animeList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyItemRangeInserted(count, animeList.size());
    }

    /**
     * Click listener for popup menu items
     */
    class PopupMenuClickListener implements PopupMenu.OnMenuItemClickListener {

        public PopupMenuClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

}
