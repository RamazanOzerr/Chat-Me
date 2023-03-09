package com.example.basicchatapp.Notifications;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    public static Retrofit getClient(String url){

        return new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

//    private static OkHttpClient provideClient() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        return new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(chain -> {
//            Request request = chain.request();
//            return chain.proceed(request);
//        }).build();
//    }
}
