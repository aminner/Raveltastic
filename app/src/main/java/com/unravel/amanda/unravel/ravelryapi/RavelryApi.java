package com.unravel.amanda.unravel.ravelryapi;

import android.util.Log;

import com.unravel.amanda.unravel.RavelApplication;
import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RavelryApi  {
    private static final String TAG = "RavelryApi";

    @Inject RavelApiService _apiService;

    public RavelryApi()   {
        RavelApplication.ravelApplication.getComponent().inject(this);
    }

    public void processRequest(RavelryApiRequest request, HttpCallback callback) {
        switch(request.requestCommand) {
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
            case LOGIN:
                _apiService.login(request.requestParameters.get(0), request.requestParameters.get(1)) .observeOn(AndroidSchedulers.mainThread())
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
                        });;
        }
    }
}
