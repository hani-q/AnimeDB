package com.hani.q.animedb;
import retrofit.Callback;
import retrofit.http.GET;

public interface MovieApiService {
    @GET("/genre/16,12,35,10751/movies")
    void getPopularMovies(Callback<Anime.AnimeResults> cb);
}