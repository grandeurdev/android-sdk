package dev.grandeur.android.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EventEmitter {
    private Map<String, List<Listener>> _events;
    private Map<String, Integer> _maxEvents;

    public interface Listener {
        void call(final Object... data) throws Exception;
    };

    public EventEmitter() {
        this._events = new Hashtable<String, List<Listener>>();
        this._maxEvents = new Hashtable<String, Integer>();
    }

    public boolean emit(final String event) throws Exception {
        if (_events.containsKey(event)) {
            for (Listener cb : _events.get(event))
                cb.call(null);
        } else {
            return false;
        }
        return true;
    }

    public boolean emit(final String event, final Object... data) throws Exception {
        if (_events.containsKey(event)) {
            for (Listener cb : _events.get(event))
                cb.call(data);
        } else {
            return false;
        }
        return true;
    }

    public boolean pEmit(final String event, final Object... data) throws Exception {
        for(String sub : eventsNames()) {
            if(event.matches(Pattern.quote(sub))) {
                emit(sub, data);
            }
        }
        return true;
    }

    public EventEmitter addListener(final String event, final Listener cb) {
        // check maxListens
        if (_maxEvents.containsKey(event) &&
                _maxEvents.get(event) < listenerCount(event)) {
        }

        if (!_events.containsKey(event)) {
            _events.put(event, new LinkedList<Listener>());
        }

        _events.get(event).add(cb);

        return this;
    }

    public EventEmitter addListener(final String event, final Listener cb, final int priority) {
        // check maxListens
        if (_maxEvents.containsKey(event) &&
                _maxEvents.get(event) < listenerCount(event)) {

            ///return this;
        }

        if (!_events.containsKey(event)) {
            _events.put(event, new LinkedList<Listener>());
        }

        if (priority < listenerCount(event))
            _events.get(event).add(priority, cb);
        else
            _events.get(event).add(cb);

        return this;
    }

    public EventEmitter on(final String event, final Listener cb) throws Exception {
        return addListener(event, cb);
    }

    public EventEmitter once(final String event, final Listener ocb) {
        return addListener(event, new Listener() {

            @Override
            public void call(final Object[] data) throws Exception {
                ocb.call(data);

                // remove listener
                removeListener(event, this);
            }

        });
    }

    public EventEmitter removeListener(final String event, final Listener cb) {
        if (_events.containsKey(event) && _events.get(event).contains(cb))
            _events.get(event).remove(cb);

        return this;
    }

    public EventEmitter removeListener(final String event) {
        if (_events.containsKey(event))
            _events.remove(event);

        return this;
    }

    public EventEmitter removeListener() {
        _events.clear();
        return this;
    }

    public EventEmitter removeAllListeners() {
        _events.clear();
        return this;
    }

    public EventEmitter setMaxListeners(final String event, final int n) {
        this._maxEvents.put(event, n);
        return this;
    }

    public List<String> eventsNames() {
        return new ArrayList(_events.keySet());
    }

    public List<Listener> listeners(final String event) {
        return _events.containsKey(event) ? _events.get(event) : null;
    }

    public int listenerCount(final String event) {
        return _events.containsKey(event) ? _events.get(event).size() : 0;
    }
}