package com.riso.android.popmovies;

import android.support.annotation.NonNull;

/**
 * Created by richard.janitor on 01-Oct-17.
 */

class PopularMovies implements Comparable<PopularMovies>{

    final String movieId;
    final String title;
    final String poster;
    final String plot;
    final Double rating;
    final String releaseDate;
    final Double popularity;

    public PopularMovies(String movieId, String title, String poster, String plot,
                         Double rating, String releaseDate, Double popularity){
        this.movieId=movieId;
        this.title=title;
        this.poster=poster;
        this.plot=plot;
        this.rating=rating;
        this.releaseDate=releaseDate;
        this.popularity=popularity;
    }

    @Override
    public int compareTo(@NonNull PopularMovies o) {
        return -rating.compareTo(o.rating);
    }


}
