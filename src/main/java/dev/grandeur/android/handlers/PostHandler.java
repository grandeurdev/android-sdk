package dev.grandeur.android.handlers;

import static dev.grandeur.android.utils.Encoder.bytesToHex;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import dev.grandeur.android.types.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import dev.grandeur.android.utils.*;

public class PostHandler {
    private Config _config;
    private String _status;
    private Context _context;

    private OkHttpClient _client = new OkHttpClient();

    public PostHandler(Context context, Config config) {
        _context = context;
        _config = config;
        _status = "ACTIVE";
    }

    public Config getConfig() { return _config; }

    public String generateSignature(String path, String url, Map<String, String> headers, JSONObject body) throws NoSuchAlgorithmException, InvalidKeyException {
        // Function to generate the signature of request
        // Start with path and generate normalized
        String canonicalPath = path;
        // Then normalize the query
        String canonicalQuery = Encoder.encodeURIComponent(url.split(Pattern.quote("?"))[1]);
        // Then normalize the key headers
        List<String> requiredHeaders = Arrays.asList("gt-date", "gt-access-token", "content-type");
        // Convert each header into canonical form and then
        // form the overall headers string
        String canonicalHeaders = requiredHeaders.stream().reduce("", (accumulator, name) ->
                accumulator + name + ":" + headers.get(name).replaceAll("/\\s/g", "")
        );
        // Convert the body in the canonical form
        String canonicalBody = Encoder.encodeURIComponent(body.toString());
        // Build the sign string
        String signString = canonicalPath + "\n" +
                canonicalQuery + "\n" +
                canonicalHeaders + "\n" +
                canonicalBody;
        // Generate signature
        SecretKeySpec secretKeySpec = new SecretKeySpec(_config.accessKey.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        String signature = bytesToHex(mac.doFinal(signString.getBytes())).toLowerCase(Locale.ROOT);

        return signature;
    }

    public void dispose() { _status = "DISPOSED"; }

    public void send(String path, JSONObject data, ResponseCallback callback) throws Exception {
        // Checking the connection status.
        if(_status == "DISPOSED") callback.call(new JSONObject("{ \"code\": \"DISPOSED\" }"));

        // Forming the url.
        String url = _config.url + (path != null ? path : "/") + "?apiKey=" + _config.apiKey;

        // Getting the session token from the storage.
        String token = null;
        token = new LocalStorage(_context).getItem("grandeur-auth-" + _config.apiKey);

        // Forming headers.
        Map headers = new HashMap();
        headers.put("gt-date", String.valueOf(new Date().getTime()));
        headers.put("gt-access-token", _config.accessToken);
        headers.put("authorization", token != null ? token : "");
        headers.put("content-type", "application/json");

        // Generating request signature.
        String signature = generateSignature(path, url, headers, data);

        // Forming the request: baseUrl + path + query (apiKey)
        Request request = new Request.Builder()
                .url(url)
                .addHeader("gt-date", (String) headers.get("gt-date"))
                .addHeader("gt-access-token", (String) headers.get("gt-access-token"))
                .addHeader("authorization", (String) headers.get("authorization"))
                .addHeader("gt-signature", (String) signature)
                .post(RequestBody.create(data.toString(), MediaType.parse("application/json")))
                .build();

        // Sending the request asynchronously.
        _client.newCall(request).enqueue(new Callback() {
            // Called on request failure.
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) { e.printStackTrace(); }
            // Called on request success
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    callback.call(new JSONObject(response.body().string()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
