package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.transform;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.commands.CreateRelativeCommand;
import com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources.CreateRelativeResource;

public class CreateRelativeCommandFromResourceAssembler {
    public static CreateRelativeCommand toCommandFromResource(CreateRelativeResource resource) {
        return new CreateRelativeCommand(
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.phone(),
                resource.relationshipType(),
                resource.userId()
        );
    }
}
