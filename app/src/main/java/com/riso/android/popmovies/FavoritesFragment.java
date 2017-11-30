package com.riso.android.popmovies;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.riso.android.popmovies.data.FavoriteMovies;
import com.riso.android.popmovies.data.FavoritesDbHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Created by richard.janitor on 30-Nov-17.
 */

public class FavoritesFragment extends Fragment {
    private SQLiteDatabase mDb;
    private GridView gridView;
    private PopularMoviesAdapter moviesAdapter;
    private PopularMovies[] popularMovies;
    private PopularMovies[] popularMovies2;
    private static final String TAG = "FavoritesFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FavoritesDbHelper dbHelper = new FavoritesDbHelper(view.getContext());
        mDb = dbHelper.getWritableDatabase();
        new GetFavoritesFromDB().execute();
        gridView = (GridView) view.findViewById(R.id.movies_grid);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.i(TAG, "Position: " + popularMovies[position].title);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", popularMovies[position].movieId);
                bundle.putString("title", popularMovies[position].title);
                bundle.putString("poster_path", popularMovies[position].poster);
                bundle.putString("overview", popularMovies[position].plot);
                bundle.putString("vote_average", popularMovies[position].rating.toString());
                bundle.putString("release_date", popularMovies[position].releaseDate);
                bundle.putDouble("popularity", popularMovies[position].popularity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private class GetFavoritesFromDB extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Cursor cursor = mDb.query(
                    FavoriteMovies.FavoriteEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            PopularMovies favorite;
            int i = 0;
            if (cursor.getCount()==0){
                return false;
            } else {
                popularMovies = new PopularMovies[cursor.getCount()];
                if (cursor.moveToFirst()) {
                    do {
                        String fmovieId = cursor.getString(cursor.getColumnIndex(FavoriteMovies.FavoriteEntry.MOVIE_ID));
                        String ftitle = cursor.getString(cursor.getColumnIndex(FavoriteMovies.FavoriteEntry.TITLE));
                        String fposter = cursor.getString(cursor.getColumnIndex(FavoriteMovies.FavoriteEntry.POSTER));
                        String fploit = cursor.getString(cursor.getColumnIndex(FavoriteMovies.FavoriteEntry.PLOT));
                        Double frating = Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteMovies.FavoriteEntry.RATING)));
                        String fdate = cursor.getString(cursor.getColumnIndex(FavoriteMovies.FavoriteEntry.RELEASE_DATE));
                        Double fpopularity = Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteMovies.FavoriteEntry.POPULARITY)));

                        favorite = new PopularMovies(fmovieId, ftitle, fposter, fploit, frating, fdate, fpopularity);
                        popularMovies[i] = favorite;
                        i++;
                    }
                    while (cursor.moveToNext());
                    popularMovies2 = new PopularMovies[popularMovies.length];
                    for (int j = 0; j < popularMovies.length; j++) {
                        popularMovies2[j] = popularMovies[j];
                    }
                }
                cursor.close();
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean dataPresent) {
            if (dataPresent) {
                List<PopularMovies> movieList = Arrays.asList(popularMovies2);
                moviesAdapter = new PopularMoviesAdapter(getActivity(), movieList);
                gridView.setAdapter(moviesAdapter);
            }
        }
    }
}
