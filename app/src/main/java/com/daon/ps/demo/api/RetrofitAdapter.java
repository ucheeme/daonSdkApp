package com.daon.ps.demo.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {

    private static final long TIME_OUT = 120;
    private static Retrofit mRetrofit;
    private static Gson mGson;


    public static Retrofit getBasicAuthInstance(String url, String user, String password ){
        Retrofit retrofit;
        Gson gson;
        String mCredentials;

        mCredentials = Credentials.basic(user, password);
        gson = new GsonBuilder().create();


        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    requestBuilder.header("Content-Type", "application/json");
                    requestBuilder.header("Accept", "application/json");
                    requestBuilder.header("Authorization", mCredentials).build();

                    return chain.proceed(requestBuilder.build());
                }).build();



        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }

}
