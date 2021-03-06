package com.example.chadyeo.updatedbakingapp.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BakingRetrofitClient {
    public static final String GITHUB_API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(GITHUB_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
