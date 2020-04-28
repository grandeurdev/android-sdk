// This is a handler class
// and it is used to send request
// to the server. Now you do not
// have to have a post function
// in every class.
package com.example.grandeurcloud_android_sdk.handlers;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

// Class definition
public class Post {

    // Configuration
    JsonObject config = null;

    // Constructor
    public Post(JsonObject config){
        // Default Configuration
        this.config = config;
    }

    // Default function for sending requests
    // to the server.
    public Call<JsonObject> send(String path, JsonObject data){

        Log.d("Data", data.toString());

        // Cookie headers are to be set here
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("Content-Type","application/json;charset=UTF-8");
        headers.put("Origin","Android");

        // Create full path with ApiKey
        final String fullPath = path + "?apiKey=" + this.config.get("apiKey").getAsString();

        // Initialize Post Service
        Call<JsonObject> postService = PostService.
                                       getService(this.config.get("url").getAsString()).
                                       send(fullPath,headers,data);
        return postService;
    }
}
