package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.commands.DeleteRelativeCommand;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.commands.UnassignRelativeFromSeniorCitizenCommand;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.queries.GetAllRelativesQuery;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.queries.GetRelativeByIdQuery;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.queries.GetRelativeByUserIdQuery;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.queries.GetRelativesBySeniorCitizenIdQuery;
import com.alpacaflow.meditrack.relatives.relatives.domain.services.RelativeCommandService;
import com.alpacaflow.meditrack.relatives.relatives.domain.services.RelativeQueryService;
import com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.resources.*;
import com.alpacaflow.meditrack.relatives.relatives.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/relatives", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Relatives", description = "Available Relative Endpoints")
public class RelativesController {
    private final RelativeCommandService relativeCommandService;
    private final RelativeQueryService relativeQueryService;

    public RelativesController(RelativeCommandService relativeCommandService,
                               RelativeQueryService relativeQueryService) {
        this.relativeCommandService = relativeCommandService;
        this.relativeQueryService = relativeQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new relative", description = "Create a new relative")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Relative created"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<RelativeResource> createRelative(@RequestBody CreateRelativeResource resource) {
        var createRelativeCommand = CreateRelativeCommandFromResourceAssembler.toCommandFromResource(resource);
        var relativeId = relativeCommandService.handle(createRelativeCommand);
        if (relativeId == null) return ResponseEntity.badRequest().build();
        var getRelativeByIdQuery = new GetRelativeByIdQuery(relativeId);
        var relative = relativeQueryService.handle(getRelativeByIdQuery);
        if (relative.isEmpty()) return ResponseEntity.notFound().build();
        var relativeResource = RelativeResourceFromEntityAssembler.toResourceFromEntity(relative.get());
        return new ResponseEntity<>(relativeResource, HttpStatus.CREATED);
    }

    @GetMapping("/{relativeId}")
    @Operation(summary = "Get relative by ID", description = "Get relative by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relative found"),
            @ApiResponse(responseCode = "404", description = "Relative not found")})
    public ResponseEntity<RelativeResource> getRelativeById(@PathVariable Long relativeId) {
        var getRelativeByIdQuery = new GetRelativeByIdQuery(relativeId);
        var relative = relativeQueryService.handle(getRelativeByIdQuery);
        if (relative.isEmpty()) return ResponseEntity.notFound().build();
        var relativeResource = RelativeResourceFromEntityAssembler.toResourceFromEntity(relative.get());
        return ResponseEntity.ok(relativeResource);
    }

    @GetMapping
    @Operation(summary = "Get all relatives", description = "Get all relatives")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatives retrieved successfully")})
    public ResponseEntity<List<RelativeResource>> getAllRelatives() {
        var getAllRelativesQuery = new GetAllRelativesQuery();
        var relatives = relativeQueryService.handle(getAllRelativesQuery);
        var relativeResources = relatives.stream()
                .map(RelativeResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(relativeResources);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get relative by user ID", description = "Get relative by associated user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relative found"),
            @ApiResponse(responseCode = "404", description = "Relative not found")})
    public ResponseEntity<RelativeResource> getRelativeByUserId(@PathVariable Long userId) {
        var getRelativeByUserIdQuery = new GetRelativeByUserIdQuery(userId);
        var relative = relativeQueryService.handle(getRelativeByUserIdQuery);
        if (relative.isEmpty()) return ResponseEntity.notFound().build();
        var relativeResource = RelativeResourceFromEntityAssembler.toResourceFromEntity(relative.get());
        return ResponseEntity.ok(relativeResource);
    }

    @GetMapping("/by-senior-citizen/{seniorCitizenId}")
    @Operation(summary = "Get relatives by senior citizen ID",
            description = "Get all relatives assigned to a senior citizen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatives retrieved successfully")})
    public ResponseEntity<List<RelativeResource>> getRelativesBySeniorCitizenId(
            @PathVariable Long seniorCitizenId) {
        var getRelativesBySeniorCitizenIdQuery = new GetRelativesBySeniorCitizenIdQuery(seniorCitizenId);
        var relatives = relativeQueryService.handle(getRelativesBySeniorCitizenIdQuery);
        var relativeResources = relatives.stream()
                .map(RelativeResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(relativeResources);
    }

    @PutMapping("/{relativeId}")
    @Operation(summary = "Update a relative", description = "Update a relative")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relative updated"),
            @ApiResponse(responseCode = "404", description = "Relative not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<RelativeResource> updateRelative(@PathVariable Long relativeId,
                                                            @RequestBody UpdateRelativeResource resource) {
        var updateRelativeCommand = UpdateRelativeCommandFromResourceAssembler.toCommandFromResource(resource, relativeId);
        var relative = relativeCommandService.handle(updateRelativeCommand);
        if (relative.isEmpty()) return ResponseEntity.notFound().build();
        var relativeResource = RelativeResourceFromEntityAssembler.toResourceFromEntity(relative.get());
        return ResponseEntity.ok(relativeResource);
    }

    @DeleteMapping("/{relativeId}")
    @Operation(summary = "Delete a relative", description = "Delete a relative")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Relative deleted"),
            @ApiResponse(responseCode = "404", description = "Relative not found")})
    public ResponseEntity<Void> deleteRelative(@PathVariable Long relativeId) {
        var deleteRelativeCommand = new DeleteRelativeCommand(relativeId);
        try {
            relativeCommandService.handle(deleteRelativeCommand);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{relativeId}/assignments")
    @Operation(summary = "Assign relative to senior citizen",
            description = "Assign a relative to a senior citizen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Relative assigned"),
            @ApiResponse(responseCode = "404", description = "Relative or senior citizen not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<Void> assignRelativeToSeniorCitizen(
            @PathVariable Long relativeId,
            @RequestBody AssignRelativeToSeniorCitizenResource resource) {
        var command = AssignRelativeToSeniorCitizenCommandFromResourceAssembler
                .toCommandFromResource(relativeId, resource);
        try {
            relativeCommandService.handle(command);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{relativeId}/assignments/{seniorCitizenId}")
    @Operation(summary = "Unassign relative from senior citizen",
            description = "Unassign a relative from a senior citizen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Relative unassigned"),
            @ApiResponse(responseCode = "404", description = "Relative or senior citizen not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<Void> unassignRelativeFromSeniorCitizen(
            @PathVariable Long relativeId,
            @PathVariable Long seniorCitizenId) {
        var command = new UnassignRelativeFromSeniorCitizenCommand(relativeId, seniorCitizenId);
        try {
            relativeCommandService.handle(command);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
