package com.alpacaflow.meditrack.relatives.relatives.domain.model.queries;

public record GetRelativeByUserIdQuery(Long userId) {
    public GetRelativeByUserIdQuery {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}
