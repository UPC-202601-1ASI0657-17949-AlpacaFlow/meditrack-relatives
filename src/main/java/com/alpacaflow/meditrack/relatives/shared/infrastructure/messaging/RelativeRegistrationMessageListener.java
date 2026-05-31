package com.alpacaflow.meditrack.relatives.shared.infrastructure.messaging;

import com.alpacaflow.meditrack.relatives.relatives.application.internal.commandservices.RelativeRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.messaging.enabled", havingValue = "true")
public class RelativeRegistrationMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelativeRegistrationMessageListener.class);

    private final RelativeRegistrationService registrationService;

    public RelativeRegistrationMessageListener(RelativeRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @JmsListener(
            destination = MessagingQueueNames.RELATIVE_REGISTRATION_REQUESTED,
            containerFactory = "jmsListenerContainerFactory")
    public void onRelativeRegistrationRequested(RelativeRegistrationRequestedMessage message) {
        LOGGER.info("Received relative registration message eventId={}", message.eventId());
        registrationService.registerFromIamMessage(message);
    }
}
