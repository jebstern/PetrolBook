package com.jebstern.petrolbook.rest;

import com.jebstern.petrolbook.extras.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private API_Service apiService;

    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(API_Service.class);
    }

    public API_Service getApiService() {
        return apiService;
    }

}
