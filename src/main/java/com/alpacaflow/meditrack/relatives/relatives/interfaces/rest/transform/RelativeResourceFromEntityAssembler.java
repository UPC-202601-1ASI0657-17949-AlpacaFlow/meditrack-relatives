package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.transform;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources.RelativeResource;

public class RelativeResourceFromEntityAssembler {
    public static RelativeResource toResourceFromEntity(Relative entity) {
        return new RelativeResource(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail() != null ? entity.getEmail().email() : null,
                entity.getPhone(),
                entity.getRelationshipType(),
                entity.getUserId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
