package com.cta.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ankur on 30/4/17.
 * <h2>OKHttpRequest</h2>
 * <p>This class is responsible for calling apis and
 * will also give a callback to the caller activity</p>
 */

public class OKHttpRequest {

    private static final String TAG = OKHttpRequest.class.getSimpleName();

    /**
     * <h1>OkHttpRequestData</h1>
     * <p>
     * Class is use to hold three parameter i.e String object,RequestBody object and OkHttpRequestCallback
     * in a single place.
     * Because async Task takes only single parameter and I have to send three parameter so.
     * Wrapping three things into a single object and sending one object to async task.
     * </p>
     *
     * @see RequestBody
     */
    private static class OkHttpRequestData {
        String requestUrl;
        JSONObject requestBody;
        NetworkCallback callbacks;
        RequestType requestType;
    }

    /**
     * <H3>Request_type</H3>
     * <p>
     * define request Type for constant
     * </p>
     */
    public enum RequestType {
        GET("getRequest"),
        POST("postRequest");
        public String value;

        RequestType(String value) {
            this.value = value;
        }
    }

    /**
     * <h2>getUrl</h2>
     * <p>
     * creating url in by attaching params using iterator
     * </p>
     */
    private static String getUrl(String url, JSONObject jsonObject) {
        String serviceUrl = url + "?";
        String query = "";
        Iterator<String> objectKeys = jsonObject.keys();
        try {
            while (objectKeys.hasNext()) {
                String keysValue = objectKeys.next();
                query = query + keysValue + "=" + jsonObject.getString(keysValue) + "&";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Value", serviceUrl + query);

        return serviceUrl + query;
    }

    /**
     * <h2>doOkhttpRequest</h2>
     * <p>
     * This method receive all the data and Store then into to single
     * array of class
     * Service Call using okHttp Request.
     * </p>
     * <p>
     * this Method Take a Request Body and a url,and OkHttpRequestCallback and does a Asyntask,
     * and does a request to the given Url
     * </p>
     *
     * @param requestUrl contains the url of the given Service link to do performance.
     * @param requestBody contains the require data to send the given Url link.
     * @param callbacks   contains the reference to set the call back response to the calling class.
     */
    public static void doOkHttpConnection(String requestUrl, RequestType requestType, JSONObject requestBody, NetworkCallback callbacks) {
        OkHttpRequestData data = new OkHttpRequestData();
        data.requestUrl = requestUrl;
        data.requestBody = requestBody;
        data.callbacks = callbacks;
        data.requestType = requestType;
        /**
         * Calling the Async task to perform the Service call.*/
        new OkHttpRequest().execute(data);
    }

    /**
     * <h1>OkHttpRequest</h1>
     * OkHttpRequest extends async task to perform the function indecently .
     * Does a service call using OkHttp client.
     * <p>
     * This class extends async task and override the method of async task .
     * on doInBackground method of async task.
     * performing a service call to th given url and sending data given to the class.
     * By the help of the OkHttpClient and sending the call back method to the calling Activity by setting
     * data to the given reference of call-Back Interface object.
     * </P>
     * If Any thing Happened to the service call like Connection Failed or any thin else.
     * Telling to the User that connection is too slow when handling Exception.
     *
     * @see Response
     * @see OkHttpClient
     */
    private static class OkHttpRequest extends AsyncTask<OkHttpRequestData, Void, String> {
        NetworkCallback callbacks;
        boolean error = false;
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(OkHttpRequestData... params) {
            callbacks = params[0].callbacks;
            String result = "";
            try {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(30, TimeUnit.SECONDS);
                builder.readTimeout(30, TimeUnit.SECONDS);
                builder.writeTimeout(30, TimeUnit.SECONDS);
                OkHttpClient httpClient = builder.build();

                Request request = null;
                if (params[0].requestType.equals(RequestType.GET)) {
                    String url = getUrl(params[0].requestUrl, params[0].requestBody);
                    request = new Request.Builder()
                            .url(url)
                            .build();
                } else if (params[0].requestType.equals(RequestType.POST)) {
                    RequestBody body = RequestBody.create(JSON, params[0].requestBody.toString());
                    request = new Request.Builder()
                            .url(params[0].requestUrl)
                            .header("Content-Type", "text/json; Charset=UTF-8")
                            .post(body)
                            .build();
                }
                Response response = httpClient.newCall(request).execute();
                result = response.body().string();
            } catch (UnsupportedEncodingException e) {

                error = true;
                Log.d(TAG, "UnsupportedEncodingException" + e.toString());
                result = "Connection Failed..Retry !";
                e.printStackTrace();
            } catch (IOException e) {

                error = true;
                Log.d(TAG, "Read IO exception" + e.toString());
                result = "Connection is too slow...Retry!";
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!error) {
                callbacks.onSuccess(result);
            } else {
                callbacks.onError(result);
            }
        }
    }
}