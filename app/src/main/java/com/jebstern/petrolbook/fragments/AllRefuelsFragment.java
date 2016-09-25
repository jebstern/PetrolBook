package com.jebstern.petrolbook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jebstern.petrolbook.R;
import com.jebstern.petrolbook.extras.DividerItemDecoration;
import com.jebstern.petrolbook.extras.MyRecyclerViewAdapter;
import com.jebstern.petrolbook.rest.AllRefuelsResponse;
import com.jebstern.petrolbook.rest.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllRefuelsFragment extends Fragment {

    private RecyclerView mRecyclerViewRefuels;


    public AllRefuelsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_refuels, container, false);


        mRecyclerViewRefuels = (RecyclerView) rootView.findViewById(R.id.rv_refuels);


        getActivity().setTitle("All refuels");

        getKeikat();
        return rootView;
    }

    public void getKeikat() {
        Call<List<AllRefuelsResponse>> call = new RestClient().getApiService().getAllRefuels("jebex");
        call.enqueue(new Callback<List<AllRefuelsResponse>>() {
            @Override
            public void onResponse(Call<List<AllRefuelsResponse>> call, Response<List<AllRefuelsResponse>> response) {
                List<AllRefuelsResponse> allRefuelsList = response.body();
                mRecyclerViewRefuels.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerViewRefuels.setHasFixedSize(true);
                mRecyclerViewRefuels.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
                mRecyclerViewRefuels.setAdapter(new MyRecyclerViewAdapter(allRefuelsList, getContext()));
            }
            @Override
            public void onFailure(Call<List<AllRefuelsResponse>> call, Throwable t) {
                Log.e("onFailure", t.toString());
            }
        });
    }


}
