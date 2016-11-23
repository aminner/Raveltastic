package com.unravel.amanda.unravel;

import android.app.Application;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RavelryApiKeys {
    public String USER_SECRET;
    public String USER_ID;
    public String ACCESS_SECRET;
    public String ACCESS_KEY;

    public RavelryApiKeys(Application application, String propFile) {
        Properties prop = new Properties();
        try {
            InputStream input = application.getBaseContext().getAssets().open(propFile);
            if (input != null)
                prop.load(input);
            ACCESS_KEY = prop.getProperty("ACCESS_KEY");
            ACCESS_SECRET = prop.getProperty("ACCESS_SECRET");
            USER_ID = prop.getProperty("USER_ID");
            USER_SECRET = prop.getProperty("USER_SECRET");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBasicAuthenticationEncoding() {
        String userPassword = ACCESS_KEY + ":" + USER_SECRET;
        return new String(Base64.encodeBase64(userPassword.getBytes()));
    }
}
