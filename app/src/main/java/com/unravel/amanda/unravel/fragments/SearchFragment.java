package com.unravel.amanda.unravel.fragments;

import android.app.Fragment;
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
import android.widget.Button;
import android.widget.EditText;

import com.unravel.amanda.unravel.PatternRVAdapter;
import com.unravel.amanda.unravel.R;
import com.unravel.amanda.unravel.SearchRecyclerViewItemDecorator;
import com.unravel.amanda.unravel.ravelryapi.HttpCallback;
import com.unravel.amanda.unravel.ravelryapi.RavelryApi;
import com.unravel.amanda.unravel.ravelryapi.RavelryApiCalls;
import com.unravel.amanda.unravel.ravelryapi.RavelryApiRequest;
import com.unravel.amanda.unravel.ravelryapi.models.Pattern;
import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import java.util.List;

public class SearchFragment extends Fragment {
    private final static String TAG = "SearchFragment";
    private boolean isAdvancedSearch = false;
    private RecyclerView _searchResults;
    private PatternRVAdapter _patternListAdapter;
    private BasicSearchFragment _baseSearchFragment;
    private RavelryApi _api;
    private MenuItem _searchAdvanced;
    private MenuItem _searchBasic;
    private RavelApiResponse searchResponse;
    private EditText _searchQuery;
    private Button _searchButton;
    private SearchResultFragment _resultFragment;
    private AdvancedSearchFragment _advancedSearchFragment;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    public SearchFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        _api = new RavelryApi(getActivity());
        _baseSearchFragment = (BasicSearchFragment) getActivity().getFragmentManager().findFragmentById(R.id.searchDetailsFragment);
        _searchButton = (Button) getActivity().findViewById(R.id.searchButton);
        _searchQuery = (EditText) getActivity().findViewById(R.id.searchQuery);
        _resultFragment = (SearchResultFragment) getActivity().getFragmentManager().findFragmentById(R.id.search_content);
        Button _clearSearchQuery = (Button) getActivity().findViewById(R.id.clearSearchButton);
        _clearSearchQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _searchQuery.setText("");
            }
        });
        _searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if(isAdvancedSearch)
                {
                    getActivity().getFragmentManager().beginTransaction().hide(_advancedSearchFragment).commit();
                }
                RavelryApi.processRequest(new RavelryApiRequest(_searchQuery.getText().toString(), RavelryApiCalls.PATTERN_SEARCH), new HttpCallback() {
                    @Override
                    public void onSuccess(RavelApiResponse response) {
                        setSearchResponse(response);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Log.d(TAG, "Error: " + exception.getMessage());
                    }
                });
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        _searchResults = (RecyclerView)getActivity().findViewById(R.id.rv);
        _searchResults.setHasFixedSize(true);
        _searchResults.addItemDecoration(new SearchRecyclerViewItemDecorator(5));
        _searchResults.setLayoutManager(llm);
        _searchResults.setAdapter(_patternListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void setSearchResponse(RavelApiResponse searchResponse) {
        this.searchResponse = searchResponse;
        if(_patternListAdapter == null) {
            _patternListAdapter = new PatternRVAdapter((List<Pattern>)(Object)searchResponse.responses);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                _searchResults.setAdapter(_patternListAdapter);
                }
            });
        }
        else
        _patternListAdapter.setItems((List<Pattern>)(Object)searchResponse.responses);
        _patternListAdapter.notifyDataSetChanged();
    }
}
