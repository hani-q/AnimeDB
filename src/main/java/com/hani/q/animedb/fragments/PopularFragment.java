package com.hani.q.animedb.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hani.q.animedb.R;
import com.hani.q.animedb.activity.DetailActivity;
import com.hani.q.animedb.adapters.PopularViewAdapter;
import com.hani.q.animedb.interfaces.PopularService;
import com.hani.q.animedb.listeners.AnimeItemClickListener;
import com.hani.q.animedb.listeners.EndlessRecyclerOnScrollListener;
import com.hani.q.animedb.models.Anime;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;


public class PopularFragment extends Fragment {

    @Nullable
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    FloatingActionButton fab;

    private PopularViewAdapter mAdapter;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        Timber.tag(PopularFragment.class.getSimpleName());
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        unbinder = ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new PopularViewAdapter(getContext(), new AnimeItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Anime anime = mAdapter.getItem(position);
                Timber.d("clicked position:" + position);
                Intent detailIntent = new Intent(getContext(), DetailActivity.class);
                //detailIntent.putExtra("Anime", anime);
                startActivity(detailIntent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        fab = ButterKnife.findById(getActivity(), R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });


        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("v1/movies");
        final List<Anime> mAnimeList = new ArrayList<Anime>();

        // Read from the database
        myRef.orderByChild("popularity")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot mvSnapshot: dataSnapshot.getChildren()) {
                            Anime anime = mvSnapshot.getValue(Anime.class);
                            mAnimeList.add(anime);
                            //Timber.d("Value is: " + mvSnapshot.getKey());
                        }
                        Collections.reverse(mAnimeList);
                        mAdapter.setAnimeList(mAnimeList);
                        Timber.d("Total Movie Count is: " + dataSnapshot.getChildrenCount());
                        
                        myRef.goOffline();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, Timber a message
                        Timber.e("FAILURE", databaseError.toException());
                    }
                });



        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener((GridLayoutManager)mRecyclerView.getLayoutManager(), getContext()) {
            @Override
            public void onLoadMore(int current_page) {
//                FetchDataTask dataTask = new FetchDataTask();
//                dataTask.execute(String.valueOf(page));
//                page++;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {

                FloatingActionButton fab = ButterKnife.findById(getActivity(), R.id.fab);
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

        Timber.d("Popular Fragment Created");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class FetchDataTask extends AsyncTask<String, Void, Void> {

        private final String Timber_TAG = FetchDataTask.class.getSimpleName();

        @Override
        protected Void doInBackground(final String... pages) {
            if (pages[0] == null)
                return null;
            Timber.d(Timber_TAG, String.format("Loading Page: %s", pages[0]));
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(getString(R.string.moviedb_api_url))
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addEncodedQueryParam("api_key", "");
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