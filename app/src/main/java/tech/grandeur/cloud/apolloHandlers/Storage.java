// Apollo storage
// This class provides
// all the necessary file storage functions
// of Grandeur Apollo.
// In order to use file storage features
// you must include this file

package tech.grandeur.cloud.apolloHandlers;

// Android defaults
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;

// Grandeur
import tech.grandeur.cloud.handlers.Handlers;
import tech.grandeur.cloud.handlers.Post.Post;

// Google Gson for Json
import com.google.gson.JsonObject;

// Retrofit
import retrofit2.Call;

public class Storage {
    Post post;
    Context context;

    // Constructor
    public Storage(Handlers handlers, Context context) {
        this.post = handlers.post;
        this.context = context;
    }

    // This function is used to send any
    // file along with its name to the server.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Call<JsonObject> uploadFile(Uri file, String filename) throws Exception{

        JsonObject data = new JsonObject();
        data.addProperty("fileName",filename);

        return this.post.send("/storage/uploadFile",data,file,this.context).clone();
    }
}
