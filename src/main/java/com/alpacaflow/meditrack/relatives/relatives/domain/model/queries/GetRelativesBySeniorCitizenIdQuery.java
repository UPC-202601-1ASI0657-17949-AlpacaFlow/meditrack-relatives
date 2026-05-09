package com.alpacaflow.meditrack.relatives.relatives.domain.model.queries;

public record GetRelativesBySeniorCitizenIdQuery(Long seniorCitizenId) {
    public GetRelativesBySeniorCitizenIdQuery {
        if (seniorCitizenId == null) {
            throw new IllegalArgumentException("Senior citizen ID cannot be null");
        }
    }
}
