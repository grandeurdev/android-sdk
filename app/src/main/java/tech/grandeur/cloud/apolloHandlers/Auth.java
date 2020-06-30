// Apollo Authentication
// This is one of the most important feature
// that we will provide with our cloud and
// it is the starting point of everything. As
// in order to be able to access the other parts
// of the SDK. It is important to first login a user
// into the Application.

package tech.grandeur.cloud.apolloHandlers;

// Android default
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.util.concurrent.CompletableFuture;

// Grandeur
import tech.grandeur.cloud.handlers.Handlers;
import tech.grandeur.cloud.handlers.Post.Post;

// Google Gson for Json
import com.google.gson.JsonObject;


public class Auth {

    Post post;
    TestPost testPost;
    Context context;

    // Constructor
    public Auth(Handlers handlers, Context context) {
        this.post = handlers.post;
        this.context = context;
    }

    // This function sends "login a user" request with required data to the server
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CompletableFuture<JsonObject> login(String email, String password) throws Exception {

        // Json Object to send to the cloud
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);

        // Sends data to the server and returns a response callback
        return this.post.send("/auth/loginwithemail", data, null, this.context);
    }
    // This function sends "login a user" request with required data to the server
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CompletableFuture<JsonObject> login1(String email, String password) throws Exception {

        // Json Object to send to the cloud
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);

        return testPost.send("/auth/loginwithemail", data, null, this.context);

        // Sends data to the server and returns a response callback
//        return this.testPost.send("/auth/loginwithemail", data, null, this.context);
    }

    // This function sends "register" request with provided data to the server
    // submit the request
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CompletableFuture<JsonObject> register(String email, String password, String displayName, String phone) throws Exception {

        // Json Object to send to the server
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);
        data.addProperty("displayName", displayName);
        data.addProperty("phone", phone);

        // Sends data to the server and returns a response callback

        return this.post.send("/auth/register", data, null, this.context);

    }

    // Confirmation function will get the token from the user received
    // earlier as a result of request request with user data via the argument
    // and will get code from the user via the argument and then using the post
    // handler function will submit the request again.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CompletableFuture<JsonObject> register(String token, String verificationCode) throws Exception {

        // Json Object to send to the server
        JsonObject data = new JsonObject();
        data.addProperty("token", token);
        data.addProperty("verificationCode", verificationCode);

        // Sends data to the server and returns a CompletableFuture
        return this.post.send("/auth/register", data, null, this.context);

    }

    // This function sends "forgotPassword" request with provided data to the server
    // submit the request
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CompletableFuture<JsonObject> forgotPassword(String email) throws Exception {

        // Json Object to send to the server
        JsonObject data = new JsonObject();
        data.addProperty("email", email);

        // Sends data to the server and returns a CompletableFuture
        return this.post.send("/auth/forgotPassword", data, null, this.context);
    }

    // Confirmation function will get the token from the user received
    // earlier as a result of forget password request with user data via the argument
    // and will get code from the user via the argument and then using the post
    // handler function will submit the request again.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CompletableFuture<JsonObject> forgotPassword(String token, String verificationCode, String password) throws Exception {

        // Json Object to send to the server
        JsonObject data = new JsonObject();
        data.addProperty("verificationCode", verificationCode);
        data.addProperty("token", token);
        data.addProperty("password", password);

        // Sends data to the server and returns a CompletableFuture
        return this.post.send("/auth/forgotPassword", data, null, this.context);
    }

    // This function sends "changePassword" request with provided data to the server
    // submit the request
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CompletableFuture<JsonObject> changePassword(String password) throws Exception {

        // Json Object to send to the server
        JsonObject data = new JsonObject();
        data.addProperty("password", password);

        // Sends data to the server and returns a CompletableFuture
        return this.post.send("/auth/changePassword", data, null, this.context);
    }

    // Confirmation function will get the token from the user received
    // earlier as a result of change password request with user data via the argument
    // and will get code from the user via the argument and then using the post
    // handler function will submit the request again.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CompletableFuture<JsonObject> changePassword(String token, String verificationCode) throws Exception {

        // Json Object to send to the server
        JsonObject data = new JsonObject();
        data.addProperty("token", token);
        data.addProperty("verificationCode", verificationCode);

        // Sends data to the server and returns a CompletableFuture
        return this.post.send("/auth/changePassword", data, null, this.context);
    }

    // This function sends PING request to the server
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CompletableFuture<JsonObject> ping() throws Exception {

        // Sends logout request to the server and returns a CompletableFuture
        return this.post.send("/auth/ping", new JsonObject(), null, this.context);
    }

    // This function sends "isAuthenticated" request to the server
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CompletableFuture<JsonObject> isAuthenticated() throws Exception {

        // Sends logout request to the server and returns a CompletableFuture
        return this.post.send("/auth/protectedpage", new JsonObject(), null, this.context);
    }

    // This function sends "logout the user" request to the server
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CompletableFuture<JsonObject> logout() throws Exception {

        // Sends logout request to the server and returns a CompletableFuture
        return this.post.send("/auth/logout", new JsonObject(), null, this.context);
    }
}
