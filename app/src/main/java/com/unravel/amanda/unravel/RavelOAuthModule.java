package com.unravel.amanda.unravel;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

@Module
public class RavelOAuthModule {
    static final String BASE_URL = "https://api.ravelry.com/";
    OAuth1AccessToken _token;
    public RavelOAuthModule(OAuth1AccessToken token) {
        _token = token;
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(RavelryApiKeys keys) {
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(keys.ACCESS_KEY, keys.ACCESS_SECRET);
        consumer.setTokenWithSecret(_token.getToken(), _token.getTokenSecret());
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new SigningInterceptor(consumer));
        return httpClient.build();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }
}
