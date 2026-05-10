package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources.CreateRelativeResource;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.RelationshipType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RelativesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void shouldCreateRelativeSuccessfully() throws Exception {
        var resource = new CreateRelativeResource(
                "Ana", "García", "ana.garcia@email.com",
                "999888777", RelationshipType.SIBLING, 1L);

        mockMvc.perform(post("/api/v1/relatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Ana")))
                .andExpect(jsonPath("$.lastName", is("García")))
                .andExpect(jsonPath("$.email", is("ana.garcia@email.com")))
                .andExpect(jsonPath("$.relationshipType", is("SIBLING")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @Order(2)
    void shouldGetRelativeByIdAfterCreation() throws Exception {
        var resource = new CreateRelativeResource(
                "Carlos", "López", "carlos.lopez@email.com",
                "999888777", RelationshipType.SON_DAUGHTER, 2L);

        var createResult = mockMvc.perform(post("/api/v1/relatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andReturn();

        var createdJson = createResult.getResponse().getContentAsString();
        var createdId = objectMapper.readTree(createdJson).get("id").asLong();

        mockMvc.perform(get("/api/v1/relatives/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Carlos")))
                .andExpect(jsonPath("$.lastName", is("López")))
                .andExpect(jsonPath("$.email", is("carlos.lopez@email.com")))
                .andExpect(jsonPath("$.id", is((int) createdId)));
    }

    @Test
    void shouldReturn404WhenRelativeNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/relatives/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllRelatives() throws Exception {
        mockMvc.perform(get("/api/v1/relatives"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void shouldReturn400WhenCreateWithInvalidData() throws Exception {
        var invalidResource = """
            {
                "firstName": "",
                "lastName": "García",
                "email": "invalid-email",
                "phone": "999888777",
                "relationshipType": "SIBLING",
                "userId": 1
            }
        """;

        mockMvc.perform(post("/api/v1/relatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidResource))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetRelativeByUserIdSuccessfully() throws Exception {
        var resource = new CreateRelativeResource(
                "María", "Ramírez", "maria.ramirez@email.com",
                "999888777", RelationshipType.PARENT, 10L);

        var createResult = mockMvc.perform(post("/api/v1/relatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andReturn();

        var createdId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/v1/relatives/user/{userId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) createdId)))
                .andExpect(jsonPath("$.firstName", is("María")));
    }

    @Test
    @Order(3)
    void shouldUpdateRelativeSuccessfully() throws Exception {
        var resource = new CreateRelativeResource(
                "Luis", "Fernández", "luis.fernandez@email.com",
                "999888777", RelationshipType.FRIEND, 3L);

        var createResult = mockMvc.perform(post("/api/v1/relatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andReturn();

        var createdId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        var updateBody = """
            {
                "firstName": "Luis Updated",
                "lastName": "Fernández Updated",
                "email": "luis.updated@email.com",
                "phone": "999888777",
                "relationshipType": "SON_DAUGHTER"
            }
        """;

        mockMvc.perform(put("/api/v1/relatives/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Luis Updated")))
                .andExpect(jsonPath("$.lastName", is("Fernández Updated")))
                .andExpect(jsonPath("$.relationshipType", is("SON_DAUGHTER")));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentRelative() throws Exception {
        var updateBody = """
            {
                "firstName": "Nobody",
                "lastName": "Nowhere",
                "email": "nobody@email.com",
                "phone": "999888777",
                "relationshipType": "OTHER"
            }
        """;

        mockMvc.perform(put("/api/v1/relatives/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    void shouldDeleteRelativeSuccessfully() throws Exception {
        var resource = new CreateRelativeResource(
                "Elena", "Martínez", "elena.martinez@email.com",
                "999888777", RelationshipType.SPOUSE, 4L);

        var createResult = mockMvc.perform(post("/api/v1/relatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andReturn();

        var createdId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/api/v1/relatives/{id}", createdId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/relatives/{id}", createdId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentRelative() throws Exception {
        mockMvc.perform(delete("/api/v1/relatives/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    void shouldAssignAndUnassignRelativeToSeniorCitizen() throws Exception {
        var resource = new CreateRelativeResource(
                "Roberto", "Gómez", "roberto.gomez@email.com",
                "999888777", RelationshipType.LEGAL_GUARDIAN, 5L);

        var createResult = mockMvc.perform(post("/api/v1/relatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andReturn();

        var createdId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        var assignBody = """
            { "seniorCitizenId": 200 }
        """;

        mockMvc.perform(post("/api/v1/relatives/{id}/assignments", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(assignBody))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/relatives/{id}/assignments/{seniorCitizenId}", createdId, 200L))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetRelativesBySeniorCitizenId() throws Exception {
        var resource = new CreateRelativeResource(
                "Sofía", "Lara", "sofia.lara@email.com",
                "999888777", RelationshipType.SIBLING, 6L);

        var createResult = mockMvc.perform(post("/api/v1/relatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andReturn();

        var createdId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        var assignBody = """
            { "seniorCitizenId": 300 }
        """;

        mockMvc.perform(post("/api/v1/relatives/{id}/assignments", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(assignBody))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/relatives/by-senior-citizen/{seniorCitizenId}", 300L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)))
                .andExpect(jsonPath("$[0].firstName", is("Sofía")));
    }

    @Test
    void shouldReturn400WhenAssigningToNonExistentRelative() throws Exception {
        var assignBody = """
            { "seniorCitizenId": 999 }
        """;

        mockMvc.perform(post("/api/v1/relatives/{id}/assignments", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(assignBody))
                .andExpect(status().isBadRequest());
    }
}
