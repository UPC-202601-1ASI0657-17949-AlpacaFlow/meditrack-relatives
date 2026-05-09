package com.alpacaflow.meditrack.relatives.relatives.domain.model.queries;

public record GetRelativeByIdQuery(Long relativeId) {
    public GetRelativeByIdQuery {
        if (relativeId == null) {
            throw new IllegalArgumentException("Relative ID cannot be null");
        }
    }
}
