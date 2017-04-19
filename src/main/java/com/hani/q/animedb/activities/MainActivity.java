package com.hani.q.animedb.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hani.q.animedb.R;
import com.hani.q.animedb.adapters.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tab_layout;
    CollapsingToolbarLayout collapsing_container;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    private static String LOG_TAG = MainActivity.class.getSimpleName();



    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        //Set Picasso Global Settings
        Picasso picasso = new Picasso.Builder(getApplicationContext())
                //.memoryCache(new LruCache(74000))
                .executor(Executors.newSingleThreadExecutor())
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .build();

        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);

        // set the global instance to use this Picasso object
        // all following Picasso (with Picasso.with(Context context) requests will use this Picasso object
        // you can only use the setSingletonInstance() method once!
        try {
            Picasso.setSingletonInstance(picasso);
        } catch (IllegalStateException ignored) {
            // Picasso instance was already set
            // cannot set it after Picasso.with(Context) was already in use
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        //viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(viewPagerAdapter);

        tab_layout = (TabLayout) findViewById(R.id.tabs);
        tab_layout.setupWithViewPager(viewPager);

        collapsing_container = (CollapsingToolbarLayout) findViewById(R.id.collapsing_container);


        setSupportActionBar(toolbar);
        collapsing_container.setTitle(getString(R.string.container_title));

        //FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
