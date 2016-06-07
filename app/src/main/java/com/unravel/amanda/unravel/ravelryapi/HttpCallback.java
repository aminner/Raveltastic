package com.unravel.amanda.unravel.ravelryapi;

import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

public interface HttpCallback {
    void onSuccess(RavelApiResponse jsonString);
    void onFailure(Throwable exception);
}
