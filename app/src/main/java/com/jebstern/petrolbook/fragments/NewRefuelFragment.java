package com.jebstern.petrolbook.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jebstern.petrolbook.FragmentHolderActivity;
import com.jebstern.petrolbook.LoginActivity;
import com.jebstern.petrolbook.R;
import com.jebstern.petrolbook.extras.Utilities;
import com.jebstern.petrolbook.rest.CreateAccountResponse;
import com.jebstern.petrolbook.rest.CreateRefuelResponse;
import com.jebstern.petrolbook.rest.RestClient;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRefuelFragment extends Fragment {

    @BindView(R.id.btn_addNewRefuel)
    Button mBtnAddRefuel;
    @BindView(R.id.et_amount)
    EditText mEditTextAmount;
    @BindView(R.id.et_price)
    EditText mEditTextPrice;
    @BindView(R.id.rg_petrol)
    RadioGroup mRadioGroupPetrol;

    private Unbinder unbinder;
    View rootView;

    public NewRefuelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_refuel, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.btn_addNewRefuel)
    public void submit() {
        if (Utilities.isAcceptableAmount(mEditTextAmount.getText().toString())) {
            mEditTextAmount.setError(null);
            if (Utilities.isAcceptablePrice(mEditTextPrice.getText().toString())) {
                mEditTextPrice.setError(null);
                int selectedId = mRadioGroupPetrol.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) rootView.findViewById(selectedId);
                String petrolType = radioButton.getText().toString();
                createRefuel(Float.parseFloat(mEditTextAmount.getText().toString()), Float.parseFloat(mEditTextPrice.getText().toString()), petrolType);
            } else {
                mEditTextPrice.setError("Not larger than zero!");
            }
        } else {
            mEditTextAmount.setError("Not larger than zero!");
        }
    }

    public void createRefuel(float amount, float price, String petrolType) {
        Random r = new Random();
        int result = r.nextInt(100 - 10) + 10;
        String address = "SomeAddress" + result;
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        Call<CreateRefuelResponse> call = new RestClient().getApiService().createRefuel(Utilities.readUsernameFromPreferences(getActivity()), address, amount, price, petrolType);
        call.enqueue(new Callback<CreateRefuelResponse>() {
            @Override
            public void onResponse(Call<CreateRefuelResponse> call, Response<CreateRefuelResponse> response) {
                CreateRefuelResponse createRefuelResponse = response.body();
                if (createRefuelResponse.getRefuelCreated()) {
                    alertDialogBuilder.setTitle("Refuel created");
                    alertDialogBuilder.setMessage(createRefuelResponse.getMessage());
                } else {
                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder.setMessage(createRefuelResponse.getMessage());
                }
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void onFailure(Call<CreateRefuelResponse> call, Throwable t) {
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage("Internet connection problem! Please retry");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
