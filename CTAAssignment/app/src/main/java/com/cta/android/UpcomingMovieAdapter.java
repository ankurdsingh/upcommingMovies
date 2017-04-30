package com.cta.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cta.pojo.Results;
import com.cta.utility.UrlConstant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ankur on 30/4/17.
 * <h2>UpcomingMovieAdapter</h2>
 *<p>this class is use as a injector for upcoming movie list</p>
 */

class UpcomingMovieAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<Results> mDataList;
    UpcomingMovieAdapter(Context context) {
            this.context = context;
            //initializing list from the parent activity
            mDataList = ((UpcomingMovies)context).resultsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating the single view
        View itemView = LayoutInflater.from(context).inflate(R.layout.upcoming_movie_single_item,parent,false);
        return new UpcomingMovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UpcomingMovieViewHolder){
            UpcomingMovieViewHolder movieViewHolder = (UpcomingMovieViewHolder) holder;
            String imageUrl = UrlConstant.IMAGE_PATH+mDataList.get(position).getPoster_path();
            //checking url valid or not
            if (Patterns.WEB_URL.matcher(imageUrl).matches()){
                Picasso.with(context).load(imageUrl).placeholder(R.mipmap.ic_launcher)
                        .into(movieViewHolder.moviePoster);
            }
            //se movie name, release date and maturity
            movieViewHolder.movieName.setText(mDataList.get(position).getOriginal_title());
            String releaseDateStr = context.getString(R.string.release_date)+mDataList.get(position).getRelease_date();
            movieViewHolder.releaseDate.setText(releaseDateStr);
            String maturityStr = context.getString(R.string.maturity);
            if (mDataList.get(position).getAdult()){
                maturityStr = maturityStr + context.getString(R.string.adult);
            }
            else {
                maturityStr = maturityStr + context.getString(R.string.everyone);
            }
            movieViewHolder.maturity.setText(maturityStr);
            //set on click and forward to the next activity send movie id
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,MovieDetail.class);
                    intent.putExtra("id",mDataList.get(holder.getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

}
