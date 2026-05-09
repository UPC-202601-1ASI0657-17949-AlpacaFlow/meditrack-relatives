package com.alpacaflow.meditrack.relatives.relatives.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelativeRepository extends JpaRepository<Relative, Long> {
    boolean existsByUserId(Long userId);
    Optional<Relative> findByUserId(Long userId);

    @Query("SELECT r FROM Relative r JOIN r.assignments a WHERE a.seniorCitizenId = :seniorCitizenId")
    List<Relative> findBySeniorCitizenId(@Param("seniorCitizenId") Long seniorCitizenId);
}
