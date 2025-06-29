package edu.architect_711.wordsapp.exception;

/**
 * Used when doing an extra check whether the node's group id or word id points
 * to nothing, certainly, it can't due to the explicit SQL constraints. Although,
 * anything can happen in a big app.
 */
public class NodePointsToNothingException extends IllegalStateException {

    public NodePointsToNothingException(String message) {
        super(message);
    }
    
}
