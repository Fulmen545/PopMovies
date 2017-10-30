package com.riso.android.popmovies;

import android.support.annotation.NonNull;

/**
 * Created by richard.janitor on 01-Oct-17.
 */

public class PopularMoviesPopularity implements Comparable<PopularMoviesPopularity>{

    String title;
    String poster;
    String plot;
    Double rating;
    String releaseDate;
    Double popularity;

    public PopularMoviesPopularity(String title, String poster, String plot,
                                   Double rating, String releaseDate, Double popularity){
        this.title=title;
        this.poster=poster;
        this.plot=plot;
        this.rating=rating;
        this.releaseDate=releaseDate;
        this.popularity=popularity;
    }

    public Double getPopularity(){
        return popularity;
    }

    @Override
    public int compareTo(@NonNull PopularMoviesPopularity o) {
        return -popularity.compareTo(o.popularity);
    }
}
