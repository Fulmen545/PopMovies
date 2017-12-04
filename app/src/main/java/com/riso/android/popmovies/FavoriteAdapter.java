package com.riso.android.popmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.riso.android.popmovies.data.FavoriteMovies;
import com.squareup.picasso.Picasso;

/**
 * Created by richard.janitor on 03-Dec-17.
 */

public class FavoriteAdapter extends CursorAdapter {
    private static final String LOG_TAG = FavoriteAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;

    public static class ViewHolder {
        public final ImageView imageView;

        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.movie_image);
        }
    }

    public FavoriteAdapter(Context context, Cursor c) {
        super(context, c);
        Log.d(LOG_TAG, "FavoriteAdapter");
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View convertView = LayoutInflater.from(context).inflate(
                    R.layout.movie_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Log.d(LOG_TAG, "In bind View");

        int imageIndex = cursor.getColumnIndex(FavoriteMovies.FavoriteEntry.POSTER);
        String image = cursor.getString(imageIndex);
        Log.i(LOG_TAG, "Image reference extracted: " + image);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w500/"+ image)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(viewHolder.imageView);

    }
}
