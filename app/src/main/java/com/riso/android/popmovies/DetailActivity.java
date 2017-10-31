package com.riso.android.popmovies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;

public class DetailActivity extends AppCompatActivity {

    private TextView detailTitle;
    private ImageView detailImage;
    private TextView detailRelease;
    private TextView detailVote;
    private TextView detailOverView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle getBundel = this.getIntent().getExtras();
        detailTitle = (TextView) findViewById(R.id.detail_title);
        detailTitle.setText(getBundel.getString("title"));
        detailImage= (ImageView) findViewById(R.id.detail_poster);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w342//"+ getBundel.getString("poster_path") )
                .into(detailImage);
        detailRelease = (TextView) findViewById(R.id.detail_releaseDate);
        detailRelease.setText(getBundel.getString("release_date"));
        detailVote = (TextView) findViewById(R.id.detail_vote);
        detailVote.setText(getBundel.getString("vote_average")+"/10");
        detailOverView = (TextView) findViewById(R.id.detail_overview);
        detailOverView.setText(getBundel.getString("overview"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
