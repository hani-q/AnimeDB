package com.hani.q.animedb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hani Q on 2/21/2017.
 */

public class PopularFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private AnimeAdapter mAdapter;
    private int page = 1;
    private static String LOG_TAG = PopularFragment.class.getSimpleName();

    public PopularFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new AnimeAdapter(getContext(), new AnimeItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Anime anime = mAdapter.getItem(position);
                Log.d(LOG_TAG, "clicked position:" + position);
                //TODO(Hani Q) Implement new Activity
                Intent detailIntent = new Intent(getContext(), DetailActivity.class);
                detailIntent.putExtra("Anime", anime);
                startActivity(detailIntent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        getAnimeData(page);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener((GridLayoutManager)mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                getAnimeData(current_page);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                FloatingActionButton fab = (FloatingActionButton) recyclerView.getRootView().findViewById(R.id.fab);
                if(((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() ==0){
                    //Its at top ..
                    fab.hide();
                }
                else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return view;
    }

    private void getAnimeData (final int page) {
        Log.d(LOG_TAG, String.format("Loading Page: %d", page));
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.moviedb_api_url))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", getString(R.string.APIKey));
                        request.addEncodedQueryParam("include_adult", "false");
                        request.addEncodedQueryParam("page", String.valueOf(page));
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();

        MovieApiService service = restAdapter.create(MovieApiService.class);
        service.getPopularMovies(new Callback<Anime.AnimeResults>() {
            @Override
            public void success(Anime.AnimeResults animeResult, Response response) {
                mAdapter.setAnimeList(animeResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

    }
}