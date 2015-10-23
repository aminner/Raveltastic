package com.unravel.amanda.unravel.ravelryapi;

/**
 * Created by Amanda on 10/23/2015.
 */
public class RavelryApiRequest {
    public final String queryString;
    public final String requestCommand;

    public RavelryApiRequest(String queryString, String requestCommand)
    {
        this.queryString = queryString;
        this.requestCommand = requestCommand;
    }
}
