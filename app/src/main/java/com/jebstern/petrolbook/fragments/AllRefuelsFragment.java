package com.jebstern.petrolbook.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jebstern.petrolbook.R;
import com.jebstern.petrolbook.extras.DividerItemDecoration;
import com.jebstern.petrolbook.adapters.MyRecyclerViewAdapter;
import com.jebstern.petrolbook.rest.RefuelResponse;
import com.jebstern.petrolbook.rest.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllRefuelsFragment extends Fragment {

    private RecyclerView mRecyclerViewRefuels;
    ProgressDialog mProgressDialog;

    public AllRefuelsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_refuels, container, false);

        mRecyclerViewRefuels = (RecyclerView) rootView.findViewById(R.id.rv_refuels);

        getAllRefuels();
        return rootView;
    }

    public void getAllRefuels() {

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Downloading refuels");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();

        Call<List<RefuelResponse>> call = new RestClient().getApiService().getAllRefuels("jebex");
        call.enqueue(new Callback<List<RefuelResponse>>() {
            @Override
            public void onResponse(Call<List<RefuelResponse>> call, Response<List<RefuelResponse>> response) {
                List<RefuelResponse> refuelList = response.body();
                final MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(refuelList);
                mRecyclerViewRefuels.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerViewRefuels.setHasFixedSize(true);
                mRecyclerViewRefuels.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
                mRecyclerViewRefuels.setAdapter(adapter);
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<RefuelResponse>> call, Throwable t) {
                mProgressDialog.dismiss();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Download error");
                alertDialogBuilder.setMessage("Unable to download refuels");
                alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                Log.e("onFailure", t.toString());
            }
        });
    }


}
