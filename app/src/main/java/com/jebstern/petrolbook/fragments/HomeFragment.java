package com.jebstern.petrolbook.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.jebstern.petrolbook.R;


public class HomeFragment extends Fragment {

    ProgressDialog mProgressDialog;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;

        if (getNewestRefuel()) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_home_first_time, container, false);
            Button buttonnewRefuel = (Button) rootView.findViewById(R.id.btn_newRefuel);
            buttonnewRefuel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.container, new NewRefuelFragment()).commit();
                }
            });
        }

        return rootView;
    }


    public boolean getNewestRefuel() {
        /*
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Getting data");
        mProgressDialog.setMessage("Checking latest refuel...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();*/
        return false;
    }


}