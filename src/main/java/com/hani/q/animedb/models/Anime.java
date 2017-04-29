package com.hani.q.animedb.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@IgnoreExtraProperties
public class Anime {
    public static final String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w500";
    public static final String FALLBACK_IMAGE_PATH_PATH = "http://image.tmdb.org/t/p/w500";

    public List<Integer> genre_ids;
    public String original_language;
    public  String original_title;
    public Float popularity;
    public String release_date;
    public String title;
    public int vote_count;
    public int vote_average;
    public String poster_path;
    public String overview;
    public String backdrop_path;

    public Anime() {}


    public float getRating() {
        return  popularity;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        if (this.poster_path == null)
            if (this.backdrop_path == null)
                return FALLBACK_IMAGE_PATH_PATH;
        return TMDB_IMAGE_PATH + this.poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdrop_path() {
        if (this.backdrop_path == null)
             if (this.poster_path == null)
                 return FALLBACK_IMAGE_PATH_PATH;
        return TMDB_IMAGE_PATH  + this.backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public static class AnimeResults {
        private List<Anime> results;

        public List<Anime> getResults() {
            return results;
        }
    }
}