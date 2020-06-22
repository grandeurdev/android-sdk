// This class is used to handle
// POST and DUPLEX handlers.
// It packs then into a container
// and returns that.

package tech.grandeur.cloud.handlers;

// Grandeur
import tech.grandeur.cloud.handlers.Post.Post;

public class Handlers {

    // Creates a Post object
    public Post post;

    // Constructor
    public Handlers(Post post){
        this.post = post;
    }

}
