package com.unravel.amanda.unravel;

import android.app.Application;


public class RavelApplication extends Application {
    private static ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
    }

    private static void initComponent()
    {
        mComponent = DaggerApplicationComponent.builder()
                .ravelAppModule(new RavelAppModule())
                .build();
    }


    public static ApplicationComponent getComponent() {
        if(mComponent == null)
        {
            initComponent();
        }
        return mComponent;
    }
}
