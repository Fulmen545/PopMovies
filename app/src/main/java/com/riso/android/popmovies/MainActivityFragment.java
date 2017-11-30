package com.riso.android.popmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


/**
 * Created by richard.janitor on 01-Oct-17.
 */

public class MainActivityFragment extends android.support.v4.app.Fragment{
    private PopularMoviesAdapter moviesAdapter;
    private static final String TAG = "MyActivity";
    private GridView gridView;

    private PopularMovies[] popularMovies;

    @Override
    public void onPause() {
        super.onPause();
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GetMovies().execute();

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
                bundle.putString("release_date",popularMovies[position].releaseDate);
                bundle.putDouble("popularity",popularMovies[position].popularity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    private class GetMovies extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler hh = new HttpHandler();
            String movieUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN;
            URL url;
            String jsonStr = null;
            try {
                url = new URL(movieUrl);
                jsonStr = hh.makeServiceCall(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            PopularMovies movie;

            if (jsonStr!=null){
                try{
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray movies = jsonObject.getJSONArray("results");
                    popularMovies = new PopularMovies[movies.length()];
                    for (int i=0;i<movies.length();i++){
                        JSONObject m = movies.getJSONObject(i);
                        String mmovieId = m.getString("id");
                        String mtitle = m.getString("title");
                        String mposter = m.getString("poster_path");
                        String mploit = m.getString("overview");
                        Double mrating = m.getDouble("vote_average");
                        String mdate = m.getString("release_date");
                        Double mpopularity = m.getDouble("popularity");

                        movie = new PopularMovies(mmovieId, mtitle, mposter, mploit, mrating, mdate, mpopularity);
                        popularMovies[i]=movie;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            List<PopularMovies> movieList = Arrays.asList(popularMovies);
            moviesAdapter =new PopularMoviesAdapter(getActivity(), movieList);
            gridView.setAdapter(moviesAdapter);
        }
    }



}
