package com.jebstern.petrolbook.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import com.jebstern.petrolbook.R;
import com.jebstern.petrolbook.adapters.MyRecyclerViewAdapter;
import com.jebstern.petrolbook.extras.DividerItemDecoration;
import com.jebstern.petrolbook.extras.Utilities;
import com.jebstern.petrolbook.rest.RefuelResponse;
import com.jebstern.petrolbook.rest.RestClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    ProgressDialog mProgressDialog;

    @BindView(R.id.linearlayout_no_refuels)
    LinearLayout mLinearLayoutNoRefuels;
    @BindView(R.id.linearlayout_newest_refuel)
    LinearLayout mLinearLayoutNewestRefuels;

    View rootView;

    private Unbinder unbinder;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        getNewestRefuel();
        return rootView;
    }

    public void getNewestRefuel() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Retrieving information");
        mProgressDialog.setMessage("Checking for your latest refuel...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        Call<RefuelResponse> call = new RestClient().getApiService().getNewestRefuel(Utilities.readUsernameFromPreferences(getActivity()));
        call.enqueue(new Callback<RefuelResponse>() {
            @Override
            public void onResponse(Call<RefuelResponse> call, Response<RefuelResponse> response) {
                RefuelResponse refuelResponse = response.body();
                if ("0".equalsIgnoreCase(refuelResponse.getId())) {
                    setLinearLayout(mLinearLayoutNoRefuels, mLinearLayoutNewestRefuels, null);
                } else {
                    setLinearLayout(mLinearLayoutNewestRefuels, mLinearLayoutNoRefuels, refuelResponse);
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RefuelResponse> call, Throwable t) {
                Log.e("onFailure", "getNewestRefuel()" + t.toString());
                mProgressDialog.dismiss();
            }
        });
    }

    public void setLinearLayout(LinearLayout show, LinearLayout hide, RefuelResponse refuelResponse) {
        show.setVisibility(View.VISIBLE);
        hide.setVisibility(View.INVISIBLE);
        if (refuelResponse != null) {
            TextView tvPrice = (TextView)rootView.findViewById(R.id.tv_price);
            TextView tvAmount = (TextView)rootView.findViewById(R.id.tv_amount);
            TextView tvType = (TextView)rootView.findViewById(R.id.tv_type);
            TextView tvTime = (TextView)rootView.findViewById(R.id.tv_time);
            tvPrice.setText(refuelResponse.getPrice() + "€");
            tvAmount.setText(refuelResponse.getAmount() + "L");
            tvType.setText(refuelResponse.getType());
            tvTime.setText(refuelResponse.getTime());
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}