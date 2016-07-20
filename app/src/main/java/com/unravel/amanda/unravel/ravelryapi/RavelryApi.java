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
        RavelApplication.getComponent().inject(this);
    }

    public void processRequest(RavelryApiRequest request, HttpCallback callback) {
        _apiService.getPatterns(request.queryString)
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
                        Log.d(TAG, e.getLocalizedMessage() + " - " + e.getCause());
                    }

                    @Override
                    public void onNext(RavelApiResponse ravelApiResponse) {
                        Log.d(TAG, "Response: " + ravelApiResponse.responses.size());
                    }
                });
    }
}
