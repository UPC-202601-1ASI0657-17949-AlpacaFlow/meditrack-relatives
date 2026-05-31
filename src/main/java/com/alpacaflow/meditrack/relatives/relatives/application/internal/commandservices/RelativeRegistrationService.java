package com.alpacaflow.meditrack.relatives.relatives.application.internal.commandservices;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.Email;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.RelationshipType;
import com.alpacaflow.meditrack.relatives.relatives.infrastructure.persistence.jpa.repositories.RelativeRepository;
import com.alpacaflow.meditrack.relatives.shared.infrastructure.messaging.RelativeRegistrationRequestedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RelativeRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelativeRegistrationService.class);

    private final RelativeRepository relativeRepository;

    public RelativeRegistrationService(RelativeRepository relativeRepository) {
        this.relativeRepository = relativeRepository;
    }

    @Transactional
    public void registerFromIamMessage(RelativeRegistrationRequestedMessage message) {
        LOGGER.info("Processing relative registration message eventId={} userId={}",
                message.eventId(), message.userId());

        if (message.userId() == null) {
            LOGGER.warn("Skipping relative registration message with null userId");
            return;
        }

        if (relativeRepository.existsByUserId(message.userId())) {
            LOGGER.warn("Relative already exists for userId={}", message.userId());
            return;
        }

        var emailValue = message.email() == null || message.email().isBlank()
                ? "relative-%d@meditrack.local".formatted(message.userId())
                : message.email();
        var email = new Email(emailValue);
        var relative = new Relative(
                message.firstName(),
                message.lastName(),
                email,
                "N/A",
                RelationshipType.OTHER,
                message.userId()
        );
        var saved = relativeRepository.save(relative);
        saved.publishCreatedEvent();
        relativeRepository.save(saved);

        LOGGER.info("Relative registration completed for userId={} relativeId={}",
                message.userId(), saved.getId());
    }
}
