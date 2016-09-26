package com.jebstern.petrolbook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.jebstern.petrolbook.R;
import com.jebstern.petrolbook.models.Refill;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Refill> query = realm.where(Refill.class);
        RealmResults<Refill> result = query.findAll();
        int refills = result.size();
        realm.close();

        if (refills > 0) {
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

}