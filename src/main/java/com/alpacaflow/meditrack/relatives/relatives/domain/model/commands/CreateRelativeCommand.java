package com.alpacaflow.meditrack.relatives.relatives.domain.model.commands;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.RelationshipType;

public record CreateRelativeCommand(
        String firstName,
        String lastName,
        String email,
        String phone,
        RelationshipType relationshipType,
        Long userId
) {
    public CreateRelativeCommand {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
        if (relationshipType == null) {
            throw new IllegalArgumentException("Relationship type cannot be null");
        }
    }
}
