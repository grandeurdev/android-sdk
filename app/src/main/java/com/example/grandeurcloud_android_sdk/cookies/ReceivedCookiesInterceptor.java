package com.example.grandeurcloud_android_sdk.cookies;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.LongDef;
import androidx.preference.PreferenceManager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {
    private Context context;
    public ReceivedCookiesInterceptor(Context context) {
        this.context = context;
    }
    // AddCookiesInterceptor()
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

//        Log.d("Original Response : ", originalResponse.body().string());
//        Log.d("Received Cookie",originalResponse.headers("Set-Cookie").toString());
//        Log.d("Response Code : ", String.valueOf(originalResponse.code()));
        Response temp = originalResponse;

//        if(temp.body().string().contains("AUTH-ACCOUNT-LOGGEDOUT")){
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//            prefs.edit().remove("PREF_COOKIES").commit();
//        }

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = (HashSet<String>) PreferenceManager.
                    getDefaultSharedPreferences(context).
                    getStringSet("PREF_COOKIES", new HashSet<String>());

            for (String header : originalResponse.headers("Set-Cookie")) {
                // If that cookie is not already set then add cookie to the list
                if(cookies.toString()!=header){
                    // Cookie is added to the list
                    // Log.d("Set Cookie",header);
                    cookies.add(header);
                }
            }

            SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
            memes.putStringSet("PREF_COOKIES", cookies).apply();
            memes.commit();
        }

        return originalResponse;
    }
}