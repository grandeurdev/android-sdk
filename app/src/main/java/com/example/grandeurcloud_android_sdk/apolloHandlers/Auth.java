// Apollo Authentication
// This is one of the most important feature
// that we will provide with our cloud and
// it is the starting point of everything. As
// in order to be able to access the other parts
// of the SDK. It is important to first login a user
// into the Application.

package com.example.grandeurcloud_android_sdk.apolloHandlers;

import android.content.Context;
import android.util.Log;

import com.example.grandeurcloud_android_sdk.handlers.Handlers;
import com.example.grandeurcloud_android_sdk.handlers.Post;
import com.google.gson.JsonObject;

import retrofit2.Call;

public class Auth {

    Post post;
    Context context;

    JsonObject jsonBody;

    // Constructor
    public Auth(Handlers handlers, Context context){
        this.post = handlers.post;
        this.context = context;
    }


    // This function sends "login a user" request with required data to the server
    public Call<JsonObject> login(String email, String password){

        // Json Object to send to the cloud
        JsonObject data = new JsonObject();
        data.addProperty("email",email);
        data.addProperty("password",password);

        // Sends data to the server and returns a response callback
        return this.post.send("/auth/loginwithemail",data,this.context).clone();
    }

    // This function sends "register" request with provided data to the server
    // submit the request
    public Call<JsonObject> register(String email, String password, String displayName, String phone){

        // Json Object to send to the cloud
        JsonObject data = new JsonObject();
        data.addProperty("email",email);
        data.addProperty("password",password);
        data.addProperty("displayName",displayName);
        data.addProperty("phone",phone);

        // Sends data to the server and returns a response callback
        return this.post.send("/auth/register",data,this.context).clone();
    }

    // Confirmation function will get the token from the user received
    // earlier as a result of request request with user data via the argument
    // and will get code from the user via the argument and then using the post
    // handler function will submit the request again.
    public Call<JsonObject> register(String token, String verificationCode){

        // Json Object to send to the cloud
        JsonObject data = new JsonObject();
        data.addProperty("token",token);
        data.addProperty("verificationCode",verificationCode);

        // Sends data to the server and returns a response callback
        return this.post.send("/auth/register",data,this.context).clone();
    }

    // This function sends "forgotPassword" request with provided data to the server
    // submit the request
    public Call<JsonObject> forgotPassword(String email){
        // Json Object to send to the cloud
        JsonObject data = new JsonObject();
        data.addProperty("email",email);

        // Sends data to the server and returns a response callback
        return this.post.send("/auth/forgotPassword",data,this.context).clone();
    }

    // Confirmation function will get the token from the user received
    // earlier as a result of forget password request with user data via the argument
    // and will get code from the user via the argument and then using the post
    // handler function will submit the request again.
    public Call<JsonObject> forgotPassword(String token, String verificationCode, String password){

        // Json Object to send to the cloud
        JsonObject data = new JsonObject();
        data.addProperty("verificationCode",verificationCode);
        data.addProperty("token",token);
        data.addProperty("password",password);

        // Sends data to the server and returns a response callback
        return this.post.send("/auth/forgotPassword",data,this.context).clone();
    }

    // This function sends "changePassword" request with provided data to the server
    // submit the request
    public Call<JsonObject> changePassword(String password){

        // Json Object to send to the cloud
        JsonObject data = new JsonObject();
        data.addProperty("password",password);

        // Sends data to the server and returns a response callback
        return this.post.send("/auth/changePassword",data,this.context).clone();
    }

    // Confirmation function will get the token from the user received
    // earlier as a result of change password request with user data via the argument
    // and will get code from the user via the argument and then using the post
    // handler function will submit the request again.
    public Call<JsonObject> changePassword(String token, String verificationCode){

        // Json Object to send to the cloud
        JsonObject data = new JsonObject();
        data.addProperty("token",token);
        data.addProperty("verificationCode",verificationCode);

        // Sends data to the server and returns a response callback
        return this.post.send("/auth/changePassword",data,this.context).clone();
    }


    // This function sends "logout the user" request to the server
    public Call<JsonObject> logout(){

        // Sends logout request to the server and returns a response callback
        return this.post.send("/auth/logout", new JsonObject(), this.context);
    }


}
