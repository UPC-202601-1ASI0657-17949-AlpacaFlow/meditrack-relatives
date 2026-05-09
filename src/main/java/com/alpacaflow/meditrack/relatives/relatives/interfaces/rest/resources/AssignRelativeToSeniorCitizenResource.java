package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources;

public record AssignRelativeToSeniorCitizenResource(Long seniorCitizenId) {
    public AssignRelativeToSeniorCitizenResource {
        if (seniorCitizenId == null) {
            throw new IllegalArgumentException("Senior citizen ID is required");
        }
    }
}
