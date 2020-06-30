package tech.grandeur.cloud.handlers.Post;

// Android defaults
import android.content.Context;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

// Grandeur
import tech.grandeur.cloud.cookies.AddCookiesInterceptor;
import tech.grandeur.cloud.cookies.ReceivedCookiesInterceptor;

// Google Gson for Json
import com.google.gson.JsonObject;

// OkHttp
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

// Retrofit
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public class PostService {

    // PostService
    public static Service Service = null;


    // Method to initialize a PostService object
    public static Service getService(String baseURL, Context context){

        // Create logging object
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // Set logging level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Create OkHttpClient builder object
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // Add Add Cookies Interceptor
        httpClient.addInterceptor(new AddCookiesInterceptor(context));

        // Add Received Cookies Interceptor
        httpClient.addInterceptor(new ReceivedCookiesInterceptor(context));

        // Add logging interceptor
        httpClient.addInterceptor(logging);

        // Create OkHttpClient object
        OkHttpClient client = httpClient.build();

        // If PostService is NULL initialize it
        if(Service == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service = retrofit.create(Service.class);
        }
        return Service;
    }

    public interface Service{

        // Send a POST request to Cloud
        @POST

        // Send function definition
        CompletableFuture<JsonObject> send(
                // Endpoint along with ApiKey
                @Url String fullPath,

                // Data to be send to the cloud
                @Body JsonObject data,

                // Headers
                @HeaderMap Map<String, String> headers
        );

        @Multipart

        // Send a POST request to Cloud
        @POST

            // Send function definition
        CompletableFuture<JsonObject> upload(
                // Endpoint along with ApiKey
                @Url String fullPath,

                // Multipart body
                @Part MultipartBody.Part file,

                @Part("filename") RequestBody name,

                // Headers
                @HeaderMap Map<String, String> headers
        );

    }
}
