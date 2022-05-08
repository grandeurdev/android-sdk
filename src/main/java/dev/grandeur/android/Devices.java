package dev.grandeur.android;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import dev.grandeur.android.handlers.DuplexHandler;
import dev.grandeur.android.handlers.PostHandler;
import dev.grandeur.android.types.OnChangeCallback;
import dev.grandeur.android.types.ResponseCallback;
import dev.grandeur.android.types.SubscriptionCallback;

public class Devices {
    private DuplexHandler _duplex;

    public class Device {
        private DuplexHandler _duplex;
        private String _deviceID;

        public class Data {
            private DuplexHandler _duplex;
            private String _deviceID;

            public Data(DuplexHandler duplex, String deviceID) {
                _duplex = duplex;
                _deviceID = deviceID;
            }

            public void get(String path, ResponseCallback callback) throws Exception {
                // Method to get device data at a path
                _duplex.send(
                        "/device/data/get",
                        new JSONObject().put("deviceID", _deviceID).put("path", path),
                        callback
                );
            }



            public void set(String path, Object data, ResponseCallback callback) throws Exception {
                // Method to set device data at a path
                _duplex.send(
                        "/device/data/set",
                        new JSONObject().put("deviceID", _deviceID).put("path", path).put("data", data),
                        callback
                );
            }

            public void on(String path, OnChangeCallback onChangeCallback, SubscriptionCallback subscriptionCallback) throws Exception {
                // Method to subscribe to device data at a path
                _duplex.subscribe("data",
                        new JSONObject()
                                .put("deviceID", _deviceID)
                                .put("event", "data")
                                .put("path", path),
                        onChangeCallback,
                        subscriptionCallback
                );
            }
        }

        public Device(DuplexHandler duplex, String deviceID) {
            _duplex = duplex;
            _deviceID = deviceID;
        }

        public void pair(ResponseCallback callback) throws Exception {
            // Method to pair a device with the user
            _duplex.send(
                    "/device/pair",
                    new JSONObject().put("deviceID", _deviceID),
                    callback
            );
        }

        public void unpair(ResponseCallback callback) throws Exception {
            // Method to unpair a device from the user
            _duplex.send(
                    "/device/unpair",
                    new JSONObject().put("deviceID", _deviceID),
                    callback
            );
        }

        public void get(String path, ResponseCallback callback) throws Exception {
            // Method to get set device name or data
            _duplex.send(
                    "/device/get",
                    new JSONObject().put("deviceID", _deviceID).put("path", path),
                    callback
            );
        }

        public void set(String path, JSONObject data, ResponseCallback callback) throws Exception {
            // Method to set device name or data
            _duplex.send(
                    "/device/set",
                    new JSONObject().put("deviceID", _deviceID).put("path", path).put("data", data),
                    callback
            );
        }

        public void on(String event, OnChangeCallback onChangeCallback, SubscriptionCallback subscriptionCallback) throws Exception {
            // Method to get updates whenever device name or data gets a change
            _duplex.subscribe(
                    event,
                    new JSONObject().put("event", event).put("deviceID", _deviceID),
                    onChangeCallback,
                    subscriptionCallback
            );
        }

        public Data data() {
            // Returns the object of Data class.
            return new Data(_duplex, _deviceID);
        }
    }

    public Devices(DuplexHandler duplex) {
        _duplex = duplex;
    }

    public void get(JSONObject filter, ResponseCallback callback) throws Exception {
        // Method to list all devices paired to user ID
        _duplex.send(
                "/devices/get",
                new JSONObject().put("filter", filter),
                callback
        );
    }

    public void count(JSONObject filter, ResponseCallback callback) throws Exception {
        // Method to count all online devices paired to user ID
        _duplex.send(
                "/devices/count",
                new JSONObject().put("filter", filter),
                callback
        );
    }

    public void on(ResponseCallback callback) throws Exception {
        // Method to list all devices paired to user ID
        _duplex.send(
                "/devices/on",
                new JSONObject().put("event", "devices"),
                callback
        );
    }

    public Device device(String deviceID) {
        // Returns the object of Device class.
        return new Device(_duplex, deviceID);
    }
}