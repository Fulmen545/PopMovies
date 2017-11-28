package com.riso.android.popmovies;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {

    private static final String TAG = "DetailActivity";
    private RecyclerView mTrailerList;
    private TrailerAdapter mAdapter;
    public Toast mToast;
    private List<String> trailers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView detailTitle;
        ImageView detailImage;
        TextView detailRelease;
        TextView detailVote;
        TextView detailOverView;
        trailers.add("Trailer1");
        trailers.add("Trailer2");
        trailers.add("Trailer3");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTrailerList = (RecyclerView) findViewById(R.id.rv_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTrailerList.setLayoutManager(layoutManager);
        mAdapter = new TrailerAdapter(trailers, this);
        mTrailerList.setAdapter(mAdapter);

        Bundle getBundel = this.getIntent().getExtras();
        if (getBundel != null) {
            detailTitle = (TextView) findViewById(R.id.detail_title);
            detailTitle.setText(getBundel.getString("title"));
            detailImage = (ImageView) findViewById(R.id.detail_poster);
            Picasso.with(this).load("http://image.tmdb.org/t/p/w342//" + getBundel.getString("poster_path"))
                    .placeholder(R.drawable.noimage)
                    .into(detailImage);
            detailRelease = (TextView) findViewById(R.id.detail_releaseDate);
            detailRelease.setText(dateFormatter(getBundel.getString("release_date")));
            detailVote = (TextView) findViewById(R.id.detail_vote);
            detailVote.setText(getBundel.getString("vote_average") + "/10");
            detailOverView = (TextView) findViewById(R.id.detail_overview);
            detailOverView.setText(getBundel.getString("overview"));
        } else {
            internetDialog();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String dateFormatter(String date){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-mm-dd");
        Date startDate = null;
        try {
            startDate = sdf .parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd-mm-yyy");
        return formatter.format(startDate);
    }

    private void internetDialog(){
        Log.w(TAG,"Something went wrong");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Something went wrong")
                .setTitle("Please check your internet connection")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onListItemClick(int listItem) {
        if (mToast!=null){
            mToast.cancel();
        }
        String msq = "Trailer #" + (listItem + 1) + " was clicked";
        mToast = Toast.makeText(this, msq, Toast.LENGTH_LONG);
        mToast.show();
    }
}
