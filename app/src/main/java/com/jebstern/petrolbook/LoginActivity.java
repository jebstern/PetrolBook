package com.jebstern.petrolbook;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.jebstern.petrolbook.models.User;
import com.jebstern.petrolbook.rest.CreateAccountResponse;
import com.jebstern.petrolbook.rest.RestClient;
import com.jebstern.petrolbook.rest.UsernameAvailabilityResponse;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class LoginActivity extends AppCompatActivity {

    EditText mEditTextUsername;
    private Subscription mSubscription;
    Button mBtnCreateAccount;
    private TextInputLayout mTextInputLayoutUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<User> query = realm.where(User.class);
        RealmResults<User> result = query.findAll();
        int users = result.size();
        realm.close();

        if (users > 0) {
            Intent intent = new Intent(LoginActivity.this, FragmentHolderActivity.class);
            startActivity(intent);
            finish();
        } else {
            mEditTextUsername = (EditText) findViewById(R.id.et_username);
            mTextInputLayoutUsername = (TextInputLayout) findViewById(R.id.input_layout_username);
            mBtnCreateAccount = (Button) findViewById(R.id.btn_createAccount);
            mBtnCreateAccount.setEnabled(false);
            setupSubscription();
        }

    }


    private Observer<TextViewTextChangeEvent> _getSearchObserver() {
        return new Observer<TextViewTextChangeEvent>() {
            @Override
            public void onCompleted() {
                Log.e("RxResult", "--------- onComplete");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("RxResult", "onError - Dang error. check your logs");
            }

            @Override
            public void onNext(TextViewTextChangeEvent onTextChangeEvent) {
                Log.e("RxResult", "onNext: " + onTextChangeEvent.text().toString());
                setupMessage();
            }
        };
    }


    public void createAccountButtonClicked(View view) {
        createAccount();
    }


    public void usernameAvailable(boolean usernameIsAvailable) {
        if (!usernameIsAvailable) {
            mBtnCreateAccount.setEnabled(false);
            mTextInputLayoutUsername.setError("Username '" + mEditTextUsername.getText().toString() + "' is not available");
        } else {
            mBtnCreateAccount.setEnabled(true);
            mTextInputLayoutUsername.setError("Username '" + mEditTextUsername.getText().toString() + "' is available");
        }
    }


    public void setupMessage() {
        String username = mEditTextUsername.getText().toString();
        if ("".equalsIgnoreCase(username)) {
            mTextInputLayoutUsername.setError("Username can't be empty!");
            mBtnCreateAccount.setEnabled(false);
        } else {
            mTextInputLayoutUsername.setError("Checking availability...");
            checkIfUsernameAvailable();
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
                Log.e("onFailure", t.toString());
            }
        });
    }


    public void createAccount() {
        String username = mEditTextUsername.getText().toString();
        Call<CreateAccountResponse> call = new RestClient().getApiService().createAccount(username);
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
                Log.e("onFailure", t.toString());
            }
        });
    }

    public void createAccountResult(boolean accountCreated, String message) {
        if (accountCreated) {
            Realm realm = Realm.getDefaultInstance();
            final String username = mEditTextUsername.getText().toString();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User user = realm.createObject(User.class);
                    user.setUsername(username);
                }
            });
            realm.close();
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            mBtnCreateAccount.setEnabled(true);
            mTextInputLayoutUsername.setError(message);
        }
    }

    private void setupSubscription() {
        mSubscription = RxTextView.textChangeEvents(mEditTextUsername)
                .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())// default Scheduler is Computation
                /*.filter(new Func1<TextViewTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(TextViewTextChangeEvent changes) {
                        String username = mEditTextUsername.getText().toString();
                        Log.e("RxResult", "call: username.isEmpty()=" + username.isEmpty());
                        return (!username.isEmpty());
                    }
                })*/
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(_getSearchObserver());
    }

    @Override
    public void onPause() {
        super.onPause();
        mSubscription.unsubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupSubscription();
    }

}
