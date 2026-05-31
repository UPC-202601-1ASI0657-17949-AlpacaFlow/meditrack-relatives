package com.alpacaflow.meditrack.relatives.relatives.domain.model.commands;

public record RegisterRelativeCommand(
        Long userId,
        String firstName,
        String lastName,
        String phone,
        Long seniorCitizenId
) {
    public RegisterRelativeCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId is required");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
    }
}
