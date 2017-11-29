package com.riso.android.popmovies;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.riso.android.popmovies.data.FavoriteMovies;
import com.riso.android.popmovies.data.FavoritesDbHelper;
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
    private TrailerUrl[] trailers;
    private ReviewUrl[] reviews;
    private boolean favorite = false;
    private ImageView star;
    private TextView star_label;
    private SQLiteDatabase mDb;
    private String sMovieId;
    private String sTitle;
    private String sPoster;
    private String sDate;
    private String sVote;
    private String sPolt;
    private String sPopularity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView detailTitle;
        ImageView detailImage;
        TextView detailRelease;
        TextView detailVote;
        TextView detailOverView;
        FavoritesDbHelper dbHelper = new FavoritesDbHelper(this);
        mDb = dbHelper.getWritableDatabase();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        star = (ImageView) findViewById(R.id.star);
        star_label = (TextView) findViewById(R.id.star_label);

        star_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite();
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite();
            }
        });

        Bundle getBundel = this.getIntent().getExtras();
        if (getBundel != null) {
            bundelToStrings(getBundel);
            favorite=isFavorite();
            setFavoriteStyle();
            new GetTrailers().execute(sMovieId);
            new GetReviews().execute(sMovieId);
            detailTitle = (TextView) findViewById(R.id.detail_title);
            detailTitle.setText(sTitle);
            detailImage = (ImageView) findViewById(R.id.detail_poster);
            Picasso.with(this).load("http://image.tmdb.org/t/p/w342//" + sPoster)
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .into(detailImage);
            detailRelease = (TextView) findViewById(R.id.detail_releaseDate);
            detailRelease.setText(sDate);
            detailVote = (TextView) findViewById(R.id.detail_vote);
            detailVote.setText(sVote + "/10");
            detailOverView = (TextView) findViewById(R.id.detail_overview);
            detailOverView.setText(sPolt);

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

    private void  bundelToStrings(Bundle bundle){
        sMovieId = bundle.getString("id");
        sTitle = bundle.getString("title");
        sPoster = bundle.getString("poster_path");
        sDate = dateFormatter(bundle.getString("release_date"));
        sVote = bundle.getString("vote_average");
        sPolt = bundle.getString("overview");
        sPopularity = Double.toString(bundle.getDouble("popularity"));
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

    public void setFavoriteStyle() {
        if (favorite) {
            star.setImageResource(R.drawable.ic_star_blue);
            star_label.setText("In Favorites");
            star_label.setTextColor(getResources().getColor(R.color.holo_blue_bright));
        } else {
            star.setImageResource(R.drawable.ic_star_grey);
            star_label.setText("Put in Favorites");
            star_label.setTextColor(getResources().getColor(R.color.darker_gray));
        }
    }

    public void favorite() {
        if (favorite) {
            favorite = false;
            removeFromFavorites();
        } else {
            favorite = true;
            addToFavorites(sMovieId, sTitle, sPoster, sPolt, sVote, sDate, sPopularity);
        }
        setFavoriteStyle();
        Log.i(TAG, "RISOTEST: " + favorite);
    }

    private long addToFavorites(String movieId, String title, String poster,
                                String plot, String rating, String date, String popularity) {
        ContentValues cv = new ContentValues();
        cv.put(FavoriteMovies.FavoriteEntry.MOVIE_ID, movieId);
        cv.put(FavoriteMovies.FavoriteEntry.TITLE, title);
        cv.put(FavoriteMovies.FavoriteEntry.POSTER, poster);
        cv.put(FavoriteMovies.FavoriteEntry.PLOT, plot);
        cv.put(FavoriteMovies.FavoriteEntry.RATING, rating);
        cv.put(FavoriteMovies.FavoriteEntry.RELEASE_DATE, date);
        cv.put(FavoriteMovies.FavoriteEntry.POPULARITY, popularity);
        return mDb.insert(FavoriteMovies.FavoriteEntry.TABLE_NAME, null, cv);
    }

    private boolean removeFromFavorites (){
        return mDb.delete(FavoriteMovies.FavoriteEntry.TABLE_NAME,
                FavoriteMovies.FavoriteEntry.MOVIE_ID +"="+ sMovieId, null)>0;
    }

    private boolean isFavorite() {
//        Cursor cursor = mDb.query(
//                FavoriteMovies.FavoriteEntry.TABLE_NAME,
//                null,
//                FavoriteMovies.FavoriteEntry.MOVIE_ID +"=?",
//                new String[] {sMovieId},
//                null,
//                null,
//                null
//        );
        String[] whereArgs = new String[] {
                sMovieId
        };
        String query = "Select * from " + FavoriteMovies.FavoriteEntry.TABLE_NAME +
                " where " + FavoriteMovies.FavoriteEntry.MOVIE_ID + " = ?";
        Cursor cursor = mDb.rawQuery(query, whereArgs);
//        String name = cursor.getString(cursor.getColumnIndex(FavoriteMovies.FavoriteEntry.TITLE));
        if (!cursor.moveToNext()) {
            return false;
        } else {
            if (cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex(FavoriteMovies.FavoriteEntry.TITLE));
                    // do what ever you want here
                }while(cursor.moveToNext());
            }
            cursor.close();
//            String name = cursor.getString(cursor.getColumnIndex(FavoriteMovies.FavoriteEntry.TITLE));
            return true;
        }
    }


    @Override
    public void onListItemClick(int listItem) {
        Uri youtubeUrl = Uri.parse("https://www.youtube.com/watch?v=" + trailers[listItem].source);
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, youtubeUrl);
        if (youtubeIntent.resolveActivity(getPackageManager()) != null) {
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
