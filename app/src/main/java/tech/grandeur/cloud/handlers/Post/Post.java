// This is a handler class
// and it is used to send request
// to the server. Now you do not
// have to have a post function
// in every class.

package tech.grandeur.cloud.handlers.Post;

// Android Defaults
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    // Context
    Context context;

    // Constructor
    public Post(JsonObject config, Context context) {
        // Default Configuration
        this.config = config;
        // Context
        this.context = context;
    }

    // Default function for sending requests
    // to the server.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Call<JsonObject> send(String path, JsonObject data, File file, Context context) throws Exception {

        // Service
        Call<JsonObject> service;

        // Create full path with ApiKey
        final String fullPath = path + "?apiKey=" + this.config.get("apiKey").getAsString();

        // Headers
        Map<String, String> headers = new HashMap<String, String>();

        // Set default headers
        headers.put("gt-date", String.valueOf(TimeUnit.NANOSECONDS.toNanos(System.currentTimeMillis())));
        headers.put("gt-access-token", this.config.get("accessToken").getAsString());

        // If there is no file to upload
        if (file == null){

            // Set Json header
            headers.put("content-type", "application/json");

            // Generate signature
            String signature = generateSignature(path, headers, data.toString());

            // New header for signature
            headers.put("gt-signature", signature);

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

            // If it is a file

            // Set header
            headers.put("content-type", "multipart/form-data");

            // Create request body with your file
            RequestBody requestFile =
                    RequestBody.create(file,MediaType.parse("multipart/form-file"));

            // File name
            String filename = data.get("filename").getAsString();

            // Create request body with your file name
            RequestBody name =
                    RequestBody.create(filename,MediaType.parse("text/plain"));

            // Create Multipart body with file's request body
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("files", file.getName(), requestFile);

            Log.d("File Name", file.getName());
            // Generate signature
            String signature = generateSignature(path, headers, data.toString());

            // New header for signature
            headers.put("gt-signature", signature);

            // Remove content type header
            headers.remove("content-type");

            // Initialize Post Service
            service = PostService.
                    getService(this.config.get("url").getAsString(), context).
                    upload(
                            // End point along with Api Key
                            fullPath,
                            // Multipart Request body
                            body,
                            // File name
                            name,
                            // All the required headers
                            headers
                    );
        }

        // Return Post Service Object
        return service;
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

        // Canonical Body
        String canonicalBody = Uri.encode(body);

        // Sign String
        String signString = canonicalPath + "\n" + canonicalQuery + "\n" + canonicalHeaders + "\n" + canonicalBody;

        // Generate MAC signature String with sign stting
        String signature = createMAC(signString, this.config.get("accessKey").getAsString());

        return signature;
    }

    // Generate hmacSHA256 signature
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

            // Assign secret key to algorithm and initialise it
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

    // Convert bytes array to Hex and then to string
    private static String bytesToHex(byte[] bytes) {

        // Default hex array
        final char[] hexArray = "0123456789abcdef".toCharArray();

        char[] hexChars = new char[bytes.length * 2];

        // Conversion index by index
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        // Convert hex characters to String
        return new String(hexChars);
    }

}