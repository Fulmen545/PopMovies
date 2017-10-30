package com.riso.android.popmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by richard.janitor on 01-Oct-17.
 */

public class PopularMoviesAdapterPopularity extends ArrayAdapter<PopularMoviesPopularity> {

    public PopularMoviesAdapterPopularity(Activity context, List<PopularMoviesPopularity> popularMoviesPopularities) {
        super(context, 0, popularMoviesPopularities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopularMoviesPopularity popularMovies = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500/"+ popularMovies.poster)
                .into(iconView);

        return convertView;

    }


}