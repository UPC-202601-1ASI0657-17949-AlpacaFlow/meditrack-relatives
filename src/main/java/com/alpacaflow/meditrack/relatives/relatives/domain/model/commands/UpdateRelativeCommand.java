package com.alpacaflow.meditrack.relatives.relatives.domain.model.commands;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.RelationshipType;

public record UpdateRelativeCommand(
        Long relativeId,
        String firstName,
        String lastName,
        String email,
        String phone,
        RelationshipType relationshipType
) {
    public UpdateRelativeCommand {
        if (relativeId == null) {
            throw new IllegalArgumentException("Relative ID cannot be null");
        }
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
