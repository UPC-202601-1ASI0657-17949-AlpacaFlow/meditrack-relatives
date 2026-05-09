package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.RelationshipType;

import java.util.Date;

public record RelativeResource(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        RelationshipType relationshipType,
        Long userId,
        Date createdAt,
        Date updatedAt
) {}
