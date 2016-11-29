package com.unravel.amanda.unravel.ravelryapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.unravel.amanda.unravel.RavelApplication;
import com.unravel.amanda.unravel.RavelryApiKeys;
import com.unravel.amanda.unravel.RaverlyWebViewCallback;
import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import java.io.IOException;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RavelryApi implements RaverlyWebViewCallback {
    private static final String TAG = "RavelryApi";

    @Inject
    UnauthenticatedRavelApiService _apiService;
    @Inject RavelryApiKeys _keys;
    private OAuth1AccessToken OAuthToken;

    private OAuth1RequestToken requestToken;
    private OAuth1AccessToken accessToken; //TODO: Store?
    private OAuth10aService service;

    public RavelryApi()   {
        RavelApplication.ravelApplication.getComponent().inject(this);
    }

    public void processRequest(RavelryApiRequest request, HttpCallback callback) {
        switch (request.requestCommand) {
            case PATTERN_SEARCH:
                _apiService.findPatterns(request.requestParameters.get(0))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(callback::onFailure)
                        .subscribe(new Subscriber<RavelApiResponse>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "subscribe completed");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.getLocalizedMessage() + " - " + e.getCause(), e);
                            }

                            @Override
                            public void onNext(RavelApiResponse ravelApiResponse) {
                                Log.d(TAG, "Response: " + ravelApiResponse.responses.size());
                                callback.onSuccess(ravelApiResponse);
                            }
                        });
                break;
            case GET_PATTERN:
                _apiService.getPattern(request.requestParameters.get(0))
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(callback::onFailure)
                        .subscribe(new Subscriber<RavelApiResponse>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "subscribe completed");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.getLocalizedMessage() + " - " + e.getCause(), e);
                            }

                            @Override
                            public void onNext(RavelApiResponse ravelApiResponse) {
                                Log.d(TAG, "Response: " + ravelApiResponse.responses.size());
                                callback.onSuccess(ravelApiResponse);
                            }
                        });
                break;
        }
    }

    public void processAuthenticatedRequest(RavelryApiRequest request, HttpCallback callback)
    {
        if(this.OAuthToken == null) {
            return; //TODO: throw error do something?
        }


    }

    public String getOAuthTokenAuthorizationUrl() throws Exception {
        SharedPreferences preference = RavelApplication.ravelApplication.getSharedPreferences("RAVEL_PREFS", Context.MODE_PRIVATE);
        if(preference.contains(RavelApiConstants.OAUTH_TOKEN)) {
            OAuthToken = new OAuth1AccessToken(preference.getString(RavelApiConstants.OAUTH_TOKEN, ""), preference.getString(RavelApiConstants.OAUTH_SECRET, ""));
            throw new Exception("Already Authorized");
        } else {
            service = new ServiceBuilder()
                    .apiKey(_keys.ACCESS_KEY)
                    .apiSecret(_keys.ACCESS_SECRET)
                    .callback("http://raveltastic")
                    .build(RavelryApiOAuth.instance());
            requestToken = service.getRequestToken();
            return service.getAuthorizationUrl(requestToken);
        }
    }
    private void setOAuthToken(OAuth1AccessToken OAuthToken) {
        this.OAuthToken = OAuthToken;
        SharedPreferences preferences = RavelApplication.ravelApplication.getSharedPreferences("RAVEL_PREFS", Context.MODE_PRIVATE);
        preferences.edit().putString(RavelApiConstants.OAUTH_TOKEN, OAuthToken.getToken()).apply();
        preferences.edit().putString(RavelApiConstants.OAUTH_SECRET, OAuthToken.getTokenSecret()).apply();
    }

    @Override
    public void authorized(String verifier) throws IOException {
        if(service == null || TextUtils.isEmpty(verifier)) {
            return; //TODO: Throw error handle
        }
        accessToken = service.getAccessToken(requestToken, verifier);
        setOAuthToken(accessToken);
    }
}
