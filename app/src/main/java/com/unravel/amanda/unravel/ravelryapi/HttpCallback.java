package com.unravel.amanda.unravel.ravelryapi;

import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

/**
 * Created by Amanda on 8/23/2015.
 */
public interface HttpCallback {
    void onSuccess(RavelApiResponse jsonString);
    void onFailure(Exception exception);
}
