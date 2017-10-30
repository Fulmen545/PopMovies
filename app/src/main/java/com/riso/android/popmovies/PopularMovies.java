package com.riso.android.popmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by richard.janitor on 01-Oct-17.
 */

public class PopularMovies implements Comparable<PopularMovies>{

    String title;
    String poster;
    String plot;
    Double rating;
    String releaseDate;
    Double popularity;

    public PopularMovies(String title, String poster, String plot,
                         Double rating, String releaseDate, Double popularity){
        this.title=title;
        this.poster=poster;
        this.plot=plot;
        this.rating=rating;
        this.releaseDate=releaseDate;
        this.popularity=popularity;
    }

    public Double getRating(){
        return rating;
    }

    @Override
    public int compareTo(@NonNull PopularMovies o) {
        return -rating.compareTo(o.rating);
    }


}
