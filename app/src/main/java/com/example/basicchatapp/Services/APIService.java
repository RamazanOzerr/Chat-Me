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
            "Authorization:key=AAAAfkA5Cwg:APA91bEC4HtTxEisHIEPekeCx_35kiGRrhr4EwvRUu3KN9q1z8s5UTczHTXyA9yQZs4dhRlkqpXNZovh5uielTAbH1FVft8v5DLK3GRxyWKjxAsYT9y8aY5dTB2hIiOgw0CoGmWDZsRR"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendNotification(@Body JsonObject jsonObject);
}
