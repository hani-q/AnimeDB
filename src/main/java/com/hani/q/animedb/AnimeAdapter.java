package com.hani.q.animedb;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static java.lang.Math.*;

/**
 * TODO(HaniQ): Adding a darn desctription
 */

public class AnimeAdapter extends RecyclerView.Adapter<AnimeViewHolder> {
    private static final String LOG_TAG = AnimeViewHolder.class.getSimpleName();
    private List<Anime> mAnimeList;
    private LayoutInflater mInflater;
    private Context mContext;
    private AnimeItemClickListener listener;
    private int width, height;

    public AnimeAdapter (Context mContext, AnimeItemClickListener listener) {

        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mAnimeList = new ArrayList<>();
        this.listener = listener;
        this.width = (int) round(mContext.getResources().getInteger(R.integer.TMDB_poster_width) * (0.65));
        this.height = (int) round(mContext.getResources().getInteger(R.integer.TMDB_poster_height) * (0.65));

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
        final Anime anime = mAnimeList.get(position);
        holder.textView.setText(anime.getTitle());
        int max_stars = mContext.getResources().getInteger(R.integer.max_stars);
        float adjusted_rating = (anime.getRating() / 10) * (max_stars);
        holder.ratingBar.setRating(adjusted_rating);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);

            }
        });

        Picasso.with(mContext)
                .load(anime.getPoster())
                .config(Bitmap.Config.RGB_565)
                .networkPolicy(NetworkPolicy.OFFLINE)
                //.resize(width, height)
                .resizeDimen(R.dimen.TMDB_poster_width, R.dimen.TMDB_poster_height)
                .centerCrop()
                .tag(mContext)
                .into(holder.imageView,  new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        Picasso.with(mContext)
                                .load(anime.getPoster())
                                //.resize(width, height)
                                .resizeDimen(R.dimen.TMDB_poster_width, R.dimen.TMDB_poster_height)
                                .tag(mContext)
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .into(holder.imageView);
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
        PreFetchImageTask preFetchImageTask = new PreFetchImageTask();
        preFetchImageTask.execute(animeList);

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

    public class PreFetchImageTask extends AsyncTask<List<Anime>, Void, Void> {

        private final String LOG_TAG = AnimeAdapter.PreFetchImageTask.class.getSimpleName();

        @Override
        protected Void doInBackground(final List<Anime>... animeList) {
            Log.d(LOG_TAG,"Pre fetching images");
            if (animeList[0] == null)
                return null;
            final int size = animeList[0].size();
            for (int i = 0; i < size; i++) {
                Anime anime = animeList[0].get(i);
                Picasso.with(mContext)
                        .load(anime.getPoster())
                        .resize(width, height)
                        .tag(mContext)
                        .config(Bitmap.Config.RGB_565)
                        .fetch();
            }
            return null;
        }
    }

//    public class FetchDataTask extends AsyncTask<String, Void, Void> {
//
//        private final String LOG_TAG = PopularFragment.FetchDataTask.class.getSimpleName();
//
//        @Override
//        protected Void doInBackground(final String... params) {
//            if (params[0] == null || params[1] == null || params[2] == null)
//                return null;
//            final String page = params[0];
//            final String url = params[1];
//            final String key = params[2];
//
//            Log.d(LOG_TAG, String.format("Loading Page: %s", page));
//            RestAdapter restAdapter = new RestAdapter.Builder()
//                    .setEndpoint(url)
//                    .setRequestInterceptor(new RequestInterceptor() {
//                        @Override
//                        public void intercept(RequestFacade request) {
//                            request.addEncodedQueryParam("api_key", key);
//                            request.addEncodedQueryParam("include_adult", "false");
//                            request.addEncodedQueryParam("page", page);
//                        }
//                    })
//                    .setLogLevel(RestAdapter.LogLevel.BASIC)
//                    .build();
//
//            MovieApiService service = restAdapter.create(MovieApiService.class);
//            service.getPopularMovies(new retrofit.Callback<Anime.AnimeResults>() {
//                @Override
//                public void success(Anime.AnimeResults animeResult, Response response) {
//                    setAnimeList(animeResult.getResults());
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    error.printStackTrace();
//                }
//            });
//            return null;
//        }
//    }
}
