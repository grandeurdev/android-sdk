package dev.grandeur.android.types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

@FunctionalInterface
public interface SubscriptionCallback {
    interface Clear {
        void call(ResponseCallback callback) throws Exception;
    }
    void call(final JSONObject response, Clear clear) throws JSONException, IOException, NoSuchAlgorithmException, InvalidKeyException;
}
