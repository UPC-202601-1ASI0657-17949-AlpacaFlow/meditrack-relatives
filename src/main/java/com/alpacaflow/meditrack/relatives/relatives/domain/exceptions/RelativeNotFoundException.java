package com.alpacaflow.meditrack.relatives.relatives.domain.exceptions;

public class RelativeNotFoundException extends RuntimeException {
    public static final String CODE_NOT_FOUND = "RELATIVE_NOT_FOUND";

    public RelativeNotFoundException(Long relativeId) {
        super("Relative not found with id: " + relativeId);
    }
}
