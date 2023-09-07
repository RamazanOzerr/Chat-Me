package com.example.basicchatapp.Services;

import android.util.JsonToken;

import com.example.basicchatapp.Notifications.FCMResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json", // you should add your own server key here
            "Authorization:key="
    })
    @POST("fcm/send")
    Call<FCMResponse> sendNotification(@Body JsonObject jsonObject);
}
