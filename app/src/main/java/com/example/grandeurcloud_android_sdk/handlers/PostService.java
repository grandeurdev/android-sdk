package com.example.grandeurcloud_android_sdk.handlers;

import android.content.Context;

import com.example.grandeurcloud_android_sdk.cookies.AddCookiesInterceptor;
import com.example.grandeurcloud_android_sdk.cookies.ReceivedCookiesInterceptor;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public class PostService {

    // PostService
    public static Service Service = null;


    // Method to initialize a PostService object
    public static Service getService(String baseURL, Context context){

        // Create logging object
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // Set logging level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Create OkHttpClient builder object
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // Add Add Cookies Interceptor
        httpClient.addInterceptor(new AddCookiesInterceptor(context));

        // Add Received Cookies Interceptor
        httpClient.addInterceptor(new ReceivedCookiesInterceptor(context));

        // Add logging interceptor
        httpClient.addInterceptor(logging);

        // Create OkHttpClient object
        OkHttpClient client = httpClient.build();

        // If PostService is NULL initialize it
        if(Service == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service = retrofit.create(Service.class);
        }
        return Service;
    }

    public interface Service{

        // Define default headers
        @Headers(
                {
                        "Content-Type: application/json;charset=UTF-8",
                        "Origin: Android"
        })

        // Send a POST request to Cloud
        @POST

        // Send function definition
        Call<JsonObject> send(
                // Endpoint along with ApiKey
                @Url String fullPath,

                // Data to be send to the cloud
                @Body JsonObject data
        );
    }
}
