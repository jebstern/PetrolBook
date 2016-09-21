package com.jebstern.petrolbook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jebstern.petrolbook.R;


public class NewRefuelFragment extends Fragment {


    public NewRefuelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_refuel, container, false);

        getActivity().setTitle("New refuel");
        return rootView;
    }




}
