package com.example.grandeurcloud_android_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.grandeurcloud_android_sdk.handlers.Post;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;

import okhttp3.Cookie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        CookieManager cookieManager = new java.net.CookieManager();
//        CookieHandler.setDefault(cookieManager);

        // Create a default config
        JsonObject config = new JsonObject();
        config.addProperty("url","https://api.grandeur.tech");
        config.addProperty("apiKey","ck412ssij0007xr239uos8jfk");

        // Dummy login data
        JsonObject data = new JsonObject();
        data.addProperty("email","demo@demo.com");
        data.addProperty("password","demo:80");

        // New password
        JsonObject newPassword = new JsonObject();
        newPassword.addProperty("password","demo:69");

        // Initialize Post object
        Post post = new Post(config);

        // Create a request
        Call<JsonObject> login =  post.send("/auth/loginwithemail", data, this);
        Call<JsonObject> changePassword = post.send("/auth/changePassword",newPassword, this);

        Button loginBtn =  (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test(login);
            }
        });


        Button changePassword1 = (Button) findViewById(R.id.changePassword);
        changePassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test(changePassword);
            }
        });


    }
    public void test(Call<JsonObject> temp) {

        temp.clone().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.d("Response : ", response.toString());
                Log.d("Headers : ", response.headers().toString());
                JsonObject jsonBody;
                if(response.body()!=null){
                     jsonBody = new JsonParser().
                            parse(response.
                                    body().
                                    toString()
                            ).
                            getAsJsonObject();
                    Log.d("Code : ", jsonBody.get("code").getAsString());
                    Log.d("Message : ", jsonBody.get("message").getAsString());
                }
//                Log.d("Cookie",response.raw().headers().get("Set-Cookie").toString());
                List<String> a = response.raw().headers().values("Set-Cookie");
                Log.d("List of Cookies", a.toString());
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Error", t.toString());

            }
        });
    }
}
