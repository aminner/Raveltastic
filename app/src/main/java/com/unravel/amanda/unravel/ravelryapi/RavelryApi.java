package com.unravel.amanda.unravel.ravelryapi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.unravel.amanda.unravel.ravelryapi.models.RavelApiResponse;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Created by Amanda on 8/16/2015.
 */
public class RavelryApi  {
    static String ACCESS_KEY;
    static String ACCESS_SECRET;
    static String USER_ID;
    static String USER_SECRET;
    static final String BASE_URL = "https://api.ravelry.com/";
    static final String API_PROP_FILE = "api.properties";

    final Context mContext;

    public static final String PATTERN_SEARCH = "/patterns/search.json?query=";
    private HttpCallback _httpCallback;
    private RavelApiResponse _ravelApiResponse;
    public RavelryApi(Context context)
    {
        mContext = context;
        setApiKeys();
    }

    private void setApiKeys() {
        Properties prop = new Properties();
        try {
            InputStream input = mContext.getAssets().open(API_PROP_FILE);
            if(input != null)
                prop.load(input);
            ACCESS_KEY = prop.getProperty("ACCESS_KEY");
            ACCESS_SECRET = prop.getProperty("ACCESS_SECRET");
            USER_ID = prop.getProperty("USER_ID");
            USER_SECRET = prop.getProperty("USER_SECRET");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void processRequest(String queryString, HttpCallback callback) {
        _httpCallback = callback;
        new AsyncNetworkTasks().execute(queryString);
    }

    public RavelApiResponse getApiResponse()
    {
        return _ravelApiResponse;
    }


    public Object getObject(String jsonString, Class mClass) {
        try {
            Gson gson = new Gson();
            Object object = gson.fromJson(jsonString, mClass);
            return object;
        } catch(Exception ex) {
            Log.d("RavelryApi", ex.getLocalizedMessage());
            return null;
        }
    }
    private String getBasicAuthenticationEncoding() {
        String userPassword = ACCESS_KEY + ":" + USER_SECRET;
        return new String(Base64.encodeBase64(userPassword.getBytes()));
    }

    public void setResponse(RavelApiResponse ravelApiResponse) {
        _ravelApiResponse = ravelApiResponse;
    }

    private class AsyncNetworkTasks extends AsyncTask<String, Integer, Boolean>
    {
        protected Boolean doInBackground(String... task)
        {
            try {
                URL url = new URL(BASE_URL+ PATTERN_SEARCH + task[0]);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().addHeader("Authorization", "Basic " + getBasicAuthenticationEncoding()).url(url).build();
                Response response = client.newCall(request).execute();
                _httpCallback.onSuccess((RavelApiResponse) getObject(response.body().string(), RavelApiResponse.class));
            }
            catch(Exception e) {
                e.printStackTrace();
                _httpCallback.onFailure(e);
                return false;
            }
            return true;
        }
    }
}
