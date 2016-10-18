package com.jebstern.petrolbook.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API_Service {
    @GET("usernames/{user}/")
    Call<UsernameAvailabilityResponse> getUsernameExists(@Path("user") String username);

    @FormUrlEncoded
    @POST("username/")
    Call<CreateAccountResponse> createAccount(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("refuel/")
    Call<CreateRefuelResponse> createRefuel(
            @Field("username") String username,
            @Field("address") String address,
            @Field("amount") float amount,
            @Field("price") float price,
            @Field("petrolType") String type
    );

    @GET("refuels/{user}/")
    Call<List<RefuelResponse>> getAllRefuels(@Path("user") String username);

    @GET("refuel/{user}/")
    Call<RefuelResponse> getNewestRefuel(@Path("user") String username);

}
