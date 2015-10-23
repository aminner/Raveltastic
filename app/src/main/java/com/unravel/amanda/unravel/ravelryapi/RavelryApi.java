package com.unravel.amanda.unravel.ravelryapi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

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
    static Gson _gson;

    final Context mContext;

    private HttpCallback _httpCallback;
    public RavelryApi(Context context)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RavelApiResponse.class, new RavelryDeserializer());
        _gson = gsonBuilder.create();
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
    public void processRequest(RavelryApiRequest request, HttpCallback callback) {
        _httpCallback = callback;
        new AsyncNetworkTasks().execute(request);
    }

    public Object getObject(String jsonString, Class mClass) {
        try {
            Object object = _gson.fromJson(jsonString, mClass);
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

    private class AsyncNetworkTasks extends AsyncTask<RavelryApiRequest, Integer, Boolean>
    {
        protected Boolean doInBackground(RavelryApiRequest... task)
        {
            try {
                URL url = new URL(BASE_URL + task[0].requestCommand + task[0].queryString);
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
