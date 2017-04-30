package com.cta.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cta.pojo.Backdrops;
import com.cta.pojo.ImagesResponsePojo;
import com.cta.pojo.MovieDetailsPojo;
import com.cta.utility.CirclePageIndicator;
import com.cta.utility.NetworkCallback;
import com.cta.utility.OKHttpRequest;
import com.cta.utility.UrlConstant;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ankur on 30/4/17.
 *<h2>MovieDetail</h2>
 * <p>In this activity will receive movie id from last activity
 * and call two services one for image pager and one for
 * fetch movie detail.</p>
 */

public class MovieDetail extends AppCompatActivity {
    private String movieId= "";
    private ProgressDialog pDialog;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private final String TAG = MovieDetail.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
        //enabling back function
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recieveBundle();
        initialization();
        getMovieDetails();
        getPagerItems();
    }

    /**
     *<p>receive bundle from last activity
     * movieId in string format<p/>
     */
    private void recieveBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            movieId = bundle.getString("id");
        }
    }

    /**
     * <p>initializing view of activity</p>
     */
    private void initialization() {
        pDialog = new ProgressDialog(MovieDetail.this);
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);

    }
    /**
    * <p>service calling  for get movie details<p/>
    */

    private void getMovieDetails() {
        pDialog.show();
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("api_key",getString(R.string.api_key));
        } catch (JSONException e) {
            e.printStackTrace();
            pDialog.dismiss();
            Snackbar.make(findViewById(R.id.toolbar),getString(R.string.connection_fail),Snackbar.LENGTH_LONG).show();
        }
        OKHttpRequest.doOkHttpConnection(UrlConstant.MOVIE_DETAIL + movieId, OKHttpRequest.RequestType.GET, reqObject, new NetworkCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG,result);
                //on success will parse the result
                if (result!=null) {
                    MovieDetailsPojo detailsPojo = new Gson().fromJson(result,MovieDetailsPojo.class);
                    setLayout(detailsPojo);

                }else {
                    Snackbar.make(findViewById(R.id.toolbar),getString(R.string.connection_fail),Snackbar.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                //on error will get error msg in snackbar
                Snackbar.make(findViewById(R.id.toolbar),error,Snackbar.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        });
    }

    /**
     * <p>This message will set movie title and overview and ratting</p>
     * @param detailsPojo that have all movie details
     */
    private void setLayout(MovieDetailsPojo detailsPojo) {
        TextView title = (TextView) findViewById(R.id.movie_deatil_title_tv);
        title.setText(detailsPojo.getOriginal_title());
        TextView description = (TextView) findViewById(R.id.movie_detail_descreption_tv);
        description.setText(detailsPojo.getOverview());
        RatingBar ratting = (RatingBar) findViewById(R.id.movie_detail_rating);
        float ratePoint = Float.parseFloat(detailsPojo.getVote_average());
        ratting.setRating(ratePoint);
    }

    /**
     * <p>get list of images via service call this list
     * may have 1 or more then one items in list according
     * to list it will show image view pager</p>
     */
    private void getPagerItems() {
        pDialog.show();
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("api_key",getString(R.string.api_key));
        } catch (JSONException e) {
            e.printStackTrace();
            pDialog.dismiss();
            Snackbar.make(findViewById(R.id.toolbar),getString(R.string.connection_fail),Snackbar.LENGTH_LONG).show();
        }
        OKHttpRequest.doOkHttpConnection(UrlConstant.MOVIE_DETAIL + movieId+"/images", OKHttpRequest.RequestType.GET, reqObject, new NetworkCallback() {
//            onSuccess image pager will visible
            @Override
            public void onSuccess(String result) {
                Log.d(TAG,result);

                if (result!=null) {
                    ImagesResponsePojo imagePojo = new Gson().fromJson(result,ImagesResponsePojo.class);
                    if (imagePojo.getBackdrops().size()>0){
                        setImageViewPager(imagePojo.getBackdrops());
                    }


                }else {
                    Snackbar.make(findViewById(R.id.toolbar),getString(R.string.connection_fail),Snackbar.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }
//          on error will show error message
            @Override
            public void onError(String error) {
                Snackbar.make(findViewById(R.id.toolbar),error,Snackbar.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        });
    }

    /**
     * <p>
     *     view page will show up most 5 images
     *     and this view pager will change image from the list
     *     after 3 seconds
     * </p>
     * @param list of images receive from service response
     */
    private void setImageViewPager(ArrayList<Backdrops> list) {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.movie_deatil_vp);
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.movie_deatil_ci);
        ArrayList<String> imageList = new ArrayList<>();
        if (list.size()<5)
        {
            for (int index = 0; index<list.size();index++) {
                imageList.add(UrlConstant.IMAGE_PATH+list.get(index).getFile_path());
            }
        }
        else {
            for (int index = 0; index<5;index++) {
                imageList.add(UrlConstant.IMAGE_PATH+list.get(index).getFile_path());
            }
        }
        MovieDetailsPagerAdapter pagerAdapter = new MovieDetailsPagerAdapter(MovieDetail.this,imageList);
        viewPager.setAdapter(pagerAdapter);
        circlePageIndicator.setViewPager(viewPager);
        NUM_PAGES =imageList.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

    }
    //back button open last activity
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
