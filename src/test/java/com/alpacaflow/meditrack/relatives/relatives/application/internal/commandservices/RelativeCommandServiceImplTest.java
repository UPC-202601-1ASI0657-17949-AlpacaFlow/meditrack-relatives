package com.alpacaflow.meditrack.relatives.relatives.application.internal.commandservices;

import com.alpacaflow.meditrack.relatives.relatives.domain.exceptions.RelativeNotFoundException;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.commands.*;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.Email;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.RelationshipType;
import com.alpacaflow.meditrack.relatives.relatives.infrastructure.persistence.jpa.repositories.RelativeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelativeCommandServiceImplTest {

    @Mock
    private RelativeRepository relativeRepository;

    @InjectMocks
    private RelativeCommandServiceImpl relativeCommandService;

    private Relative relative;

    @BeforeEach
    void setUp() {
        relative = new Relative(
                "Juan",
                "Pérez",
                new Email("juan.perez@email.com"),
                "999888777",
                RelationshipType.SON_DAUGHTER,
                1L
        );
        ReflectionTestUtils.setField(relative, "id", 1L);
    }

    @Test
    void shouldCreateRelativeSuccessfully() {
        var command = new CreateRelativeCommand(
                "Juan", "Pérez", "juan.perez@email.com",
                "999888777", RelationshipType.SON_DAUGHTER, 1L);

        when(relativeRepository.save(any(Relative.class))).thenReturn(relative);

        var relativeId = relativeCommandService.handle(command);

        assertEquals(1L, relativeId);
        verify(relativeRepository, times(2)).save(any(Relative.class));
    }

    @Test
    void shouldThrowWhenCreateFailsWithException() {
        var command = new CreateRelativeCommand(
                "Juan", "Pérez", "juan.perez@email.com",
                "999888777", RelationshipType.SON_DAUGHTER, 1L);

        when(relativeRepository.save(any(Relative.class))).thenThrow(new RuntimeException("DB error"));

        assertThrows(IllegalArgumentException.class, () -> relativeCommandService.handle(command));
    }

    @Test
    void shouldUpdateRelativeSuccessfully() {
        var command = new UpdateRelativeCommand(
                1L, "Ana", "García", "ana.garcia@email.com",
                "999888777", RelationshipType.SIBLING);

        when(relativeRepository.findById(1L)).thenReturn(Optional.of(relative));
        when(relativeRepository.save(any(Relative.class))).thenReturn(relative);

        var result = relativeCommandService.handle(command);

        assertTrue(result.isPresent());
        assertEquals("Ana", result.get().getFirstName());
        assertEquals("García", result.get().getLastName());
        assertEquals(RelationshipType.SIBLING, result.get().getRelationshipType());
    }

    @Test
    void shouldThrowWhenRelativeNotFoundOnUpdate() {
        var command = new UpdateRelativeCommand(
                999L, "Juan", "Pérez", "juan@email.com",
                "999888777", RelationshipType.SON_DAUGHTER);

        when(relativeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RelativeNotFoundException.class, () -> relativeCommandService.handle(command));
    }

    @Test
    void shouldDeleteRelativeSuccessfully() {
        var command = new DeleteRelativeCommand(1L);

        when(relativeRepository.existsById(1L)).thenReturn(true);
        when(relativeRepository.findById(1L)).thenReturn(Optional.of(relative));

        relativeCommandService.handle(command);

        verify(relativeRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenRelativeNotFoundOnDelete() {
        var command = new DeleteRelativeCommand(999L);

        when(relativeRepository.existsById(999L)).thenReturn(false);

        assertThrows(RelativeNotFoundException.class, () -> relativeCommandService.handle(command));
    }

    @Test
    void shouldAssignRelativeToSeniorCitizenSuccessfully() {
        var command = new AssignRelativeToSeniorCitizenCommand(1L, 100L);

        when(relativeRepository.findById(1L)).thenReturn(Optional.of(relative));

        relativeCommandService.handle(command);

        assertTrue(relative.isAssignedToSeniorCitizen(100L));
        verify(relativeRepository).save(relative);
    }

    @Test
    void shouldThrowWhenAssigningToNonExistentRelative() {
        var command = new AssignRelativeToSeniorCitizenCommand(999L, 100L);

        when(relativeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RelativeNotFoundException.class, () -> relativeCommandService.handle(command));
    }

    @Test
    void shouldUnassignRelativeFromSeniorCitizenSuccessfully() {
        relative.assignToSeniorCitizen(100L);

        var command = new UnassignRelativeFromSeniorCitizenCommand(1L, 100L);

        when(relativeRepository.findById(1L)).thenReturn(Optional.of(relative));

        relativeCommandService.handle(command);

        assertFalse(relative.isAssignedToSeniorCitizen(100L));
        verify(relativeRepository).save(relative);
    }

    @Test
    void shouldThrowWhenUnassigningFromNonExistentRelative() {
        var command = new UnassignRelativeFromSeniorCitizenCommand(999L, 100L);

        when(relativeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RelativeNotFoundException.class, () -> relativeCommandService.handle(command));
    }
}
