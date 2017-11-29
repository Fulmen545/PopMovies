package com.riso.android.popmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       if (isOnline()) {
           FrameLayout frame = new FrameLayout(this);
           frame.setId(android.R.id.content);
           setContentView(frame, new FrameLayout.LayoutParams(
                   FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

           if (savedInstanceState == null) {
            MainActivityFragment newFragment = new MainActivityFragment();
//               MainActivityFragmentPopularity newFragment = new MainActivityFragmentPopularity();
               FragmentManager fragmentManager = getSupportFragmentManager();
               android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
               ft.add(android.R.id.content, newFragment).commit();
           }
       }else {
           internetDialog();
       }


    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        MainActivityFragment maf = new MainActivityFragment();
        MainActivityFragmentPopularity map = new MainActivityFragmentPopularity();
        if (id == R.id.order_by_vote) {
            if (isOnline()) {
                changeTo(maf, android.R.id.content);
            } else {
                internetDialog();
            }
        } else if (id == R.id.order_by_popularity){
            if (isOnline()){
                changeTo(map, android.R.id.content);
            } else {
                internetDialog();
            }
        } else if (id == R.id.order_by_favorite){
            if (isOnline()){
//                changeTo(map, android.R.id.content);
            } else {
                internetDialog();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void changeTo(Fragment fragment, int containerViewId){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(containerViewId, fragment).commit();
    }

    private void internetDialog(){
        Log.w(TAG,"Something went wrong");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please check your internet connection")
                .setTitle("No internet connection")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}
