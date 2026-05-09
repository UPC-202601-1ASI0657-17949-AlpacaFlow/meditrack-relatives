package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.RelationshipType;

public record UpdateRelativeResource(
        String firstName,
        String lastName,
        String email,
        String phone,
        RelationshipType relationshipType
) {
    public UpdateRelativeResource {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (relationshipType == null) {
            throw new IllegalArgumentException("Relationship type is required");
        }
    }
}
