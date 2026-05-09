package com.alpacaflow.meditrack.relatives.relatives.domain.model.commands;

public record UnassignRelativeFromSeniorCitizenCommand(
        Long relativeId,
        Long seniorCitizenId
) {
    public UnassignRelativeFromSeniorCitizenCommand {
        if (relativeId == null) {
            throw new IllegalArgumentException("Relative ID cannot be null");
        }
        if (seniorCitizenId == null) {
            throw new IllegalArgumentException("Senior citizen ID cannot be null");
        }
    }
}
