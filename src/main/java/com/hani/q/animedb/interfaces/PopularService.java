package com.hani.q.animedb.interfaces;
import com.hani.q.animedb.models.Anime;

import retrofit.Callback;
import retrofit.http.GET;

public interface PopularService {
    @GET("/movie/popular")
    void getPopularAnime(Callback<Anime.AnimeResults> cb);
}
