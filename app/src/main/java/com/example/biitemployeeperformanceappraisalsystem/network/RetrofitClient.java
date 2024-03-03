package com.example.biitemployeeperformanceappraisalsystem.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    public Retrofit getRetrofitInstance() {
        // Create a Gson instance with custom date format
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();



        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.7/api/").client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }
}
