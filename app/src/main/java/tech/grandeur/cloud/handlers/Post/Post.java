// This is a handler class
// and it is used to send request
// to the server. Now you do not
// have to have a post function
// in every class.

package tech.grandeur.cloud.handlers.Post;

// Android Defaults
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// Grandeur
import tech.grandeur.cloud.handlers.RealPathUtil;

// Google Gson for Json
import com.google.gson.JsonObject;

// Android Crypto
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

// OkHttp
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

// Retrofit
import retrofit2.Call;

public class Post {

    // Configuration
    JsonObject config = null;
    JsonObject jsonBody = null;
    Context context;

    // Constructor
    public Post(JsonObject config, Context context) {
        // Default Configuration
        this.config = config;
        this.context = context;
    }

    // Function to generate the signature for request
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    String generateSignature(String path, Map<String, String> headers, String body) {

        // Canonical Path
        String canonicalPath = path;

        // Canonical Query
        String canonicalQuery = Uri.encode("apiKey=" + this.config.get("apiKey").getAsString());

        // Canonical Headers
        String canonicalHeaders = "gt-date:" + headers.get("gt-date").toString() +
                "gt-access-token:" + headers.get("gt-access-token").toString() +
                "content-type:" + headers.get("content-type").toString();
        Log.d("Canonical Headers", canonicalHeaders);

        // Canonical Body
        String canonicalBody = Uri.encode(body);

        // Sign String
        String signString = canonicalPath + "\n" + canonicalQuery + "\n" + canonicalHeaders + "\n" + canonicalBody;
        Log.d("Canonical Sign string", signString);
        // Generate MAC signature String
        String signature = createMAC(signString, this.config.get("accessKey").getAsString());

        return signature;
    }

    // Create hmacSHA256 signature
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String createMAC(String data, String key) {

        String signature = null;

        // Define algorithm
        String algorithm = "HmacSHA256";
        try {

            // Get an algorithm instance
            Mac hmacSha256 = Mac.getInstance(algorithm);

            // Create secret key with specific string and algorithm
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), algorithm);

            // Assign secret key algorithm and initialise it
            hmacSha256.init(secretKey);

            // Update HMAC data with our data
            hmacSha256.update(data.getBytes(StandardCharsets.UTF_8));

            // Create digest of signature
            byte[] digest = hmacSha256.doFinal();

            // Encode hashed digest
            signature = bytesToHex(digest);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        } catch (InvalidKeyException e) {

            e.printStackTrace();

        }
        // Returns signature
        return signature;
    }

    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // Default function for sending requests
    // to the server.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Call<JsonObject> send(String path, JsonObject data, Uri fileUri, Context context) throws Exception {

        Call<JsonObject> service;

        // Create full path with ApiKey
        final String fullPath = path + "?apiKey=" + this.config.get("apiKey").getAsString();

        // Headers
        Map<String, String> headers = new HashMap<String, String>();

        // Set appropriate headers
        headers.put("gt-date", String.valueOf(TimeUnit.NANOSECONDS.toNanos(System.currentTimeMillis())));
        headers.put("gt-access-token", this.config.get("accessToken").getAsString());

        if (fileUri == null){

            // Set header
            headers.put("content-type", "application/json");

            // Create a new header for signature
            String signature = generateSignature(path, headers, data.toString());
            headers.put("gt-signature", signature);


            Log.d("OkHttp Headers", headers.toString());

            // Initialize Post Service
            service = PostService.
                    getService(this.config.get("url").getAsString(), context).
                    send(
                            // End point along with Api Key
                            fullPath,
                            // Json Object of data to send
                            data,
                            // All the required headers
                            headers
                    );
        } else {

            // Set header
            headers.put("content-type", "multipart/form-data");

            String simplePath = fileUri.getPath();
            String absolutePath = null;
            if(checkPermissionForReadExternalStorage(context)==true){
                absolutePath = RealPathUtil.getRealPath(this.context,fileUri);
            } else {
                requestPermissionForReadExtertalStorage(context);
            }
            Log.d("Path simple", simplePath);
            Log.d("Path Absolute", absolutePath);
            File file = new File(absolutePath);


            // Assume your file is JPG
            RequestBody requestFile =
                    RequestBody.create(file,MediaType.parse("multipart/form-file"));
            RequestBody name =
                    RequestBody.create("Demo",MediaType.parse("text/plain"));
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("files", file.getName(), requestFile);

            // Create a new header for signature
            String signature = generateSignature(path, headers, data.toString());
            Log.d("Signature", signature);
            headers.put("gt-signature", signature);

            // Remove content type header
            headers.remove("content-type");
            Log.d("OkHttp Headers", headers.toString());

            // Initialize Post Service
            service = PostService.
                    getService(this.config.get("url").getAsString(), context).
                    upload(
                            // End point along with Api Key
                            fullPath,

                            // Request body
                            body,

                            name,

                            // All the required headers
                            headers
                    );
        }

        // Return Post Service Object
        return service;
    }
    public boolean checkPermissionForReadExternalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
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