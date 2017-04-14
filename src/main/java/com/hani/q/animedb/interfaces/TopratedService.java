package com.hani.q.animedb.interfaces;

import com.hani.q.animedb.models.Anime;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by hani Q on 4/11/2017.
 */

public interface TopratedService {
    @GET("/movie/top_rated")
    void getTopratedAnime(Callback<Anime.AnimeResults> cb);
}
