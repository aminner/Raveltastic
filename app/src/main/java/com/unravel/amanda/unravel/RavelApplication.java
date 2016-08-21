package com.unravel.amanda.unravel;

import android.app.Application;


public class RavelApplication extends Application {
    private ApplicationComponent mComponent;
    public static RavelApplication ravelApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
        ravelApplication = this;
    }

    private void initComponent()
    {
        mComponent = DaggerApplicationComponent.builder()
                .ravelAppModule(new RavelAppModule(this))
                .build();
    }


    public ApplicationComponent getComponent() {
        if(mComponent == null)
        {
            initComponent();
        }
        return mComponent;
    }
}
