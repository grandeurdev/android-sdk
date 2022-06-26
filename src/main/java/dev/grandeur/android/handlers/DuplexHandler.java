package dev.grandeur.android.handlers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.grandeur.android.*;
import dev.grandeur.android.types.*;
import dev.grandeur.android.utils.EventEmitter;
import dev.grandeur.android.utils.LocalStorage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class DuplexHandler {
    private String _node;
    private Context _context;
    private Config _config;
    private EventEmitter _tasks;
    private EventEmitter _subscriptions;
    private String _status;
    private ConnectionCallback _cConnection;
    private Map<String, JSONObject> _queue;
    private WebSocket _ws;
    private Handler _recon;
    private Runnable _reconRunnable;
    private Handler _ping;
    private Runnable _pingRunnable;
    // Setup list for events
    private List<String> _userEvents = Arrays.asList("devices");
    private List<String> _deviceEvents = Arrays.asList("name", "status", "data");

    private OkHttpClient _client = new OkHttpClient();

    public DuplexHandler(Context context, Config config) {
        _context = context;
        _config = config;
        _node = _config.node + "?apiKey=" + _config.apiKey;
        _tasks = new EventEmitter();
        _subscriptions = new EventEmitter();
        _status = "CONNECTING";
        _cConnection = null;
        _queue = new HashMap();
    }

    public void init(Auth auth) throws Exception {
        auth.ping((JSONObject res) -> {
            switch ((String) res.get("code")) {
                case "AUTH-UNAUTHORIZED":
                    reconnect(auth);
                    setStatus("AUTH-UNAUTHORIZED");
                    flush();
                    return;
                case "SIGNATURE-INVALID":
                    setStatus("SIGNATURE-INVALID");
                    flush();
                    return;
                case "AUTH-AUTHORIZED":
                    // User is authenticated so we'll try to connect to duplex.
                    // Getting session token from storage
                    String token = new LocalStorage(_context).getItem("grandeur-auth-" + _config.apiKey);
                    token = token != null ? token : "";

                    // Forming the request: baseUrl + path + query (apiKey)
                    Request request = new Request.Builder()
                            .url(_node + "&token=" + token)
                            .build();
                    _client.newWebSocket(request, new WebSocketListener() {
                        @Override
                        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                            super.onOpen(webSocket, response);
                            _ws = webSocket;

                            // Set status to connected
                            setStatus("CONNECTED");

                            // Notify user about the change
                            if (_cConnection != null)
                                _cConnection.call("CONNECTED");

                            // Start Ping
                            _ping = new Handler(Looper.getMainLooper());
                            _pingRunnable = () -> {
                                // Send packet to server
                                JSONObject packet = null;
                                try {
                                    packet = new JSONObject()
                                            .put("header", new JSONObject()
                                                    .put("id", "ping")
                                                    .put("task", "ping"))
                                            .put("payload", new JSONObject());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                _ws.send(packet.toString());
                                // Scheduling ping again.
                                _ping.postDelayed(_pingRunnable, 25000);
                            };
                            _ping.postDelayed(_pingRunnable, 25000);

                            // Handle queued packets
                            try {
                                handle();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                            super.onMessage(webSocket, text);
                            try {
                                // When a message is received from the server on duplex
                                JSONObject data = new JSONObject(text);
                                String task = (String) ((JSONObject) data.get("header")).get("task");

                                // Raise user event
                                if (task.equals("update")) {
                                    String event = (String) ((JSONObject) data.get("payload")).get("event");
                                    String path = (String) ((JSONObject) data.get("payload")).get("path");
                                    String deviceID = (String) ((JSONObject) data.get("payload")).get("deviceID");
                                    String update = (String) ((JSONObject) data.get("payload")).get("update");
                                    // Got an update a subscribed topic
                                    // Add a patch for backward compatibility
                                    if (event.equals("deviceParms") || event.equals("deviceSummary")) event = "data";

                                    if (_deviceEvents.contains(event)) {
                                        // If event is of device type then get topic
                                        String topic = deviceID + "/" + event + (path != null ? "/" + path : "");

                                        // Then check the event type
                                        if (event.equals("data")) {
                                            // Emit event
                                            _subscriptions.pEmit(topic, path, update);
                                        }
                                        else {
                                            // Handler is defined for the event type
                                            // so execute the callback
                                            _subscriptions.emit(topic, update);
                                        }
                                    }
                                    else {
                                        // Handler is defined for the event type
                                        // so execute the callback
                                        _subscriptions.emit(event, update);
                                    }
                                }
                                else {
                                    String id = (String) ((JSONObject) data.get("header")).get("id");
                                    // Got response for a task
                                    if (!data.isNull("payload"))
                                        // Fire event
                                        _tasks.emit(id, data.get("payload"));

                                    // Since the res has been received, so we can dequeue the packet
                                    // if it was ever placed on the queue
                                    if (!task.equals("/topic/subscribe")) {
                                        // But don't remove the subscription based packets
                                        _queue.remove(id);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                            super.onClosed(webSocket, code, reason);

                            // Set the status to connecting
                            setStatus("CONNECTING");
                            // Notify user about the change
                            if (_cConnection != null)
                                _cConnection.call("DISCONNECTED");
                            // Clear ping
                            _ping.removeCallbacks(_pingRunnable);
                            // Retry connection after a while
                            reconnect(auth);
                        }

//                        @Override
//                        public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
//                            super.onClosing(webSocket, code, reason);
//                        }

                        @Override
                        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                            super.onFailure(webSocket, t, response);
                            // Set the status to connecting
                            setStatus("CONNECTING");
                            // Notify user about the change
                            if (_cConnection != null)
                                _cConnection.call("DISCONNECTED");
                            // Clear ping
                            _ping.removeCallbacks(_pingRunnable);
                            // Retry connection after a while
                            reconnect(auth);
                        }
                    });

            }

        });
    }

    public boolean getStatus() {
        return true;
    }

    public void reconnect(Auth auth) {
        // This function calls the init function again after sometime.
        // If the connection was disposed then don't reconnect.
        if (_status == "DISPOSED") return;

        _recon = new Handler(Looper.getMainLooper());
        _reconRunnable = () -> {
            _status = "CONNECTING";
            try {
                init(auth);
            } catch (Exception e) {
                e.printStackTrace();
            }
            _recon.postDelayed(_reconRunnable, 5000);
        };
        _recon.postDelayed(_reconRunnable, 5000);
    }

    public void setStatus(String status) {
        // Prevent setting status if the connection was disposed
        if (_status == "DISPOSED") return;
        // Otherwise set.
        _status = status;
    }

    public void dispose() {
        // The function closes the duplex.
        if (_status == "CONNECTED") _ws.close(1000, null);
        // Set status to disposed.
        setStatus("DISPOSED");
        // Clear timeout
        _recon.removeCallbacks(_reconRunnable);
    }

    public void onConnection(ConnectionCallback callback) {
        // This function will take the callback from user and will set it to context so that
        // a the user could be notified about possible connection changes.
        _cConnection = callback;
    }

    public void clearOnConnection() {
        // Remove the callback.
        _cConnection = null;
    }

    public void handle() throws JSONException {
        // We will loop over the queue to send
        // the stored packets to server
        for(JSONObject packet : _queue.values()) _ws.send(packet.toString());
    }

    public void flush() throws Exception {
        // This function flushes the event
        // queue of the duplex. Loop over the queue
        for(JSONObject packet : _queue.values()) {
            // Emit event and throw error.
            _tasks.emit((String) ((JSONObject) packet.get("header")).get("id"), null, new JSONObject().put("code", _status));

            // Remove the packet from queue
            _queue.remove(((JSONObject) packet.get("header")).get("id"));
        }
    }

    public void send(String event, JSONObject payload, ResponseCallback callback) throws Exception {
        // If the connection is not borked
        if (_status != "DISPOSED") {
            // Generate unique ID for the request
            String id = String.valueOf(new Date().getTime());

            // Setup packet
            JSONObject packet = new JSONObject()
                    .put("header", new JSONObject()
                            .put("id", id)
                            .put("task", event))
                    .put("payload", payload);

            // Attach an event listener
            _tasks.once(id, data -> {
                // res = data[0], err = data[1]
                // Reject if error has been returned
                if (data.length > 1) callback.call((JSONObject) data[1]);
                    // Resolve the promise
                else callback.call((JSONObject) data[0]);
            });

            // If Connected to server
            if (_status == "CONNECTED") {
                // Then send packet right
                _ws.send(packet.toString());
            }
            else
                // Otherwise store the packet into a queue
                _queue.put(id, packet);
        }
        else {
            // Otherwise return a rejection
            callback.call(new JSONObject().put("code", _status));
        }
    }

    public void subscribe(String event, JSONObject payload, OnChangeCallback onChangeCallback, SubscriptionCallback subscriptionCallback) throws Exception {
        // Method to subscribe to a particular device's data
        // Verify that the event is valid
        if (!(_deviceEvents.contains(event) || _userEvents.contains(event))) {
            // If the event is invalid
            // then return an error through callback
            onChangeCallback.call("new JSONObject().put(\"code\", \"TOPIC-INVALID\")");
            return;
        }

        // Verify that if it is a device event
        // then device id is provided
        if (_deviceEvents.contains(event) && payload.get("deviceID") == null) {
            // device id is not specified
            onChangeCallback.call(new JSONObject().put("code", "DATA-INVALID"));
            return;
        }

        //  If the connection is not borked
        if (_status != "SIGNATURE-INVALID" && _status != "DISPOSED") {
            // Generate unique ID for the request
            String id = String.valueOf(new Date().getTime());

            // Setup packet
            JSONObject packet = new JSONObject()
                    .put("header", new JSONObject()
                            .put("id", id)
                            .put("task", "/topic/subscribe"))
                    .put("payload", payload);

            // Attach an event listener
            _tasks.once(id, data -> {
                // res = data[0], err = data[1]
                // Reject if error has been returned
                if (data.length > 1) onChangeCallback.call(null, (JSONObject) data[1]);

                // Add callback to subscriptions queue
                // depending upon type of event
                String deviceID = (String) payload.get("deviceID");
                String path = (String) payload.get("path");
                if (_deviceEvents.contains(event)) {
                    // If event is of device type
                    _subscriptions.on(deviceID + "/" + event + (path != null ? ("/" + path) : ""),  onChangeCallback);
                }
                else {
                    // otherwise
                    _subscriptions.on(event, (EventEmitter.Listener) onChangeCallback);
                }

                // Resolve the promise
                subscriptionCallback.call((JSONObject) data[0], (ResponseCallback callback) -> {
                    // Remove event listener
                    if (_deviceEvents.contains(event)) {
                        // If event is of device type
                        _subscriptions.removeListener(deviceID + "/" + event + (path != null ? "/" + path : ""), (EventEmitter.Listener) onChangeCallback);
                    }
                    else {
                        // otherwise
                        _subscriptions.removeListener(event, (EventEmitter.Listener) onChangeCallback);
                    }

                    // Remove the subscription packet from queue
                    _queue.remove(((JSONObject) packet.get("header")).get("id"), payload);

                    // Send request
                    send("/topic/unsubscribe", payload, callback);
                });
            });

            // Always queue the packet because
            // we want these packets to later restore control
            _queue.put(id, packet);

            // If Connected to server
            if (_status == "CONNECTED")
                // Then send packet right away
                _ws.send(packet.toString());
        }
        else {
            // Otherwise return a rejection
            subscriptionCallback.call(new JSONObject().put("code", _status), null);
        }
    }
}
