package com.cta.android;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ankur on 30/4/17.
 *<h2>MovieDetailsPagerAdapter</h2>
 * <p>this adapter have list of images according to list
 * view pager will show</p>
 */

public class MovieDetailsPagerAdapter extends PagerAdapter
{
    private Context context;

    private ArrayList<String> image_array= new ArrayList<>();

    public MovieDetailsPagerAdapter(Activity context, ArrayList<String> image_array) {
        this.context = context;
        this.image_array = image_array;
    }

    @Override
    public int getCount() {
        if(image_array != null){
            return image_array.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // inflating image view at run time
        View itemView = LayoutInflater.from(context).inflate(R.layout.movie_details_pager_single, container, false);

        ImageView movieImage = (ImageView) itemView.findViewById(R.id.viewPager_movie_details);
        //setting image based on index
        Picasso.with(context)
                .load(image_array.get(position))
                .placeholder(R.mipmap.ic_launcher)
                .into(movieImage);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
