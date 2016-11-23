package com.unravel.amanda.unravel;

import java.io.IOException;

/**
 * Created by Amanda on 11/22/2016.
 */
public interface RaverlyWebViewCallback {
    void authorized(String verifier) throws IOException;
}
