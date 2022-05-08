package dev.grandeur.android;

import android.content.Context;
import android.net.LocalServerSocket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import dev.grandeur.android.handlers.PostHandler;
import dev.grandeur.android.types.ConfirmationCallback;
import dev.grandeur.android.types.ResponseCallback;
import dev.grandeur.android.utils.LocalStorage;

public class Auth {
    private Context _context;
    private PostHandler _post;
    private String _token = null;

    public Auth(Context context, PostHandler postHandler) {
        _context = context;
        _post = postHandler;
    }

    public void login(String email, String password, ResponseCallback callback) throws Exception {
        _post.send(
                "/auth/login",
                new JSONObject().put("email", email).put("password", password),
                (JSONObject res) -> {
                    // If login is successful, saving session token in storage.
                    if(res.get("code").equals("AUTH-ACCOUNT-LOGGEDIN"))
                        new LocalStorage(_context).setItem(
                                "grandeur-auth-" + _post.getConfig().apiKey,
                                (String) res.get("token")
                        );

                    // Sending response to the user.
                    callback.call(res);
                }
        );
    }

    public void register(String email, String password, String displayName, String phone, ConfirmationCallback callback) throws Exception {
        _post.send(
                "/auth/updateProfile",
                new JSONObject()
                        .put("displayName", email)
                        .put("displayPicture", password)
                        .put("phone", displayName)
                        .put("phone", phone),
                (JSONObject res) -> {
                    // If OTP is successfully sent on phone
                    if(res.get("code").equals("PHONE-CODE-SENT")) {
                        callback.call(
                                new JSONObject()
                                        .put("code", res.get("code"))
                                        .put("message", res.get("message")),
                                (ResponseCallback rCallback, String... args) -> _post.send(
                                        "/auth/register",
                                        new JSONObject()
                                                .put("token", _token)
                                                .put("verificationCode", args[0]),
                                        (JSONObject response) -> {
                                            // If registration is successful, saving session token in storage.
                                            if(response.get("code").equals("AUTH-ACCOUNT-REGISTERED"))
                                                new LocalStorage(_context).setItem(
                                                        "grandeur-auth-" + _post.getConfig().apiKey,
                                                        (String) res.get("token")
                                                );
                                            // Sending response to user.
                                            rCallback.call(response);
                                        }
                                )
                        );
                        // Save token for later call to confirm.
                        _token = res.get("token").toString();
                    }
                }
        );
    }

    public void updateProfile(String displayName, String displayPicture, String phone, ConfirmationCallback callback) throws Exception {
        _post.send(
                "/auth/updateProfile",
                new JSONObject()
                        .put("displayName", displayName)
                        .put("displayPicture", displayPicture)
                        .put("phone", phone),
                (JSONObject res) -> {
                    // If OTP is successfully sent on phone
                    if(res.get("code").equals("PHONE-CODE-SENT")) {
                        callback.call(
                                new JSONObject()
                                        .put("code", res.get("code"))
                                        .put("message", res.get("message")),
                                (ResponseCallback rCallback, String... args) -> _post.send(
                                        "/auth/updateProfile",
                                        new JSONObject()
                                                .put("token", _token)
                                                .put("verificationCode", args[0]),
                                        (JSONObject response) -> rCallback.call(response)
                                )
                            );
                        // Save token for later call to confirm.
                        _token = res.get("token").toString();
                    }
                }
        );
    }

    public void forgotPassword(String email, ConfirmationCallback callback) throws Exception {
        _post.send(
                "/auth/forgotPassword",
                new JSONObject().put("email", email),
                (JSONObject res) -> {
                    // If OTP is successfully sent on phone
                    if(res.get("code").equals("PHONE-CODE-SENT")) {
                        callback.call(new JSONObject()
                                        .put("code", res.get("code"))
                                        .put("message", res.get("message")),
                                (ResponseCallback rCallback, String... args) -> _post.send(
                                        "/auth/forgotPassword",
                                        new JSONObject()
                                                .put("token", _token)
                                                .put("verificationCode", args[0])
                                                .put("password", args[1]),
                                        (JSONObject response) -> rCallback.call(response)
                                ));
                        // Save token for later call to confirm.
                        _token = res.get("token").toString();
                    }
                }
        );
    }

    public void changePassword(String password, ConfirmationCallback callback) throws Exception {
        _post.send(
                "/auth/changePassword",
                new JSONObject().put("password", password),
                (JSONObject res) -> {
                    // If OTP is successfully sent on phone
                    if(res.get("code").equals("PHONE-CODE-SENT")) {
                        // Return to user with response and "confirm" function
                        callback.call(new JSONObject()
                                        .put("code", res.get("code"))
                                        .put("message", res.get("message")),
                                (ResponseCallback rCallback, String... args) -> _post.send(
                                        "/auth/changePassword",
                                        new JSONObject().put("token", _token).put("verificationCode", args[0]),
                                        (JSONObject response) -> rCallback.call(response)
                                ));
                        // Save token for later call to confirm.
                        _token = res.get("token").toString();
                    }
                }
        );
    }

    public void isAuthenticated(ResponseCallback callback) throws Exception {
        _post.send("/auth/protectedpage", new JSONObject(), (JSONObject res) -> callback.call(res));
    }

    public void ping(ResponseCallback callback) throws Exception {
        _post.send("/auth/ping", new JSONObject(), (JSONObject res) -> callback.call(res));
    }

    public void logout(ResponseCallback callback) throws Exception {
        _post.send("/auth/logout", new JSONObject(), (JSONObject res) -> callback.call(res));
    }
}
