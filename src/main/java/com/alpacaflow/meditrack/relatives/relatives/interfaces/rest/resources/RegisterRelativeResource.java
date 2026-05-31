package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegisterRelativeResource(
        Long userId,
        String firstName,
        String lastName,
        @JsonAlias("phoneNumber") String phone,
        Long seniorCitizenId,
        String planType,
        String email
) {
}
