package com.cta.utility;

/**
 * Created by ankur on 30/4/17.
 * <h2>NetworkCallback</h2>
 * <p> This interface trigger when we call api and api
 * will give a callback</p>
 */

public interface NetworkCallback {
    /**
     * Called When Success result of JSON request
     * @param result have string value that we have to parse into class.
     */
    void onSuccess(String result);


    /**
     * Called When Error result of JSON request
     * @param error have one error message to show user what went wrong.
     */
    void onError(String error);
}
