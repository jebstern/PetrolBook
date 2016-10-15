package com.jebstern.petrolbook.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API_Service {
    @GET("usernames/{user}/")
    Call<UsernameAvailabilityResponse> getUsernameExists(@Path("user") String username);

    @POST("usernames/{user}/")
    Call<CreateAccountResponse> createAccount(@Path("user") String username);

    @GET("refuels/{user}/")
    Call<List<RefuelResponse>> getAllRefuels(@Path("user") String username);
}
