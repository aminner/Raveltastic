package com.unravel.amanda.unravel.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.unravel.amanda.unravel.R;
import com.unravel.amanda.unravel.RavelApplication;
import com.unravel.amanda.unravel.RavelryWebViewClient;
import com.unravel.amanda.unravel.ravelryapi.RavelryApi;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

//public class LoginFragment extends Fragment  {
public class LoginFragment extends Fragment {
    private static final String TAG = "loginFragment";
    @BindView(R.id.ravelryWebview)
    WebView ravelryWebView;

    @Inject RavelryApi _api;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RavelApplication.ravelApplication.getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        try {
            initializeWebView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void initializeWebView() throws Exception {
        WebSettings webSettings = ravelryWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        ravelryWebView.setWebViewClient(new RavelryWebViewClient(_api));
        ravelryWebView.loadUrl(_api.getOAuthTokenAuthorizationUrl());
    }
}
