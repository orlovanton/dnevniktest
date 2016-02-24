package ru.antonorlov.dnevniktest.exception;

import java.util.Arrays;
import java.util.List;

/**
 * Created by antonorlov on 24/02/16.
 */

/**
 * Исключение выкидываемое при ошибке обработки док-та
 */
public class DocumentProcessingException extends Exception {

    private List<DocumentError> errors;

    public DocumentProcessingException(DocumentError... errors){
        this.errors = Arrays.asList(errors);
    }

    public DocumentProcessingException(List<DocumentError> errors) {
        this.errors = errors;
    }

    public List<DocumentError> getErrors() {
        return errors;
    }
}
