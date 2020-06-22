// This is the main Apollo class and it is
// used to initialize your project.

package tech.grandeur.cloud;

// Android defaults
import android.content.Context;

// Grandeur
import tech.grandeur.cloud.apolloHandlers.Auth;
import tech.grandeur.cloud.apolloHandlers.Storage;
import tech.grandeur.cloud.handlers.Handlers;
import tech.grandeur.cloud.handlers.Post.Post;

// Google Gson for Json
import com.google.gson.JsonObject;

public class Apollo {

    // Create a default config
    private JsonObject config = new JsonObject();

    // Creates Post Object
    private Post post = null;

    // Creates Handlers Object
    private Handlers handlers = null;

    // Creates an Auth object
    Auth auth = null;

    // Creates a Storage object
    Storage storage = null;

    // Context
    private Context context = null;

    Apollo(Context context){
        // Set default config
        this.config.addProperty("url","https://api.grandeur.tech");
        this.config.addProperty("node","wss://api.grandeur.tech");

        // Set context
        this.context = context;
    }

    // Function that initializes
    // the Apollo object
    public void init(String apiKey,String accessKey, String accessToken){
        // set configuration
        this.config.addProperty("apiKey",apiKey);
        this.config.addProperty("accessKey",accessKey);
        this.config.addProperty("accessToken",accessToken);

        // Post Handler
        this.post = new Post(this.config,this.context);

        // Handlers
        this.handlers = new Handlers(this.post);

        // Initialize default auth
        this.auth = new Auth(handlers,this.context);

        // Initialize default storage
        this.storage = new Storage(handlers,this.context);

    }
}
