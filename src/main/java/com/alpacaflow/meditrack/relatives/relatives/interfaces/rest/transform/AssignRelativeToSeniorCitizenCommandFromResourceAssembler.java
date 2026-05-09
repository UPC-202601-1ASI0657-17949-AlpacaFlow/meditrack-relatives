package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.transform;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.commands.AssignRelativeToSeniorCitizenCommand;
import com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources.AssignRelativeToSeniorCitizenResource;

public class AssignRelativeToSeniorCitizenCommandFromResourceAssembler {
    public static AssignRelativeToSeniorCitizenCommand toCommandFromResource(
            Long relativeId, AssignRelativeToSeniorCitizenResource resource) {
        return new AssignRelativeToSeniorCitizenCommand(relativeId, resource.seniorCitizenId());
    }
}
