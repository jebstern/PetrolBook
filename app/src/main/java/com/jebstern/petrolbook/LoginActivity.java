package com.jebstern.petrolbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.jebstern.petrolbook.extras.Utilities;

import com.jebstern.petrolbook.rest.CreateAccountResponse;
import com.jebstern.petrolbook.rest.RestClient;
import com.jebstern.petrolbook.rest.UsernameAvailabilityResponse;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private Button mBtnCreateAccount;
    private TextInputLayout mTextInputLayoutUsername;
    private TextInputLayout mTextInputLayoutPassword;
    private Subscription mSubscriptionUsername;
    private Subscription mSubscriptionPassword;
    private boolean mCreateAccount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Utilities.readAccountRegisteredFromPreferences(this)) {
            Intent intent = new Intent(LoginActivity.this, FragmentHolderActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_login);
            mEditTextUsername = (EditText) findViewById(R.id.et_username);
            mEditTextPassword = (EditText) findViewById(R.id.et_password);
            mTextInputLayoutUsername = (TextInputLayout) findViewById(R.id.til_username);
            mTextInputLayoutPassword = (TextInputLayout) findViewById(R.id.til_password);
            mBtnCreateAccount = (Button) findViewById(R.id.btn_createAccount);
        }
    }

    public void createAccountButtonClicked(View view) {
        mCreateAccount = true;
        checkIfUsernameAvailable();
    }

    public void usernameAvailable(boolean usernameIsAvailable) {
        if (!usernameIsAvailable) {
            setErrorTextColor(mTextInputLayoutUsername, Color.rgb(255, 0, 0));
            mTextInputLayoutUsername.setError("Username '" + mEditTextUsername.getText().toString() + "' is not available");
        } else {
            if (mCreateAccount) {
                mTextInputLayoutPassword.setErrorEnabled(false);
                if (Utilities.isAcceptableUsername(mEditTextUsername.getText().toString())) {
                    if (Utilities.isAcceptablePassword(mEditTextPassword.getText().toString())) {
                        createAccount();
                    } else {
                        mTextInputLayoutPassword.setError("Password is too short (min. 6 characters)!");
                    }
                } else {
                    setErrorTextColor(mTextInputLayoutUsername, Color.rgb(255, 0, 0));
                    mTextInputLayoutUsername.setError("Username is too short (min. 4 characters)!");
                }
            } else {
                setErrorTextColor(mTextInputLayoutUsername, Color.rgb(34, 139, 34));
                mTextInputLayoutUsername.setError("Username '" + mEditTextUsername.getText().toString() + "' is available");
            }
        }
    }


    public void setupMessage() {
        if (Utilities.isAcceptableUsername(mEditTextUsername.getText().toString())) {
            mTextInputLayoutUsername.setError("Checking availability...");
            checkIfUsernameAvailable();
        } else {
            setErrorTextColor(mTextInputLayoutUsername, Color.rgb(255, 0, 0));
            mTextInputLayoutUsername.setError("Username is too short (min. 4 characters)!");
        }
    }


    public void checkIfUsernameAvailable() {
        String username = mEditTextUsername.getText().toString();
        Call<UsernameAvailabilityResponse> call = new RestClient().getApiService().getUsernameExists(username);
        call.enqueue(new Callback<UsernameAvailabilityResponse>() {
            @Override
            public void onResponse(Call<UsernameAvailabilityResponse> call, Response<UsernameAvailabilityResponse> response) {
                UsernameAvailabilityResponse usernameAvailabilityResponse = response.body();
                if (usernameAvailabilityResponse.getUsernameExists()) {
                    usernameAvailable(false);
                } else {
                    usernameAvailable(true);
                }
            }

            @Override
            public void onFailure(Call<UsernameAvailabilityResponse> call, Throwable t) {
                Log.e("onFailure", "checkIfUsernameAvailable: "+t.toString());
            }
        });
    }


    public void createAccount() {
        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();
        Call<CreateAccountResponse> call = new RestClient().getApiService().createAccount(username,password);
        call.enqueue(new Callback<CreateAccountResponse>() {
            @Override
            public void onResponse(Call<CreateAccountResponse> call, Response<CreateAccountResponse> response) {
                CreateAccountResponse createAccountResponse = response.body();
                if (createAccountResponse.getAccountCreated()) {
                    createAccountResult(true, createAccountResponse.getMessage());
                } else {
                    createAccountResult(false, createAccountResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CreateAccountResponse> call, Throwable t) {
                Log.e("onFailure", "createAccount: "+t.toString());
            }
        });
    }

    public void createAccountResult(boolean accountCreated, String message) {
        if (accountCreated) {
            Utilities.writeUsernameIntoPreferences(this, mEditTextUsername.getText().toString());
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Account created");
            alertDialogBuilder.setMessage("Your account has been succesfully created. Press OK to continue.");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent intent = new Intent(LoginActivity.this, FragmentHolderActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            mBtnCreateAccount.setEnabled(true);
            mTextInputLayoutUsername.setError(message);
        }
    }

    private void setupSubscription() {
        mSubscriptionUsername = RxTextView.textChangeEvents(mEditTextUsername)
                .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())  // default Scheduler is Computation
                .filter(new Func1<TextViewTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(TextViewTextChangeEvent changes) {
                        String username = mEditTextUsername.getText().toString();
                        Log.e("RxResult", "call: username.isEmpty()=" + username.isEmpty());
                        return (!username.isEmpty());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(getUsernameObserver());

        mSubscriptionPassword = RxTextView.textChangeEvents(mEditTextPassword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getPasswordObserver());
    }

    public static void setErrorTextColor(TextInputLayout textInputLayout, int color) {
        try {
            Field fErrorView = TextInputLayout.class.getDeclaredField("mErrorView");
            fErrorView.setAccessible(true);
            TextView mErrorView = (TextView) fErrorView.get(textInputLayout);
            mErrorView.setTextColor(color);
            mErrorView.requestLayout();
        } catch (Exception e) {
            Log.e("asd", "setErrorTextColor error!");
        }
    }

    private Observer<TextViewTextChangeEvent> getUsernameObserver() {
        return new Observer<TextViewTextChangeEvent>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("RxResult", "onError - Dang error. check your logs");
            }

            @Override
            public void onNext(TextViewTextChangeEvent onTextChangeEvent) {
                Log.e("RxResult", "getUsernameObserver - onNext: " + onTextChangeEvent.text().toString());
                mCreateAccount = false;
                setupMessage();
            }
        };
    }


    private Observer<TextViewTextChangeEvent> getPasswordObserver() {
        return new Observer<TextViewTextChangeEvent>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("RxResult", "onError - Dang error. check your logs");
            }

            @Override
            public void onNext(TextViewTextChangeEvent onTextChangeEvent) {
                Log.e("RxResult", "getPasswordObserver - onNext: " + onTextChangeEvent.text().toString());
                if (onTextChangeEvent.text().toString().length() > 0 && onTextChangeEvent.text().toString().length() < 6) {
                    mTextInputLayoutPassword.setError("Password is too short (min. 6 characters)!");
                } else {
                    mTextInputLayoutPassword.setErrorEnabled(false);
                }

            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        mSubscriptionUsername.unsubscribe();
        mSubscriptionPassword.unsubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupSubscription();
    }

}
