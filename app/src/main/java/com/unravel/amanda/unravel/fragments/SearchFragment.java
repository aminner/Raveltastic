package com.unravel.amanda.unravel.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.unravel.amanda.unravel.PatternRVAdapter;
import com.unravel.amanda.unravel.R;
import com.unravel.amanda.unravel.RavelApplication;
import com.unravel.amanda.unravel.SearchRecyclerViewItemDecorator;
import com.unravel.amanda.unravel.ravelryapi.HttpCallback;
import com.unravel.amanda.unravel.ravelryapi.RavelryApi;
import com.unravel.amanda.unravel.ravelryapi.RavelryApiCalls;
import com.unravel.amanda.unravel.ravelryapi.RavelryApiRequest;
import com.unravel.amanda.unravel.ravelryapi.models.Pattern;
import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends Fragment {
    private final static String TAG = "SearchFragment";
    private boolean isAdvancedSearch = false;
    private PatternRVAdapter _patternListAdapter;
    private BasicSearchFragment _baseSearchFragment;
    private MenuItem _searchAdvanced;
    private MenuItem _searchBasic;
    private RavelApiResponse searchResponse;

    @Inject
    RavelryApi _api;

    @BindView(R.id.rv) RecyclerView _searchResults;
    @BindView(R.id.searchQuery) EditText _searchQuery;

    private SearchResultFragment _resultFragment;
    private AdvancedSearchFragment _advancedSearchFragment;
    private View fragmentView;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public SearchFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RavelApplication.ravelApplication.getComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_settings);
        _searchAdvanced = menu.add(R.string.search_menu_advanced);
        _searchBasic = menu.add(R.string.search_menu_basic);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if(_advancedSearchFragment == null)
            _advancedSearchFragment =  AdvancedSearchFragment.newInstance();
        if (item.getTitle().toString().equals("Advanced")) {
            getFragmentManager().beginTransaction().replace(R.id.search_content, _advancedSearchFragment).addToBackStack("Results").commit();
            isAdvancedSearch = true;
        } else if (item.getTitle().equals("Quick")) {
            getFragmentManager().beginTransaction().replace(R.id.search_content, _resultFragment).addToBackStack("Advanced").commit();
            isAdvancedSearch = false;
        }
        return false;
    }

    @OnClick(R.id.searchButton)
    public void performSearch() {
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        if (isAdvancedSearch) {
            getActivity().getFragmentManager().beginTransaction().hide(_advancedSearchFragment).commit();
        }
        _api.processRequest(new RavelryApiRequest(new String[]{_searchQuery.getText().toString()}, RavelryApiCalls.PATTERN_SEARCH), new HttpCallback() {
            @Override
            public void onSuccess(RavelApiResponse response) {
                setSearchResponse(response);
            }

            @Override
            public void onFailure(Throwable exception) {
                Log.e(TAG, "Error: " + exception.getMessage(), exception);
            }
        });
    }

    @OnClick(R.id.clearSearchButton)
    public void clearSearch() {
        _searchQuery.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, fragmentView);
        _baseSearchFragment = (BasicSearchFragment) getActivity().getFragmentManager().findFragmentById(R.id.searchDetailsFragment);
        _resultFragment = (SearchResultFragment) getActivity().getFragmentManager().findFragmentById(R.id.search_content);

        if(_searchResults != null) {
            _searchResults.setHasFixedSize(true);
            _searchResults.addItemDecoration(new SearchRecyclerViewItemDecorator(5));
            _searchResults.setLayoutManager(new LinearLayoutManager(getActivity()));
            _searchResults.setAdapter(_patternListAdapter);
        }
        return fragmentView;
    }

    public void setSearchResponse(RavelApiResponse searchResponse) {
        this.searchResponse = searchResponse;
        getActivity().runOnUiThread(() -> {
            if (_patternListAdapter == null) {
                _patternListAdapter = new PatternRVAdapter((List<Pattern>) (Object) searchResponse.responses, getActivity());
                _searchResults.setAdapter(_patternListAdapter);

            } else {
                _patternListAdapter.setItems((List<Pattern>) (Object) searchResponse.responses);
            }
            _patternListAdapter.notifyDataSetChanged();
        });
    }
}
