// This is the main
// Apollo class and it is
// used to initialize
// your project.

package com.example.grandeurcloud_android_sdk;

import android.content.Context;

import com.example.grandeurcloud_android_sdk.apolloHandlers.Auth;
import com.example.grandeurcloud_android_sdk.handlers.Handlers;
import com.example.grandeurcloud_android_sdk.handlers.Post;
import com.google.gson.JsonObject;

public class Apollo {

    // Create a default config
    private JsonObject config = new JsonObject();

    // Creates Post Object
    private Post post = null;

    // Creates Handlers Object
    private Handlers handlers = null;

    // Creates an Auth object
    Auth auth = null;

    // Context
    private Context context = null;

    Apollo(Context context){
        // Set default config
        this.config.addProperty("url","https://api.grandeur.tech");
        this.config.addProperty("node","wss://api.grandeur.tech");

        // Set context
        this.context = context;
    }

    // Function that initializes
    // the Apollo object
    public void init(String apiKey){
        this.config.addProperty("apiKey",apiKey);

        // Post Handler
        this.post = new Post(this.config);

        // Handlers
        this.handlers = new Handlers(this.post);

        // Initialize default auth
        this.auth = new Auth(handlers,this.context);

    }
}
