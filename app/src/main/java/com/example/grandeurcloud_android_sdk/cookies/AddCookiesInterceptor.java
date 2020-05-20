package com.example.grandeurcloud_android_sdk.cookies;

import android.content.Context;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This interceptor put all the Cookies in Preferences in the Request.
 * Your implementation on how to get the Preferences may ary, but this will work 99% of the time.
 */
public class AddCookiesInterceptor implements Interceptor {
    public static final String PREF_COOKIES = "PREF_COOKIES";

    private Context context;

    public AddCookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

//        // Clear all the cookies if url contains LOGOUT endpoint
      String url = chain.request().url().toString();
      String body = chain.request().body().toString();

//      Log.i("BODY", body);
      //Log.i("URL", url);

        HashSet<String> preferences = (HashSet<String>) PreferenceManager.
                getDefaultSharedPreferences(context).
                getStringSet(PREF_COOKIES, new HashSet<String>());

       // Log.i("Add Cookie", preferences.toString());
       // builder.addHeader("Cookie", preferences.toString());
        for (String cookie : preferences) {
           builder.addHeader("Cookie", cookie);

        }
        return chain.proceed(builder.build());
    }
}