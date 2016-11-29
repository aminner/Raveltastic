package com.unravel.amanda.unravel.ravelryapi;

import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface AuthenticatedRavelApiService {
    @GET("patterns/search.json")
    Observable<RavelApiResponse> findPatterns(@Query("query") String patternId);

    @GET("patterns/{patternId}.json")
    Observable<RavelApiResponse> getPattern(@Path("patternId") String patternId);
}