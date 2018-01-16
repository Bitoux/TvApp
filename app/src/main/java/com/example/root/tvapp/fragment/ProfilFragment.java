package com.example.root.tvapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.interfaces.IUserListener;
import com.example.root.tvapp.model.User;
import com.example.root.tvapp.service.APIServices;

/**
 * Created by root on 14/01/18.
 */

public class ProfilFragment extends Fragment {

    private TextView userNameView;
    private TextView userLanguageView;
    private TextView userDisplayModeView;

    public ProfilFragment(){}

    @Override
    public void onViewCreated(View view, @NonNull Bundle savedInstanceState){

        final APIServices apiServices = new APIServices(getActivity().getApplicationContext());

        this.userNameView = (TextView) view.findViewById(R.id.user_name);
        this.userLanguageView = (TextView) view.findViewById(R.id.user_language);
        this.userDisplayModeView = (TextView) view.findViewById(R.id.user_display_mode);

        apiServices.getUserAuth(new IUserListener() {
            @Override
            public void onSuccess(User user) {
                Log.d("Profile Fragment", user.toString());
                userNameView.setText(user.getUserName());
                userLanguageView.setText(user.getLanguage());
                userDisplayModeView.setText(user.getDisplayMode());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profil, container, false);
    }
}
