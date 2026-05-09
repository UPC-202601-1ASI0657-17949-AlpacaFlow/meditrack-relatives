package com.alpacaflow.meditrack.relatives.relatives.application.internal.commandservices;

import com.alpacaflow.meditrack.relatives.relatives.domain.exceptions.RelativeNotFoundException;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.commands.*;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.Email;
import com.alpacaflow.meditrack.relatives.relatives.domain.services.RelativeCommandService;
import com.alpacaflow.meditrack.relatives.relatives.infrastructure.persistence.jpa.repositories.RelativeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RelativeCommandServiceImpl implements RelativeCommandService {
    private final RelativeRepository relativeRepository;

    public RelativeCommandServiceImpl(RelativeRepository relativeRepository) {
        this.relativeRepository = relativeRepository;
    }

    @Override
    public Long handle(CreateRelativeCommand command) {
        var email = new Email(command.email());
        var relative = new Relative(
                command.firstName().trim(),
                command.lastName().trim(),
                email,
                command.phone(),
                command.relationshipType(),
                command.userId()
        );
        try {
            var savedRelative = relativeRepository.save(relative);
            savedRelative.publishCreatedEvent();
            relativeRepository.save(savedRelative);
            return savedRelative.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving relative: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public Optional<Relative> handle(UpdateRelativeCommand command) {
        var result = relativeRepository.findById(command.relativeId());
        if (result.isEmpty()) {
            throw new RelativeNotFoundException(command.relativeId());
        }
        var relativeToUpdate = result.get();
        var email = new Email(command.email());
        try {
            var updatedRelative = relativeToUpdate.updateInformation(
                    command.firstName().trim(),
                    command.lastName().trim(),
                    email,
                    command.phone(),
                    command.relationshipType()
            );
            var savedRelative = relativeRepository.save(updatedRelative);
            return Optional.of(savedRelative);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating relative: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteRelativeCommand command) {
        if (!relativeRepository.existsById(command.relativeId())) {
            throw new RelativeNotFoundException(command.relativeId());
        }
        try {
            var relative = relativeRepository.findById(command.relativeId()).orElseThrow();
            relative.markForDeletion();
            relativeRepository.save(relative);
            relativeRepository.deleteById(command.relativeId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting relative: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(AssignRelativeToSeniorCitizenCommand command) {
        var relative = relativeRepository.findById(command.relativeId())
                .orElseThrow(() -> new RelativeNotFoundException(command.relativeId()));
        try {
            relative.assignToSeniorCitizen(command.seniorCitizenId());
            relativeRepository.save(relative);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error assigning relative to senior citizen: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(UnassignRelativeFromSeniorCitizenCommand command) {
        var relative = relativeRepository.findById(command.relativeId())
                .orElseThrow(() -> new RelativeNotFoundException(command.relativeId()));
        try {
            relative.unassignFromSeniorCitizen(command.seniorCitizenId());
            relativeRepository.save(relative);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error unassigning relative from senior citizen: %s".formatted(e.getMessage()));
        }
    }
}
