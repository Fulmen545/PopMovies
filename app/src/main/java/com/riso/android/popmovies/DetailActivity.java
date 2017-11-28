package com.riso.android.popmovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {

    private static final String TAG = "DetailActivity";
    private RecyclerView mTrailerList;
    private TrailerAdapter mAdapterTrailers;
    private RecyclerView mReviewList;
    private ReviewAdapter mAdapterReviews;
    public Toast mToast;
    private TrailerUrl[] trailers;
    private ReviewUrl[] reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView detailTitle;
        ImageView detailImage;
        TextView detailRelease;
        TextView detailVote;
        TextView detailOverView;
        String movieId;
//        trailers.add("Trailer1");
//        trailers.add("Trailer2");
//        trailers.add("Trailer3");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle getBundel = this.getIntent().getExtras();
        if (getBundel != null) {
            movieId = getBundel.getString("id");
            new GetTrailers().execute(movieId);
            new GetReviews().execute(movieId);
            detailTitle = (TextView) findViewById(R.id.detail_title);
            detailTitle.setText(getBundel.getString("title"));
            detailImage = (ImageView) findViewById(R.id.detail_poster);
            Picasso.with(this).load("http://image.tmdb.org/t/p/w342//" + getBundel.getString("poster_path"))
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .into(detailImage);
            detailRelease = (TextView) findViewById(R.id.detail_releaseDate);
            detailRelease.setText(dateFormatter(getBundel.getString("release_date")));
            detailVote = (TextView) findViewById(R.id.detail_vote);
            detailVote.setText(getBundel.getString("vote_average") + "/10");
            detailOverView = (TextView) findViewById(R.id.detail_overview);
            detailOverView.setText(getBundel.getString("overview"));

            mTrailerList = (RecyclerView) findViewById(R.id.rv_trailers);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mTrailerList.setLayoutManager(layoutManager);

            mReviewList = (RecyclerView) findViewById(R.id.rv_reviews);
            LinearLayoutManager layoutReviewManager = new LinearLayoutManager(this);
            mReviewList.setLayoutManager(layoutReviewManager);
        } else {
            internetDialog();
        }
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

    private String dateFormatter(String date) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-mm-dd");
        Date startDate = null;
        try {
            startDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd-mm-yyy");
        return formatter.format(startDate);
    }

    private void internetDialog() {
        Log.w(TAG, "Something went wrong");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Something went wrong")
                .setTitle("Please check your internet connection")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onListItemClick(int listItem) {
        Uri youtubeUrl = Uri.parse("https://www.youtube.com/watch?v=" + trailers[listItem].source);
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, youtubeUrl);
        if (youtubeIntent.resolveActivity(getPackageManager())!=null){
            startActivity(youtubeIntent);
        }
    }

    private class GetTrailers extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpHandler hh = new HttpHandler();
            String movieUrl = "http://api.themoviedb.org/3/movie/" + params[0] + "/trailers?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN;
            URL url;
            String jsonStr = null;
            try {
                url = new URL(movieUrl);
                jsonStr = hh.makeServiceCall(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            TrailerUrl trailer;

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray trailerJson = jsonObject.getJSONArray("youtube");
                    trailers = new TrailerUrl[trailerJson.length()];
                    for (int i = 0; i < trailerJson.length(); i++) {
                        JSONObject t = trailerJson.getJSONObject(i);
                        String tname = t.getString("name");
                        String tsource = t.getString("source");

                        trailer = new TrailerUrl(tname, tsource);
                        trailers[i] = trailer;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            List<TrailerUrl> trailerList = Arrays.asList(trailers);
            mAdapterTrailers = new TrailerAdapter(trailerList, DetailActivity.this);
            mTrailerList.setAdapter(mAdapterTrailers);
        }
    }

    private class GetReviews extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpHandler hh = new HttpHandler();
            String movieUrl = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN;
            URL url;
            String jsonStr = null;
            try {
                url = new URL(movieUrl);
                jsonStr = hh.makeServiceCall(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ReviewUrl review;

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray reviewJson = jsonObject.getJSONArray("results");
                    reviews = new ReviewUrl[reviewJson.length()];
                    for (int i = 0; i < reviewJson.length(); i++) {
                        JSONObject r = reviewJson.getJSONObject(i);
                        String rauthor = r.getString("author");
                        String rcontent = r.getString("content");

                        review = new ReviewUrl(rauthor, rcontent);
                        reviews[i] = review;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            List<ReviewUrl> reviewList = Arrays.asList(reviews);
            mAdapterReviews = new ReviewAdapter(reviewList, DetailActivity.this);
            mReviewList.setAdapter(mAdapterReviews);
        }
    }


}
