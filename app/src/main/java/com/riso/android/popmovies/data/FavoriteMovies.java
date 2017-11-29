package com.riso.android.popmovies.data;

import android.provider.BaseColumns;

/**
 * Created by richard.janitor on 29-Nov-17.
 */

public class FavoriteMovies {

    public static final class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String MOVIE_ID = "movieid";
        public static final String TITLE = "title";
        public static final String POSTER = "poster";
        public static final String PLOT = "plot";
        public static final String RATING = "rating";
        public static final String RELEASE_DATE = "date";
        public static final String POPULARITY = "popularity";
    }
}
