package com.alpacaflow.meditrack.relatives.relatives.domain.model.commands;

public record AssignRelativeToSeniorCitizenCommand(
        Long relativeId,
        Long seniorCitizenId
) {
    public AssignRelativeToSeniorCitizenCommand {
        if (relativeId == null) {
            throw new IllegalArgumentException("Relative ID cannot be null");
        }
        if (seniorCitizenId == null) {
            throw new IllegalArgumentException("Senior citizen ID cannot be null");
        }
    }
}
