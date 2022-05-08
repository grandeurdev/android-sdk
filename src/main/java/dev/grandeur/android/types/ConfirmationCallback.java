package dev.grandeur.android.types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ConfirmationCallback {
    interface Confirm {
        void call(ResponseCallback callback, String... args) throws Exception;
    }
    void call(JSONObject response, Confirm confirm) throws JSONException, IOException, NoSuchAlgorithmException, InvalidKeyException;
}
