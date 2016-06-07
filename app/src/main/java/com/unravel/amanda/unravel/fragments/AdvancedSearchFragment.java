package com.unravel.amanda.unravel.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.TextView;

import com.unravel.amanda.unravel.R;
import com.unravel.amanda.unravel.ravelryapi.HttpCallback;
import com.unravel.amanda.unravel.ravelryapi.RavelryApi;
import com.unravel.amanda.unravel.ravelryapi.RavelryApiCalls;
import com.unravel.amanda.unravel.ravelryapi.RavelryApiRequest;
import com.unravel.amanda.unravel.ravelryapi.models.ColorFamily;
import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import java.util.Arrays;
import java.util.List;

public class AdvancedSearchFragment extends Fragment {
    RavelryApi _ravelryApi;

    public static AdvancedSearchFragment newInstance() {
        AdvancedSearchFragment fragment = new AdvancedSearchFragment();
        return fragment;
    }

    public AdvancedSearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _ravelryApi = new RavelryApi(getActivity());
    }
    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View _layout =  getActivity().findViewById(R.id.color_choice_layout);
        final GridLayout _expandView = (GridLayout) getActivity().findViewById(R.id.color_selection_grid);
        final TextView _expand = (TextView) getActivity().findViewById(R.id.color_expand);
        _layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_expandView.getVisibility() == View.GONE) {
                    _expandView.setVisibility(View.VISIBLE);
                    _expand.setText("-");
                } else {
                    _expandView.setVisibility(View.GONE);
                    _expand.setText("+");
                }
            }
        });

        setColorOptions();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advanced, container, false);
    }

    private void setColorOptions() {
        _ravelryApi.processRequest(new RavelryApiRequest("", RavelryApiCalls.COLOR_FAMILIES), new HttpCallback() {
            @Override
            public void onSuccess(final RavelApiResponse jsonString) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<ColorFamily> _families = Arrays.asList((ColorFamily[]) jsonString.responses.toArray());
                        GridLayout grid = (GridLayout) getActivity().findViewById(R.id.color_selection_grid);
                        int col = 0;
                        int row = 0;
                        for (ColorFamily color : _families) {
                            if(col==2){
                                col = 0;
                                row++;
                            }
                            if(row >= grid.getRowCount()){
                                grid.setRowCount(grid.getRowCount()+1);
                            }
                            CheckBox checkBox = new CheckBox(getActivity());
                            checkBox.setText(color.name);
                            checkBox.setChecked(false);
                            checkBox.setWidth((grid.getWidth()-30)/2);
                            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                            params.setGravity(1);
                            params.columnSpec = GridLayout.spec(col);
                            params.rowSpec =GridLayout.spec(row);
                            grid.addView(checkBox, params);
                            col++;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable exception) {

            }
        });
    }
    public void onDetach() {
        super.onDetach();
    }
}
