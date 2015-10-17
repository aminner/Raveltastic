package com.unravel.amanda.unravel.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.unravel.amanda.unravel.PatternListAdapter;
import com.unravel.amanda.unravel.R;
import com.unravel.amanda.unravel.ravelryapi.HttpCallback;
import com.unravel.amanda.unravel.ravelryapi.RavelryApi;
import com.unravel.amanda.unravel.ravelryapi.models.RavelApiResponse;

public class SearchResultsFragment extends Fragment {
    private final static String TAG = "SearchResultsFragment";
    private ListView _searchResults;
    private PatternListAdapter _patternListAdapter;
    private BasicSearchFragment _baseSearchFragment;
    private RavelryApi _api;
    private MenuItem _searchAdvanced;
    private MenuItem _searchBasic;
    private RavelApiResponse searchResponse;
    private EditText _searchQuery;
    private Button _searchButton;

    public static SearchResultsFragment newInstance() {
        SearchResultsFragment fragment = new SearchResultsFragment();
        return fragment;
    }

    public SearchResultsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_settings);
        _searchAdvanced = menu.add("Advanced");
        _searchBasic = menu.add("Quick");
    }
    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        _api = new RavelryApi(getActivity());
        _baseSearchFragment = (BasicSearchFragment) getActivity().getFragmentManager().findFragmentById(R.id.searchDetailsFragment);
        _searchResults = (ListView) getActivity().findViewById(R.id.searchResults);
        _searchButton = (Button) getActivity().findViewById(R.id.searchButton);
        _searchQuery = (EditText) getActivity().findViewById(R.id.searchQuery);
        _searchResults.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(_patternListAdapter._selectedViews.contains(view)){
                    _patternListAdapter._selectedViews.remove(view);
                    view.setBackgroundColor(getActivity().getResources().getColor(R.color.background_material_light));
                }
                else
                {
                    _patternListAdapter._selectedViews.add((RelativeLayout) view);
                    view.setBackgroundColor(getActivity().getResources().getColor(R.color.shadow_start_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                _api.processRequest(_searchQuery.getText().toString(), new HttpCallback() {
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
            _patternListAdapter = new PatternListAdapter(getActivity(), searchResponse.patterns);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _searchResults.setAdapter(_patternListAdapter);
                }
            });
        }
        else
            _patternListAdapter.setItems(searchResponse.patterns);
        _patternListAdapter.notifyDataSetChanged();
    }
}
