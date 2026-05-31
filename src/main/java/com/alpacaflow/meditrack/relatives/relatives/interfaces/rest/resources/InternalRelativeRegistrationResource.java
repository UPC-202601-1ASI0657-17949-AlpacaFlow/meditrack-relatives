package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources;

public record InternalRelativeRegistrationResource(
        Long userId,
        String email,
        String firstName,
        String lastName
) {
}
