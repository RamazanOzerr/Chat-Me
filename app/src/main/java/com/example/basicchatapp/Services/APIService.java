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
            "Content-Type:application/json",
            "Authorization:key=AAAAnO4U-jo:APA91bFQkp6FP_zsFj8KK9Fdw_k66de_Ax0YV9o3IcTY8JU4nC3SoOEx_jaSYgjNSSxKdYhp98wbPHbIRHvl_KVqZ3w9YW4ngOH-rk5fBRGOrVPNHd145J2UPym0mzUU68xh7QQxKXe0"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendNotification(@Body JsonObject jsonObject);
}
