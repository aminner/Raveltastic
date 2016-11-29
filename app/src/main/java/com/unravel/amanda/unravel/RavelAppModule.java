package com.unravel.amanda.unravel;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unravel.amanda.unravel.ravelryapi.UnauthenticatedRavelApiService;
import com.unravel.amanda.unravel.ravelryapi.RavelryApi;
import com.unravel.amanda.unravel.ravelryapi.RavelryDeserializer;
import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RavelAppModule
{
    static final String BASE_URL = "https://api.ravelry.com/";
    static final String AUTH_BASE_URL = "http://www.ravelry.com/";
    static final String API_PROP_FILE = "api.properties";

    RavelApplication mApplication;

    public RavelAppModule(RavelApplication ravelApplication) {
        mApplication = ravelApplication;
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
    UnauthenticatedRavelApiService providesRavelApiService(Retrofit retrofit)
    {
        return retrofit.create(UnauthenticatedRavelApiService.class);
    }

    @Provides
    @Singleton
    RavelryApiKeys provideRavelApiKeys()
    {
        RavelryApiKeys keys = new RavelryApiKeys(mApplication, API_PROP_FILE);
        return keys;
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(RavelryApiKeys keys) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Basic " + keys.getBasicAuthenticationEncoding())
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
    Gson providesGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RavelApiResponse.class, new RavelryDeserializer());
        return gsonBuilder.create();
    }
}
