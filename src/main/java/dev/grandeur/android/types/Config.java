package dev.grandeur.android.types;

// Config class for storing configurations for duplex connection establishment.
public class Config {
    public String url = "https://api.grandeur.tech";
    public String node = "wss://api.grandeur.tech";
    public String apiKey;
    public String accessKey;
    public String accessToken;

    // Constructor
    public Config() {
        apiKey = "";
        accessKey = "";
        accessToken = "";
    };

    public Config(String apiKey, String accessKey, String accessToken) {
        this.apiKey = apiKey;
        this.accessKey = accessKey;
        this.accessKey = accessToken;
    };
};