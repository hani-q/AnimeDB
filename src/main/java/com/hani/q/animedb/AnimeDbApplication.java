package com.hani.q.animedb;

/**
 * Created by hanan on 4/29/2017.
 */

import android.app.Application;
import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import timber.log.Timber;

public class AnimeDbApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Set Picasso Global Settings
        Picasso picasso = new Picasso.Builder(getApplicationContext())
                //.memoryCache(new LruCache(74000))
                .executor(Executors.newSingleThreadExecutor())
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .build();

        picasso.setIndicatorsEnabled(false);
        picasso.setLoggingEnabled(false);

        // set the global instance to use this Picasso object
        // all following Picasso (with Picasso.with(Context context) requests will use this Picasso object
        // you can only use the setSingletonInstance() method once!
        try {
            Picasso.setSingletonInstance(picasso);
        } catch (IllegalStateException ignored) {
            // Picasso instance was already set
            // cannot set it after Picasso.with(Context) was already in use
        }

        Timber.plant(new Timber.DebugTree());
        ButterKnife.setDebug(BuildConfig.DEBUG);
    }
}
