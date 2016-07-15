package io.cloudslang.content.json.exceptions;

/**
 * Created by Folea Ilie Cristian on 2/4/2016.
 */
public class RemoveEmptyElementException extends Exception {

    public RemoveEmptyElementException(String message) {
        super(message);
    }

    public RemoveEmptyElementException(Exception ex) {
        super(ex);
    }
}
