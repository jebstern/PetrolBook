package com.jebstern.petrolbook.extras;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class FetchUserAddress implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    protected static final String TAG = "main-activity";

    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected boolean mAddressRequested;
    protected String mAddressOutput;
    private AddressResultReceiver mResultReceiver;
    protected TextView mLocationAddressTextView;
    private static final int ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST = 1;
    Activity mActivity;
    Context mContext;


    public FetchUserAddress(Activity activity, Context context) {
        mResultReceiver = new AddressResultReceiver(new Handler());
        mActivity = activity;
        mContext = context;


        // Set defaults, then update using values stored in the Bundle.
        mAddressRequested = false;
        mAddressOutput = "";


        buildGoogleApiClient();
        fetchAddress();
    }




    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                displayAddressOutput();
            }
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    public void fetchAddress() {

        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            //startIntentService();
        }
        // If GoogleApiClient isn't connected, we process the user's request by setting
        // mAddressRequested to true. Later, when GoogleApiClient connects, we launch the service to
        // fetch the address. As far as the user is concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
        mAddressRequested = true;
    }


    //  Runs when a GoogleApiClient object successfully connects.
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(mActivity, "shouldShowRequestPermissionRationale", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST);
                Toast.makeText(mActivity, "requestPermissions", Toast.LENGTH_LONG).show();
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }
            if (mLastLocation != null) {
                if (!Geocoder.isPresent()) {
                    Toast.makeText(mActivity, "no_geocoder_available", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mAddressRequested) {
                    //startIntentService();
                }
            }
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                if (!Geocoder.isPresent()) {
                    Toast.makeText(mActivity, "no_geocoder_available", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mAddressRequested) {
                    //startIntentService();
                }
            }
        }
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mLastLocation != null) {
                        if (!Geocoder.isPresent()) {
                            Toast.makeText(mActivity, "no_geocoder_available", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (mAddressRequested) {
                            startIntentService();
                        }
                    }
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }


    protected void startIntentService() {
        Toast.makeText(mActivity, "startIntentService", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(mActivity, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        mActivity.startService(intent);
    }
*/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Toast.makeText(mActivity, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode(), Toast.LENGTH_LONG).show();
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        Toast.makeText(mActivity, "Connection suspended", Toast.LENGTH_LONG).show();
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    protected void displayAddressOutput() {
        mLocationAddressTextView.setText(mAddressOutput);
    }


    protected void showToast(String text) {
        Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Display the address string or an error message sent from the intent service.
            //mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            // Show a toast message if an address was found.
           // if (resultCode == Constants.SUCCESS_RESULT) {
            //    showToast("address_found");
          //  }
            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
        }
    }
}