package com.unravel.amanda.unravel;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unravel.amanda.unravel.ravelryapi.RavelApiService;
import com.unravel.amanda.unravel.ravelryapi.RavelryApi;
import com.unravel.amanda.unravel.ravelryapi.RavelryDeserializer;
import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Amanda on 7/13/2016.
 */
@Module
public class RavelAppModule
{
    static String ACCESS_KEY;
    static String ACCESS_SECRET;
    static String USER_ID;
    static String USER_SECRET;
    static final String BASE_URL = "https://api.ravelry.com/";
    static final String API_PROP_FILE = "api.properties";

    Application mApplication;

    public RavelAppModule() {
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient)
    {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }


    @Provides
    @Singleton
    RavelApiService providesRavelApiService(Retrofit retrofit)
    {
        return retrofit.create(RavelApiService.class);
    }
    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient()
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Basic " + getBasicAuthenticationEncoding())
                    .header("Accept", "application/json")
                    .method(original.method(), original.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        return httpClient.build();
    }

    @Provides
    @Singleton
    RavelryApi provideRavelryApi()
    {
        return new RavelryApi();
    }


    @Provides
    @Singleton
    Gson providesGson()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RavelApiResponse.class, new RavelryDeserializer());
        return gsonBuilder.create();
    }

    private String getBasicAuthenticationEncoding() {
        if(ACCESS_KEY == null) {
            setApiKeys();
        }
        String userPassword = ACCESS_KEY + ":" + USER_SECRET;
        return new String(Base64.encodeBase64(userPassword.getBytes()));
    }

    private void setApiKeys() {
        Properties prop = new Properties();
        try {
            InputStream input = mApplication.getBaseContext().getAssets().open(API_PROP_FILE);
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

}
