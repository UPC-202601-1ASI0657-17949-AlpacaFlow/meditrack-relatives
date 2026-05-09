package com.alpacaflow.meditrack.relatives.relatives.domain.exceptions;

public class RelativeDuplicateRegistrationException extends RuntimeException {
    public static final String CODE_DUPLICATE_REGISTRATION = "RELATIVE_DUPLICATE_REGISTRATION";

    public RelativeDuplicateRegistrationException(String message) {
        super(message);
    }
}
