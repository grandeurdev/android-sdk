package dev.grandeur.android;

import dev.grandeur.android.handlers.*;
import dev.grandeur.android.types.Config;
import android.content.Context;

public class Grandeur {

    private Config _config = new Config();
    private PostHandler _postHandler;
    private DuplexHandler _duplexHandler;
    static Context _context;

    public Grandeur() {}

    public static void initializeContext(Context context) { _context = context; }

    public Project init(String apiKey, String accessKey, String accessToken) throws Exception {
        _config.apiKey = apiKey;
        _config.accessKey = accessKey;
        _config.accessToken = accessToken;

        _postHandler = new PostHandler(_context, _config);
        _duplexHandler = new DuplexHandler(_context, _config);

        _duplexHandler.init(new Auth(_context, _postHandler));

        return new Project(_context, _postHandler, _duplexHandler);
    }
}
