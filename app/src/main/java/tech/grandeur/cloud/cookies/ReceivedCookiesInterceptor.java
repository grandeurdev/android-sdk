package tech.grandeur.cloud.cookies;

// Android defaults

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.HashSet;

// AndroidX preferences manager
import androidx.preference.PreferenceManager;

// OkHttp
import okhttp3.Interceptor;
import okhttp3.Response;

// This interceptor put all the received Cookies in Shared Preferences.
public class ReceivedCookiesInterceptor implements Interceptor {

    private Context context;

    public ReceivedCookiesInterceptor(Context context) {

        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies =  new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {

                // Cookie is added to the list
                cookies.add(header);
            }
            SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(context).edit();
            preferences.putStringSet("PREF_COOKIES", cookies).apply();

        }
        return originalResponse;
    }
}
