package com.unravel.amanda.unravel.ravelryapi;

import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Amanda on 6/2/2016.
 */
public interface RavelApiService {
    @GET("Patterns/{patternId}")
    Observable<RavelApiResponse> getPatterns(@Path("patternId") String patternId);
}