package com.hani.q.animedb.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.hani.q.animedb.R;
import com.hani.q.animedb.adapters.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

import at.favre.lib.dali.Dali;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.anime_toolbar) Toolbar toolbar;

    @BindView(R.id.tabs) TabLayout tab_layout;

    @BindView(R.id.collapsing_container) CollapsingToolbarLayout collapsing_container;

    @BindView(R.id.tab_viewpager) ViewPager viewPager;

    @BindView(R.id.backdrop)
    ImageView backdrop;

    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        Picasso.with(this)
                .load(R.drawable.gibli)
                .into(backdrop);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(viewPagerAdapter);

        tab_layout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
        collapsing_container.setTitle(getString(R.string.container_title));

        Timber.tag("LifeCycles");
        Timber.d("Activity Created");
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
