package com.cta.android;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ankur on 30/4/17.
 * <h2>UpcomingMovieViewHolder</h2>
 *<p>view holder  for single layout of the list item of upcoming movie</p>
 */

public class UpcomingMovieViewHolder extends RecyclerView.ViewHolder {
    TextView movieName,releaseDate,maturity;
    ImageView moviePoster;
    public UpcomingMovieViewHolder(View itemView) {
        super(itemView);
        moviePoster = (ImageView) itemView.findViewById(R.id.movie_logo_iv);
        movieName = (TextView) itemView.findViewById(R.id.movie_name_tv);
        releaseDate = (TextView) itemView.findViewById(R.id.movie_release_tv);
        maturity = (TextView) itemView.findViewById(R.id.movie_maturity_tv);
    }
}
