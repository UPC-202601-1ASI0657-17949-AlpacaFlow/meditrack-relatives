package com.alpacaflow.meditrack.relatives.shared.infrastructure.messaging;

/**
 * Async command published by IAM after a relative user signs up.
 */
public record RelativeRegistrationRequestedMessage(
        String eventId,
        Long userId,
        String email,
        String firstName,
        String lastName
) {
}
