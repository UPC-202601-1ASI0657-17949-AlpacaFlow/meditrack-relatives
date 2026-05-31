package com.alpacaflow.meditrack.relatives.relatives.domain.services;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.commands.*;

import java.util.Optional;

public interface RelativeCommandService {
    Long handle(CreateRelativeCommand command);
    Optional<Relative> handle(UpdateRelativeCommand command);
    void handle(DeleteRelativeCommand command);
    void handle(AssignRelativeToSeniorCitizenCommand command);
    void handle(UnassignRelativeFromSeniorCitizenCommand command);
    Long handle(RegisterRelativeCommand command);
}
