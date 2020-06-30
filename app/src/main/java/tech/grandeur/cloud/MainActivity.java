package tech.grandeur.cloud;

// Android defaults
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.File;

//Google Gson for Json
import com.google.gson.JsonObject;

// Grandeur
import tech.grandeur.cloud.apolloHandlers.Auth;
import tech.grandeur.cloud.apolloHandlers.Storage;

// Grandeur
// Google Gson for Json


public class MainActivity extends AppCompatActivity {

    // File Select Id
    private static final int FILE_SELECT = 1;
    //Token
    String token = null;
    // Uri
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

        // Fetch image button
        Button fetchImageBtn = (Button) findViewById(R.id.fetchImage);

        // Test login 1
        Button login1 = (Button) findViewById(R.id.login1);

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

        // EditText for Image Name
        final EditText imageName = (EditText) findViewById(R.id.imageName);

        // Login On Click listener
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                // Get email from user
                String userEmail = email.getText().toString();

                // GEt password from user
                String userPassword = password.getText().toString();

                try {
                    // Create and call login module
                    JsonObject res = auth.login(userEmail,userPassword).get();
                    Toast.makeText(context,res.get("code").getAsString(),Toast.LENGTH_LONG)
                            .show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

       //  Register on click listener
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

                try {
                    // Create and call register module
                    JsonObject res = auth.register(userEmail,userPassword,userDisplayName,userPhone).get();
                    Toast.makeText(context,res.get("code").getAsString(),Toast.LENGTH_LONG)
                            .show();
                    token = res.get("token").getAsString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Confirm Register on click listener
        confirmRegBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                // Get code from the user
                String userCode = code.getText().toString();

                try {
                    // Create and call register module
                    JsonObject res = auth.register(token,userCode).get();
                    Toast.makeText(context,res.get("code").getAsString(),Toast.LENGTH_LONG)
                            .show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // Change password on click listener
        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                // Get new password from user
                String userPassword = password.getText().toString();

                try {
                    // Create and call change password module
                    JsonObject res = auth.changePassword(userPassword).get();
                    Toast.makeText(context,res.get("code").getAsString(),Toast.LENGTH_LONG)
                            .show();
                    token = res.get("code").getAsString();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // Confirm change password on click listener
        confirmChangePassBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                // Get code from the user
                String userCode = code.getText().toString();
                try {
                    // Create and call change password module
                    JsonObject res = auth.changePassword(token,userCode).get();
                    Toast.makeText(context,res.get("code").getAsString(),Toast.LENGTH_LONG)
                            .show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Forget password on click listener
        forgetPassBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                // Get email from user
                String userEmail = email.getText().toString();
                try {
                    // Create and call forget module
                    JsonObject res = auth.forgotPassword(userEmail).get();
                    Toast.makeText(context,res.get("code").getAsString(),Toast.LENGTH_LONG)
                            .show();
                    token = res.get("code").getAsString();

                } catch (Exception e) {
                    e.printStackTrace();
                }

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

                try {
                    // Create and call forget password module
                    JsonObject res = auth.forgotPassword(token,userCode,userPassword).get();
                    Toast.makeText(context,res.get("code").getAsString(),Toast.LENGTH_LONG)
                            .show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Logout on click listener
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    // Create and call logout module
                    JsonObject res = auth.logout().get();
                    Toast.makeText(context,res.get("code").getAsString(),Toast.LENGTH_LONG)
                            .show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Logout listener ends

        // Pick Image
        pickImgBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Select picture"), FILE_SELECT );
            }
        });

        // Upload Image
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                // Absolute Path
                String absolutePath = null;

                if(checkPermissionForReadExternalStorage(getApplicationContext())==true){
                    absolutePath = RealPathUtil.getRealPath(getApplicationContext(),uri);
                } else {
                    try {
                        requestPermissionForReadExtertalStorage(getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Create a new File with absolute path
                File file = new File(absolutePath);

                // Image name
                String fileName = imageName.getText().toString();
                try {
                    // Create and call storage module
                    JsonObject res = storage.uploadFile(file,fileName).get();
                    Toast.makeText(context,res.get("code").getAsString(),Toast.LENGTH_LONG)
                            .show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // Fetch File
        fetchImageBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                // Image name
                String fileName = imageName.getText().toString();
                try {
                    // Create and call storage module
                    JsonObject res = storage.getFileUrl(fileName).get();
                    Toast.makeText(context,res.get("code").getAsString(),Toast.LENGTH_LONG)
                            .show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
    // On create ends

    // Default method to fetch file URI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT)
        {
            if (resultCode == RESULT_OK)
            {
                uri = data.getData();
            }
        }
    }

    // Check if read storage permission is enabled
    public boolean checkPermissionForReadExternalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    // Request for read storage permission
    public void requestPermissionForReadExtertalStorage(Context context) throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    777);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}

