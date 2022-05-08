package dev.grandeur.android.types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.function.BiFunction;
import java.util.function.Function;

import kotlin.jvm.functions.Function0;

@FunctionalInterface
public interface ResponseCallback {
    void call(JSONObject response) throws Exception;
}
