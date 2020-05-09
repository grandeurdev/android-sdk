package com.example.grandeurcloud_android_sdk;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grandeurcloud_android_sdk.apolloHandlers.Auth;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //Token
    String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Context
        Context context = this;

        // Create a new Apollo project
        Apollo apolloProject = new Apollo(this);

        // Initialize Apollo project with the ApiKey
        apolloProject.init("ck412ssij0007xr239uos8jfk");

        // Get Auth functions
        Auth auth = apolloProject.auth;

        // Login button
        Button loginBtn =  (Button) findViewById(R.id.login);

        // Register button
        Button registerBtn = (Button) findViewById(R.id.register);

        // Confirm registration button
        Button confirmRegBtn = (Button) findViewById(R.id.confirmReg);

        // Logout button
        Button logoutBtn = (Button) findViewById(R.id.logout);

        // EditText for email
        EditText email = (EditText) findViewById(R.id.email);

        // EditText for password
        EditText password = (EditText) findViewById(R.id.password);

        // EditText for Display Name
        EditText displayName = (EditText) findViewById(R.id.displayName);

        // EditText for Phone
        EditText phone = (EditText) findViewById(R.id.phone);

        // EditText for code
        EditText code = (EditText) findViewById(R.id.code);



        // Login On Click listener
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get email from user
                String userEmail = email.getText().toString();

                // GEt password from user
                String userPassword = password.getText().toString();

                // Get login module from Grandeur Auth
                Call<JsonObject> login = auth.login(userEmail, userPassword);

                // Call login module with credentials
                login.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonBody;
                        if(response.body()!=null) {
                            jsonBody = new JsonParser().
                                    parse(response.
                                            body().
                                            toString()
                                    ).
                                    getAsJsonObject();
                            Log.d("Response : ", jsonBody.toString());
                            Toast.makeText(context,jsonBody.get("code").getAsString(),Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });

        // Register on click listener
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email from user
                String userEmail = email.getText().toString();

                // GEt password from user
                String userPassword = password.getText().toString();

                // Get display name from user
                String userDisplayName = displayName.getText().toString();

                // Get phone no from user
                String userPhone = phone.getText().toString();

                // Get register module from Grandeur Auth
                Call<JsonObject> register = auth.register(userEmail,userPassword,userDisplayName,userPhone);

                // Call Register module with credentials
                register.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonBody;
                        if(response.body()!=null) {
                            jsonBody = new JsonParser().
                                    parse(response.
                                            body().
                                            toString()
                                    ).
                                    getAsJsonObject();
                            token = jsonBody.get("token").getAsString();
                            Log.d("Response : ", jsonBody.toString());
                            Toast.makeText(context,jsonBody.get("code").getAsString(),Toast.LENGTH_LONG)
                                    .show();
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

            }
        });

        // Confirm Register on click listener
        confirmRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get code from the user
                String userCode = code.getText().toString();

                // Get register module from Grandeur Auth
                Call<JsonObject> ConfirmReg = auth.register(token,userCode);

                // Call Register module with credentials
                ConfirmReg.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonBody;
                        if(response.body()!=null) {
                            jsonBody = new JsonParser().
                                    parse(response.
                                            body().
                                            toString()
                                    ).
                                    getAsJsonObject();
                            Log.d("Response : ", jsonBody.toString());
                            Toast.makeText(context,jsonBody.get("code").getAsString(),Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

            }
        });

        // Logout on click listener
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get logout module from Grandeur Auth
                Call<JsonObject> logout = auth.logout();

                // Call logout module
                logout.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonBody;
                        if(response.body()!=null) {
                            jsonBody = new JsonParser().
                                    parse(response.
                                            body().
                                            toString()
                                    ).
                                    getAsJsonObject();
                            Log.d("Response : ", jsonBody.toString());
                            Toast.makeText(context,jsonBody.get("code").getAsString(),Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });







































    }
}
