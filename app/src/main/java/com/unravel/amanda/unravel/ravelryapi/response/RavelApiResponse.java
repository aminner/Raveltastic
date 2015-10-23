package com.unravel.amanda.unravel.ravelryapi.response;

import com.unravel.amanda.unravel.ravelryapi.models.Paginator;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Amanda on 8/23/2015.
 */
public class RavelApiResponse implements Serializable{
    public Paginator paginator;
    public List<Object> responses;

    public RavelApiResponse(Paginator page, List<Object> responses) {
        this.paginator = page;
        this.responses = responses;
    }
}
