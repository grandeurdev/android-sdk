package tech.grandeur.cloud.cookies;

// Android defaults
import android.content.Context;
import java.io.IOException;
import java.util.HashSet;

// AndroidX preferences manager
import androidx.preference.PreferenceManager;

// OkHttp
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


// This interceptor put all the Cookies in Preferences in the Request.
public class AddCookiesInterceptor implements Interceptor {

    public static final String PREF_COOKIES = "PREF_COOKIES";

    private Context context;

    public AddCookiesInterceptor(Context context) {

        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();

        HashSet<String> preferences = (HashSet<String>) PreferenceManager.
                getDefaultSharedPreferences(context).
                getStringSet(PREF_COOKIES, new HashSet<String>());

        // Set all cookies from Shared Preferences
        for (String cookie : preferences) {

            builder.addHeader("Cookie", cookie);
        }
        return chain.proceed(builder.build());
    }
}
