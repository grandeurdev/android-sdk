// This is a handler class
// and it is used to send request
// to the server. Now you do not
// have to have a post function
// in every class.

package com.example.grandeurcloud_android_sdk.handlers;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;

// Class definition
public class Post {

    private static final String PREF_COOKIES ="PREF_COOKIES" ;

    // Configuration
    JsonObject config = null;
    JsonObject jsonBody = null;

    // Constructor
    public Post(JsonObject config) {
        // Default Configuration
        this.config = config;
    }

    // Default function for sending requests
    // to the server.
    public Call<JsonObject> send(String path, JsonObject data, Context context) {

        jsonBody = null;

        // Data object
        Log.i("Data", data.toString());

        // Create full path with ApiKey
        final String fullPath = path + "?apiKey=" + this.config.get("apiKey").getAsString();

        // Initialize Post Service
        Call<JsonObject> postService = PostService.
                getService(this.config.get("url").getAsString(), context).
                send(
                        // End point along with Api Key
                        fullPath,
                        // Json Object of data to send
                        data
                );

        // Return Post Service Object
        return postService;
    }
}
