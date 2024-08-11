package com.sql.parser.exception;

public class RequiredClauseMissingException extends RuntimeException {

    public RequiredClauseMissingException(String message) {
        super(message);
    }
}
