package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest;

import com.alpacaflow.meditrack.relatives.relatives.application.internal.commandservices.RelativeRegistrationService;
import com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources.InternalRelativeRegistrationResource;
import com.alpacaflow.meditrack.relatives.shared.infrastructure.messaging.RelativeRegistrationRequestedMessage;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Server-to-server ACL endpoint used by IAM when {@code app.relatives.adapter=rest}.
 */
@Hidden
@RestController
@RequestMapping(value = "/api/v1/internal/relatives", produces = APPLICATION_JSON_VALUE)
public class InternalRelativeRegistrationController {

    private final RelativeRegistrationService registrationService;

    public InternalRelativeRegistrationController(RelativeRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Void> registerRelative(@RequestBody InternalRelativeRegistrationResource resource) {
        registrationService.registerFromIamMessage(new RelativeRegistrationRequestedMessage(
                UUID.randomUUID().toString(),
                resource.userId(),
                resource.email(),
                resource.firstName(),
                resource.lastName()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
