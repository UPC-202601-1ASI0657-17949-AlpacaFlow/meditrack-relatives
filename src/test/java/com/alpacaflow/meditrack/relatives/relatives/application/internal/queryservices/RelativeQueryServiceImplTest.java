package com.alpacaflow.meditrack.relatives.relatives.application.internal.queryservices;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.queries.*;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelativeQueryServiceImplTest {

    @Mock
    private RelativeRepository relativeRepository;

    @InjectMocks
    private RelativeQueryServiceImpl relativeQueryService;

    private Relative relative;

    @BeforeEach
    void setUp() {
        relative = new Relative(
                "Juan",
                "Perez",
                new Email("juan.perez@email.com"),
                "999888777",
                RelationshipType.SON_DAUGHTER,
                1L
        );
        ReflectionTestUtils.setField(relative, "id", 1L);
    }

    @Test
    void shouldReturnAllRelatives() {
        var relatives = List.of(relative);
        when(relativeRepository.findAll()).thenReturn(relatives);

        var result = relativeQueryService.handle(new GetAllRelativesQuery());

        assertEquals(1, result.size());
        assertEquals("Juan", result.getFirst().getFirstName());
        verify(relativeRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoRelatives() {
        when(relativeRepository.findAll()).thenReturn(List.of());

        var result = relativeQueryService.handle(new GetAllRelativesQuery());

        assertTrue(result.isEmpty());
        verify(relativeRepository).findAll();
    }

    @Test
    void shouldReturnRelativeById() {
        when(relativeRepository.findById(1L)).thenReturn(Optional.of(relative));

        var result = relativeQueryService.handle(new GetRelativeByIdQuery(1L));

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Juan", result.get().getFirstName());
        verify(relativeRepository).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenRelativeNotFoundById() {
        when(relativeRepository.findById(999L)).thenReturn(Optional.empty());

        var result = relativeQueryService.handle(new GetRelativeByIdQuery(999L));

        assertTrue(result.isEmpty());
        verify(relativeRepository).findById(999L);
    }

    @Test
    void shouldReturnRelativeByUserId() {
        when(relativeRepository.findByUserId(1L)).thenReturn(Optional.of(relative));

        var result = relativeQueryService.handle(new GetRelativeByUserIdQuery(1L));

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getUserId());
        assertEquals("Juan", result.get().getFirstName());
        verify(relativeRepository).findByUserId(1L);
    }

    @Test
    void shouldReturnEmptyWhenNoRelativeForUserId() {
        when(relativeRepository.findByUserId(999L)).thenReturn(Optional.empty());

        var result = relativeQueryService.handle(new GetRelativeByUserIdQuery(999L));

        assertTrue(result.isEmpty());
        verify(relativeRepository).findByUserId(999L);
    }

    @Test
    void shouldReturnRelativesBySeniorCitizenId() {
        when(relativeRepository.findBySeniorCitizenId(100L)).thenReturn(List.of(relative));

        var result = relativeQueryService.handle(new GetRelativesBySeniorCitizenIdQuery(100L));

        assertEquals(1, result.size());
        assertEquals("Juan", result.getFirst().getFirstName());
        verify(relativeRepository).findBySeniorCitizenId(100L);
    }

    @Test
    void shouldReturnEmptyListWhenNoRelativesForSeniorCitizen() {
        when(relativeRepository.findBySeniorCitizenId(999L)).thenReturn(List.of());

        var result = relativeQueryService.handle(new GetRelativesBySeniorCitizenIdQuery(999L));

        assertTrue(result.isEmpty());
        verify(relativeRepository).findBySeniorCitizenId(999L);
    }

    @Test
    void shouldThrowWhenGetByIdQueryHasNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> new GetRelativeByIdQuery(null));
    }

    @Test
    void shouldThrowWhenGetByUserIdQueryHasNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> new GetRelativeByUserIdQuery(null));
    }

    @Test
    void shouldThrowWhenGetBySeniorCitizenIdQueryHasNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> new GetRelativesBySeniorCitizenIdQuery(null));
    }
}
