package com.riso.android.popmovies;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView detailTitle;
        ImageView detailImage;
        TextView detailRelease;
        TextView detailVote;
        TextView detailOverView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle getBundel = this.getIntent().getExtras();
        if (getBundel != null) {
            detailTitle = (TextView) findViewById(R.id.detail_title);
            detailTitle.setText(getBundel.getString("title"));
            detailImage = (ImageView) findViewById(R.id.detail_poster);
            Picasso.with(this).load("http://image.tmdb.org/t/p/w342//" + getBundel.getString("poster_path"))
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


}
