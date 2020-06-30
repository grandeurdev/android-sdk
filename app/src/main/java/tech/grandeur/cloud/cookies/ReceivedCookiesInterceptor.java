package tech.grandeur.cloud.cookies;

// Android defaults
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

// AndroidX preferences manager
import androidx.preference.PreferenceManager;

// Google Gson for Json
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// OkHttp
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

// This interceptor put all the received Cookies in Shared Preferences.
public class ReceivedCookiesInterceptor implements Interceptor {

    private Context context;

    public ReceivedCookiesInterceptor(Context context) {

        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        // Raw response
        Response originalResponse = chain.proceed(chain.request());

        // Response body
        String response = originalResponse.body().string();

        // Response code
        Integer responseCode = originalResponse.code();

        // Set headers
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {

            HashSet<String> cookies =  new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {

                // Cookie is added to the list
                cookies.add(header);
            }
            // Open shared preferences
            SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(context).edit();

            // Add new cookies to shared preferences
            preferences.putStringSet("PREF_COOKIES", cookies).apply();

        }
        if(responseCode > 400 || responseCode == 200){

            // New Json Object
            JsonObject jsonObject;

            // Parse response to new JsonObject
            jsonObject = new JsonParser().
                        parse(response).
                        getAsJsonObject();

            // Media type of response
            MediaType contentType = originalResponse.body().contentType();

            // New response body with old response
            ResponseBody body = ResponseBody.create(jsonObject.toString(),contentType);

            // New response
            return originalResponse.newBuilder().code(200).body(body).build();
        }
        return originalResponse;
    }
}
