package com.unravel.amanda.unravel;

import android.app.Application;

import com.google.gson.Gson;
import com.unravel.amanda.unravel.fragments.AdvancedSearchFragment;
import com.unravel.amanda.unravel.fragments.SearchFragment;
import com.unravel.amanda.unravel.ravelryapi.RavelryApi;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {RavelAppModule.class})
public interface ApplicationComponent {
    void inject(RavelryApi ravelryApi);
    void inject(LaunchActivity launchActivity);
    void inject(SearchFragment searchFragment);
    void inject(AdvancedSearchFragment advancedSearchFragment);
    void inject(PatternRVAdapter patternRVAdapter);
    Application application();
    Gson gson();
    OkHttpClient okHttpClient();
    Retrofit retrofit();
}
