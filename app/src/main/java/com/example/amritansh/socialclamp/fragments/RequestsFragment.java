package com.example.amritansh.socialclamp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amritansh.socialclamp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {


    public RequestsFragment() {
    }

    public static RequestsFragment createFragment(){
        return new RequestsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        return view;
    }

}
