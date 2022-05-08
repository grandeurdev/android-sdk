package dev.grandeur.android;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import dev.grandeur.android.handlers.DuplexHandler;
import dev.grandeur.android.types.ResponseCallback;

public class Datastore {
    private DuplexHandler _duplex;

    public class Collection {
        private DuplexHandler _duplex;
        private String _collection;

        public class Pipeline {
            private DuplexHandler _duplex;
            private String _collection;
            private JSONObject _index;
            private List _query;

            public Pipeline(DuplexHandler duplex, String name, JSONObject index, List query) {
                _duplex = duplex;
                _collection = name;
                _index = index != null ? index : new JSONObject();
                _query = query != null ? query : new ArrayList();
            }

            public Pipeline match(JSONObject filter) throws JSONException {
                _query.add(new JSONObject()
                        .put("type", "match")
                        .put("filter", filter));
                return new Pipeline(_duplex, _collection, _index, _query);
            }

            public Pipeline project(JSONObject specs) throws JSONException {
                _query.add(new JSONObject()
                        .put("type", "project")
                        .put("specs", specs));

                return new Pipeline(_duplex, _collection, _index, _query);
            }

            public Pipeline group(JSONObject condition, JSONObject fields) throws JSONException {
                _query.add(new JSONObject()
                        .put("type", "group")
                        .put("condition", condition)
                        .put("fields", fields));

                return new Pipeline(_duplex, _collection, _index, _query);
            }

            public Pipeline sort(JSONObject specs) throws JSONException {
                _query.add(new JSONObject()
                        .put("type", "sort")
                        .put("specs", specs));

                return new Pipeline(_duplex, _collection, _index, _query);
            }



            public void execute(int nPage, ResponseCallback callback) throws Exception {
                _duplex.send(
                        "/datastore/pipeline",
                        new JSONObject()
                                .put("collection", _collection)
                                .put("index", _index)
                                .put("pipeline", _query.toArray())
                                .put("nPage", nPage),
                        callback
                );
            }
        }

        public Collection(DuplexHandler duplex, String name) {
            _duplex = duplex;
            _collection = name;
        }

        public void insert(JSONObject[] documents, ResponseCallback callback) throws Exception {
            _duplex.send("/datastore/insert",
                    new JSONObject()
                            .put("collection", _collection)
                            .put("documents", documents),
                    callback
            );
        }

        public void update(JSONObject filter, JSONObject update, ResponseCallback callback) throws Exception {
            _duplex.send("/datastore/update",
                    new JSONObject()
                            .put("collection", _collection)
                            .put("filter", filter)
                            .put("update", update),
                    callback
            );
        }

        public void search(JSONObject filter, JSONObject projection, int nPage, ResponseCallback callback) throws Exception {
            Pipeline searchPipeline = new Pipeline(_duplex, _collection, null, null)
                    .match(filter);

            if(projection != null) searchPipeline = searchPipeline.project(projection);

            searchPipeline.execute(nPage, callback);
        }
    }

    public Datastore(DuplexHandler duplex) {
        _duplex = duplex;
    }

    public Collection collection(String name) {
        return new Collection(_duplex, name);
    }

    public void list(int nPage, ResponseCallback callback) throws Exception {
        _duplex.send(
                "/datastore/list",
                new JSONObject().put("nPage", nPage),
                callback
        );
    }

    public void drop(String collectionName, ResponseCallback callback) throws Exception {
        _duplex.send(
                "/datastore/drop",
                new JSONObject().put("collection", collectionName),
                callback
        );
    }
}
