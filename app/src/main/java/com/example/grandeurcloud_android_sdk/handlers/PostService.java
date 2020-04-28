package com.example.grandeurcloud_android_sdk.handlers;

import com.google.gson.JsonObject;

import java.net.CookieManager;
import java.util.Map;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public class PostService {

    // PostService
    public static Service Service = null;

    // Method to initialize a PostService object
    public static Service getService(String baseURL){

        // If PostService is NULL initialize it
        if(Service == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(
                            new OkHttpClient.Builder()
                                    // this line is the important one:
                                    .cookieJar(new JavaNetCookieJar(new CookieManager()))
                                    .build()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service = retrofit.create(Service.class);
        }
        return Service;
    }

    public interface Service{

        // Define default headers
        @Headers({"Content-Type: application/json;charset=UTF-8",
                "Origin: Android"
        })

        // Send a POST request to Cloud
        @POST

        // Send function definition
        Call<JsonObject> send(
                // Endpoint along with ApiKey
                @Url String fullPath,

                // All the headers i.e cookies
                @HeaderMap Map<String,String> headers,

                // Data to be send to the cloud
                @Body JsonObject data
        );
    }
}
