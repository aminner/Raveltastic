package com.unravel.amanda.unravel.ravelryapi;

import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface RavelApiService {
    @GET("patterns/search.json")
    Observable<RavelApiResponse> getPatterns(@Query("query") String patternId);
}