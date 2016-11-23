package com.unravel.amanda.unravel;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;

public class RavelryWebViewClient extends WebViewClient {
    private final RaverlyWebViewCallback _callback;
    public RavelryWebViewClient(RaverlyWebViewCallback callback)
    {
        _callback = callback;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        if(url.contains("http://raveltastic"))
        {
            try {

                Uri uri= Uri.parse(url);
                String verifier = uri.getQueryParameter("oauth_verifier");
                _callback.authorized(verifier);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        Log.d(this.getClass().getSimpleName(), "Should Override: " + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView webView, String url, Bitmap favicon) {
        Log.d(this.getClass().getSimpleName(), url);
        super.onPageStarted(webView, url, favicon);
    }
}
