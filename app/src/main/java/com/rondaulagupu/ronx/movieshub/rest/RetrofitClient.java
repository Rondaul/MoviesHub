package com.rondaulagupu.ronx.movieshub.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit object initialization for api calls
 */
public class RetrofitClient {
    //BASE URL
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    //static Retrofit object
    private static Retrofit sRetrofit = null;

    //singleton pattern to get the Retrofit object
    public static Retrofit getRetrofitClient() {
        if(sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }
}
