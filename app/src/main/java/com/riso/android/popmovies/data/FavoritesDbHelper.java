package com.riso.android.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by richard.janitor on 29-Nov-17.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 1;

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + FavoriteMovies.FavoriteEntry.TABLE_NAME + " (" +
                FavoriteMovies.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteMovies.FavoriteEntry.MOVIE_ID + " TEXT NOT NULL, " +
                FavoriteMovies.FavoriteEntry.TITLE + " TEXT NOT NULL, " +
                FavoriteMovies.FavoriteEntry.POSTER + " TEXT NOT NULL, " +
                FavoriteMovies.FavoriteEntry.PLOT + " TEXT NOT NULL, " +
                FavoriteMovies.FavoriteEntry.RATING + " TEXT NOT NULL, " +
                FavoriteMovies.FavoriteEntry.RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMovies.FavoriteEntry.POPULARITY + " TEXT NOT NULL " +
                "); ";

        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
