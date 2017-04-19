package com.hani.q.animedb.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.hani.q.animedb.BuildConfig;
import com.hani.q.animedb.activities.MainActivity;
import com.hani.q.animedb.interfaces.PopularService;
import com.hani.q.animedb.models.Anime;
import com.hani.q.animedb.adapters.AnimeAdapter;
import com.hani.q.animedb.listeners.AnimeItemClickListener;
import com.hani.q.animedb.activities.DetailActivity;
import com.hani.q.animedb.listeners.EndlessRecyclerOnScrollListener;
import com.hani.q.animedb.R;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by hani Q on 2/21/2017.
 */

public class PopularFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private AnimeAdapter mAdapter;
    private int page = 1;
    private static String LOG_TAG = PopularFragment.class.getSimpleName();
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private static String api_key;

    // Remote Config keys
    private static final String API_KEY = "tmdb_api_key";


    public PopularFragment() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(LOG_TAG, "Creating Fragment View");
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new AnimeAdapter(getContext(), new AnimeItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Anime anime = mAdapter.getItem(position);
                Log.d(LOG_TAG, "clicked position:" + position);
                Intent detailIntent = new Intent(getContext(), DetailActivity.class);
                detailIntent.putExtra("Anime", anime);
                startActivity(detailIntent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        page = 1;


        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Create a Remote Config Setting to enable developer mode, which you can use to increase
        // the number of fetches available per hour during development. See Best Practices in the
        // README for more information.
        // [START enable_dev_mode]
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        // [END enable_dev_mode]

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }



        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
        // will use fetch data from the Remote Config service, rather than cached parameter values,
        // if cached parameter values are more than cacheExpiration seconds old.
        // See Best Practices in the README for more information.
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            api_key = mFirebaseRemoteConfig.getString(API_KEY);
                            FetchDataTask dataTask = new FetchDataTask();
                            dataTask.execute(String.valueOf(page));
                            page++;

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(getContext(), "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END fetch_config_with_callback]



        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener((GridLayoutManager)mRecyclerView.getLayoutManager(), getContext()) {
            @Override
            public void onLoadMore(int current_page) {
                FetchDataTask dataTask = new FetchDataTask();
                dataTask.execute(String.valueOf(page));
                page++;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                FloatingActionButton fab = (FloatingActionButton) recyclerView.getRootView().findViewById(R.id.fab);
                if(((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0){
                    //Its at top ..
                    fab.hide();
                }
                else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }

                final Picasso picasso = Picasso.with(mContext);
                if (newState == SCROLL_STATE_IDLE || newState == SCROLL_STATE_DRAGGING) {
                    picasso.resumeTag(mContext);
                } else {
                    picasso.pauseTag(mContext);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return view;
    }



    private class FetchDataTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchDataTask.class.getSimpleName();

        @Override
        protected Void doInBackground(final String... pages) {
            if (pages[0] == null)
                return null;
            Log.d(LOG_TAG, String.format("Loading Page: %s", pages[0]));
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(getString(R.string.moviedb_api_url))
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addEncodedQueryParam("api_key", api_key);
                            request.addEncodedQueryParam("include_adult", "false");
                            request.addEncodedQueryParam("page", pages[0]);
                        }
                    })
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .build();

            PopularService service = restAdapter.create(PopularService.class);
            service.getPopularAnime(new Callback<Anime.AnimeResults>() {
                @Override
                public void success(Anime.AnimeResults animeResult, Response response) {
                    mAdapter.setAnimeList(animeResult.getResults());
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
            return null;
        }
    }
}