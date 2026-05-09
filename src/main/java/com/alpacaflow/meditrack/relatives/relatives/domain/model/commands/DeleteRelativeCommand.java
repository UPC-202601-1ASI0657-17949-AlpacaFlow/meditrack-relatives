package com.alpacaflow.meditrack.relatives.relatives.domain.model.commands;

public record DeleteRelativeCommand(Long relativeId) {
    public DeleteRelativeCommand {
        if (relativeId == null) {
            throw new IllegalArgumentException("Relative ID cannot be null");
        }
    }
}
