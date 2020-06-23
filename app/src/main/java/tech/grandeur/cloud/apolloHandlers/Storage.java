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
import java.io.File;

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
    public Call<JsonObject> uploadFile(File file, String filename) throws Exception{

        // Json Object to send to the server
        JsonObject data = new JsonObject();
        data.addProperty("filename",filename);

        // Sends data to the server and returns a response callback
        return this.post.send("/storage/uploadFile",data,file,this.context).clone();
    }

    // This function to fetch a file from the
    // server's file system
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Call<JsonObject> getFileUrl(String filename) throws Exception {

        // Json Object to send to the server
        JsonObject data = new JsonObject();
        data.addProperty("filename",filename);

        // Sends logout request to the server and returns a response callback
        return this.post.send("/storage/getFileUrl", data, null, this.context);
    }
}
