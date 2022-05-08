package dev.grandeur.android;

import android.content.Context;

import dev.grandeur.android.*;
import dev.grandeur.android.handlers.*;
import dev.grandeur.android.types.*;

public class Project {
    private Context _context;
    private PostHandler _post;
    private DuplexHandler _duplex;

    public Project() {}
    public Project(Context context, PostHandler post, DuplexHandler duplex) {
        _context = context;
        _post = post;
        _duplex = duplex;
    }
    // Connection related methods:
    // Schedules a connection handler function to be called on successful connection establishment
    // with Grandeur.
    public void onConnection(ConnectionCallback connectionCallback) {
        // Specifying connection handler for underlying duplex channel.
        _duplex.onConnection(connectionCallback);
    }
    // Removes the connection handler function.
    public void clearConnectionCallback() {
        // Clearing connection handler of its underlying duplex channel.
        _duplex.clearOnConnection();
    }
    // Checks if we are connected with Grandeur.
    public boolean isConnected() {
        return (_duplex.getStatus() == true);
    }

    // Instantiator methods — return reference to objects of their classes.
    public Auth auth() {
        return new Auth(_context, _post);
    }
    public Devices devices() { return new Devices(_duplex); }
    public Datastore datastore() { return new Datastore(_duplex); }
}