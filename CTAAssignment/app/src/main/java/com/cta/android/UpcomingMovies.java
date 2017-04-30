package com.cta.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cta.pojo.Results;
import com.cta.pojo.UpcomingMovieResponsePojo;
import com.cta.utility.NetworkCallback;
import com.cta.utility.OKHttpRequest;
import com.cta.utility.UrlConstant;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ankur on 30/4/17.
 *<h2>UpcomingMovies</h2>
 * <p>In this activity will call service to get upcoming movie list
 *  and that will also show release date and adult rating.</p>
 */

public class UpcomingMovies extends AppCompatActivity {

    private final String TAG = UpcomingMovies.class.getSimpleName();
    public ArrayList<Results> resultsList;
    private UpcomingMovieAdapter movieAdapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialization();
        getUpcomingMovie();
    }

    /**
     * initializing all view present in the activity
     */
    private void initialization() {
        pDialog = new ProgressDialog(UpcomingMovies.this);
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);
        resultsList = new ArrayList<>();
        RecyclerView upcomingMovieList = (RecyclerView) findViewById(R.id.upcomming_movie_list);
        movieAdapter = new UpcomingMovieAdapter(UpcomingMovies.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(UpcomingMovies.this);
        upcomingMovieList.setLayoutManager(layoutManager);
        upcomingMovieList.setAdapter(movieAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upcoming_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            Intent intent = new Intent(UpcomingMovies.this,InformationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * <p>get all upcoming movie list and parse it to the class</p>
     */
    private void getUpcomingMovie(){
        pDialog.show();
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("api_key",getString(R.string.api_key));
        } catch (JSONException e) {
            e.printStackTrace();
            pDialog.dismiss();
            Snackbar.make(findViewById(R.id.toolbar),getString(R.string.connection_fail),Snackbar.LENGTH_LONG).show();

        }
        OKHttpRequest.doOkHttpConnection(UrlConstant.UPCOMING_MOVIE, OKHttpRequest.RequestType.GET, reqObject,new NetworkCallback() {
            //on succes will parse the result and add it to list and notify to the adapter
            @Override
            public void onSuccess(String result) {
                Log.d(TAG,result);
                if (result!=null) {
                    UpcomingMovieResponsePojo responsePojo = new Gson().fromJson(result, UpcomingMovieResponsePojo.class);
                    if (responsePojo.getResults().size()>0) {
                        int position = resultsList.size();
                        resultsList.addAll(responsePojo.getResults());
                        movieAdapter.notifyItemInserted(position);
                    }
                }
                else {
                    Snackbar.make(findViewById(R.id.toolbar),getString(R.string.connection_fail),Snackbar.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }
            //on error will show error message
            @Override
            public void onError(String error) {
                Log.d(TAG,error);
                Snackbar.make(findViewById(R.id.toolbar),error,Snackbar.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        });
    }
}