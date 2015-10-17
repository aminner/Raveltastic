package com.unravel.amanda.unravel.ravelryapi.models;

import java.io.Serializable;

/**
 * Created by Amanda on 8/23/2015.
 */
public class RavelApiResponse implements Serializable{
    public Paginator paginator;
    public Pattern [] patterns;
}
