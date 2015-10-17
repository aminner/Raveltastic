package com.unravel.amanda.unravel.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unravel.amanda.unravel.R;

public class BasicSearchFragment extends Fragment {
    public static BasicSearchFragment newInstance() {
        BasicSearchFragment fragment = new BasicSearchFragment();
        return fragment;
    }

    public BasicSearchFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basic_search, container, false);
    }
    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
