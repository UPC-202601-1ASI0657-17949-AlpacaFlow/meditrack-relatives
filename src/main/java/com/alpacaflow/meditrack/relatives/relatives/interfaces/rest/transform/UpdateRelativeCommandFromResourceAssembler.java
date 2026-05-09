package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.transform;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.commands.UpdateRelativeCommand;
import com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources.UpdateRelativeResource;

public class UpdateRelativeCommandFromResourceAssembler {
    public static UpdateRelativeCommand toCommandFromResource(UpdateRelativeResource resource, Long relativeId) {
        return new UpdateRelativeCommand(
                relativeId,
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.phone(),
                resource.relationshipType()
        );
    }
}
