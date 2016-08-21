package com.unravel.amanda.unravel.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unravel.amanda.unravel.R;
import com.unravel.amanda.unravel.RavelApplication;
import com.unravel.amanda.unravel.ravelryapi.RavelryApi;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    @BindView(R.id.usernameInput)
    TextView tv_username;

    @BindView(R.id.passwordInput)
    TextView tv_password;

    @Inject
    RavelryApi _api;
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
        return view;
    }

    @OnClick(R.id.loginButton)
    public void login() {
        if(validateUsername() && validatePassword()) {
//            _api.login(new RavelryApiRequest(new String[]{tv_username.getText().toString(), tv_password.getText().toString()},
//                    RavelryApiCalls.LOGIN));
        }
    }

    private boolean validateUsername() {
        return tv_username != null && TextUtils.isEmpty(tv_username.getText().toString());
    }
    private boolean validatePassword() {
        return tv_password != null && TextUtils.isEmpty(tv_password.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
