package com.unravel.amanda.unravel;

import android.app.Application;

import com.github.scribejava.core.model.OAuth1AccessToken;


public class RavelApplication extends Application {
    private ApplicationComponent applicationComponent;
    private OAuthComponent oAuthComponent;
    public static RavelApplication ravelApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
        ravelApplication = this;
    }

    private void initComponent()
    {
        applicationComponent = DaggerApplicationComponent.builder()
                .ravelAppModule(new RavelAppModule(this))
                .build();
    }

    private void initOAuthComponent(OAuth1AccessToken accessToken) {
        oAuthComponent = DaggerOAuthComponent.builder()
                .ravelOAuthModule(new RavelOAuthModule(accessToken))
        .build();
    }


    public ApplicationComponent getComponent() {
        if(applicationComponent == null)
        {
            initComponent();
        }
        return applicationComponent;
    }
}
