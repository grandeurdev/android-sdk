package dev.grandeur.android.types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import dev.grandeur.android.utils.EventEmitter;

@FunctionalInterface
public interface OnChangeCallback extends EventEmitter.Listener {
    void call(final Object... data) throws Exception;
}
