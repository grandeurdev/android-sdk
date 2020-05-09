package com.example.grandeurcloud_android_sdk.handlers;

import android.content.Context;
import com.example.grandeurcloud_android_sdk.cookies.AddCookiesInterceptor;
import com.example.grandeurcloud_android_sdk.cookies.ReceivedCookiesInterceptor;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;

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

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AddCookiesInterceptor(context));
        httpClient.addInterceptor(new ReceivedCookiesInterceptor(context));

//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Interceptor.Chain chain) throws IOException {
//                Request original = chain.request();
//
//                // Request customization: add request headers
//                Request.Builder requestBuilder = original.newBuilder()
//                        .addHeader("Content-Type", "application/json;charset=UTF-8")
//                        .addHeader("Origin", "Android");
//
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        });

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
