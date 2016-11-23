package com.unravel.amanda.unravel.ravelryapi;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

/**
 * Created by Amanda on 11/22/2016.
 */

public class RavelryApiOAuth extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://www.ravelry.com/oauth/authorize?oauth_token=%s";

    protected RavelryApiOAuth(){}

    public static class InstanceHolder
    {
        private static final RavelryApiOAuth INSTANCE  = new RavelryApiOAuth();
    }

    public static RavelryApiOAuth instance()
    {
        return InstanceHolder.INSTANCE;
    }
    @Override
    public String getRequestTokenEndpoint() {
        return "https://www.ravelry.com/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.ravelry.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}
