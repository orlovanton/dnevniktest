package ru.antonorlov.dnevniktest.exception;

/**
 * Created by antonorlov on 23/02/16.
 */
public class DocumentNotFoundException extends Exception {

    public DocumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentNotFoundException(String message) {
        super(message);
    }
}
