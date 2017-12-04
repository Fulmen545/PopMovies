package com.riso.android.popmovies;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.riso.android.popmovies.data.FavoriteMovies;
import com.riso.android.popmovies.data.FavoritesDbHelper;

/**
 * Created by richard.janitor on 30-Nov-17.
 */

public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private SQLiteDatabase mDb;
    private GridView gridView;
    private PopularMoviesAdapter moviesAdapter;
    private FavoriteAdapter moviesCursorAdapter;
    private PopularMovies[] popularMovies;
    private PopularMovies[] popularMovies2;
    private Cursor mCursor;
    private static final String TAG = "FavoritesFragment";
    private static final int CURSOR_LOADER_ID = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        FavoritesDbHelper dbHelper = new FavoritesDbHelper(view.getContext());

        mDb = dbHelper.getWritableDatabase();
        moviesCursorAdapter = new FavoriteAdapter(getContext(), null);

        gridView = (GridView) view.findViewById(R.id.movies_grid);
        gridView.setAdapter(moviesCursorAdapter);
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID,null, this);
        mCursor=getActivity().getContentResolver().query(FavoriteMovies.FavoriteEntry.CONTENT_URI,
                null,
                null,
                null,
                FavoriteMovies.FavoriteEntry.MOVIE_ID);
        getListData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Cursor c =
                getActivity().getContentResolver().query(FavoriteMovies.FavoriteEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        FavoriteMovies.FavoriteEntry.MOVIE_ID);
        if (c.getCount() == 0) {
            Log.i(TAG, "No data loaded");
        }
        mCursor = c;
        getListData();
        // initialize loader
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                FavoriteMovies.FavoriteEntry.CONTENT_URI,
                null,
                null,
                null,
                FavoriteMovies.FavoriteEntry.MOVIE_ID);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        moviesCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesCursorAdapter.swapCursor(null);
    }

    private void getListData() {
        PopularMovies favorite;
        int i = 0;
        if (mCursor.getCount() == 0) {
//            return popularMovies = null;
        } else {
            popularMovies = new PopularMovies[mCursor.getCount()];
            if (mCursor.moveToFirst()) {
                do {
                    String fmovieId = mCursor.getString(mCursor.getColumnIndex(FavoriteMovies.FavoriteEntry.MOVIE_ID));
                    String ftitle = mCursor.getString(mCursor.getColumnIndex(FavoriteMovies.FavoriteEntry.TITLE));
                    String fposter = mCursor.getString(mCursor.getColumnIndex(FavoriteMovies.FavoriteEntry.POSTER));
                    String fploit = mCursor.getString(mCursor.getColumnIndex(FavoriteMovies.FavoriteEntry.PLOT));
                    Double frating = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(FavoriteMovies.FavoriteEntry.RATING)));
                    String fdate = mCursor.getString(mCursor.getColumnIndex(FavoriteMovies.FavoriteEntry.RELEASE_DATE));
                    Double fpopularity = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(FavoriteMovies.FavoriteEntry.POPULARITY)));

                    favorite = new PopularMovies(fmovieId, ftitle, fposter, fploit, frating, fdate, fpopularity);
                    popularMovies[i] = favorite;
                    i++;
                }
                while (mCursor.moveToNext());
            }
        }
    }
}
