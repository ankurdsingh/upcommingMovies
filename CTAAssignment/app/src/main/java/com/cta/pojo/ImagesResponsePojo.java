package com.cta.pojo;

import java.util.ArrayList;

/**
 * Created by ankur on 30/4/17.
 */

public class ImagesResponsePojo {
    private int id;
    private ArrayList<Backdrops>backdrops;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Backdrops> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(ArrayList<Backdrops> backdrops) {
        this.backdrops = backdrops;
    }
}
