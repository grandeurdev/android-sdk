package tech.grandeur.cloud;

// Android defaults
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

// Grandeur
import tech.grandeur.cloud.apolloHandlers.Auth;
import tech.grandeur.cloud.apolloHandlers.Storage;

// Google Gson for Json
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// Android Crypto
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

// Retrofit
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_SELECT = 1;
    //Token
    String token = null;
    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Context
        final Context context = this;

        // Create a new Apollo project
        Apollo apolloProject = new Apollo(this);

        // Initialize Apollo project with the ApiKey
        apolloProject.init(
                "ck412ssij0007xr239uos8jfk",
                "accesskahf3nou001e01wtdmst82ac",
                "eyJ0b2tlbiI6ImV5SmhiR2NpT2lKSVV6STFOaUlzSW5SNWNDSTZJa3BYVkNKOS5leUpwWkNJNkltRmpZMlZ6YzJ0aGFHWXpibTkxTURBeFpUQXhkM1JrYlhOME9ESmhZeUlzSW5SNWNHVWlPaUpoWTJObGMzTWlMQ0pwWVhRaU9qRTFPVEF4TURRek9UVjkua3VaNksxUzQ4ZjdLNVZ5dUxCMU0zVFVIdlVMbWhreXc4ZFJkQUdPVVlpZyJ9");

        // Get Auth functions
        final Auth auth = apolloProject.auth;

        // Get Storage functions
        final Storage storage = apolloProject.storage;

        // Login button
        Button loginBtn =  (Button) findViewById(R.id.login);

        // Register button
        Button registerBtn = (Button) findViewById(R.id.register);

        // Confirm registration button
        Button confirmRegBtn = (Button) findViewById(R.id.confirmReg);

        // Change password button
        Button changePassBtn = (Button) findViewById(R.id.changePass);

        // Confirm change password button
        Button confirmChangePassBtn = (Button) findViewById(R.id.confirmChange);

        // Forget password button
        Button forgetPassBtn = (Button) findViewById(R.id.forgetPass);

        // Confirm Forget password button
        Button confirmForgetPassBtn = (Button) findViewById(R.id.confirmForget);

        // Logout button
        Button logoutBtn = (Button) findViewById(R.id.logout);

        // Pick image button
        Button pickImgBtn = (Button) findViewById(R.id.pickImage);

        // Upload image button
        Button uploadImgBtn = (Button) findViewById(R.id.uploadImage);

        // EditText for email
        final EditText email = (EditText) findViewById(R.id.email);

        // EditText for password
        final EditText password = (EditText) findViewById(R.id.password);

        // EditText for Display Name
        final EditText displayName = (EditText) findViewById(R.id.displayName);

        // EditText for Phone
        final EditText phone = (EditText) findViewById(R.id.phone);

        // EditText for code
        final EditText code = (EditText) findViewById(R.id.code);



        // Login On Click listener
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                // Get email from user
                String userEmail = email.getText().toString();

                // GEt password from user
                String userPassword = password.getText().toString();

//                 Get login module from Grandeur Auth
                Call<JsonObject> login = null;
                try {
                    login = auth.login(userEmail, userPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                 Call login module with credentials
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                Call<JsonObject> register = null;
                try {
                    register = auth.register(userEmail,userPassword,userDisplayName,userPhone);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                // Get code from the user
                String userCode = code.getText().toString();

                // Get register module from Grandeur Auth
                Call<JsonObject> ConfirmReg = null;
                try {
                    ConfirmReg = auth.register(token,userCode);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                token = null;

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

        // Change password on click listener
        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                // Get new password from user
                String userPassword = password.getText().toString();

                // Get change password moudle from Auth
                Call<JsonObject> changePassword = null;
                try {
                    changePassword = auth.changePassword(userPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Call change password
                changePassword.enqueue(new Callback<JsonObject>() {
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

        // Confirm change password on click listener
        confirmChangePassBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                // Get code from the user
                String userCode = code.getText().toString();

                // Get change password moudle from Auth
                Call<JsonObject> changePassword = null;
                try {
                    changePassword = auth.changePassword(token,userCode);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                token = null;

                // Call change password
                changePassword.enqueue(new Callback<JsonObject>() {
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

        // Forget password on click listener
        forgetPassBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                // Get email from user
                String userEmail = email.getText().toString();

                // Get forget password moudle from Auth
                Call<JsonObject> forgetPassword = null;
                try {
                    forgetPassword = auth.forgotPassword(userEmail);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Call forget password
                forgetPassword.enqueue(new Callback<JsonObject>() {
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

        // Confirm forget password on click listener
        confirmForgetPassBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                // Get code from the user
                String userCode = code.getText().toString();

                // Get new password from user
                String userPassword = password.getText().toString();

                // Get change password module from Auth
                Call<JsonObject> forgetPassword = null;
                try {
                    forgetPassword = auth.forgotPassword(token,userCode,userPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                token = null;

                // Call change password
                forgetPassword.enqueue(new Callback<JsonObject>() {
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                // Get logout module from Grandeur Auth
                Call<JsonObject> logout = null;
                try {
                    logout = auth.logout();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

        // Logout listener ends
        pickImgBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Select picture"), FILE_SELECT );
            }
        });
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    storage.uploadFile(uri,"Demo").enqueue(new Callback<JsonObject>() {
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
                            }

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT)
        {
            if (resultCode == RESULT_OK)
            {
                uri = data.getData();


                Log.d("Uri", "onActivityResult: "+uri.toString());
            }
        }
    }
}
