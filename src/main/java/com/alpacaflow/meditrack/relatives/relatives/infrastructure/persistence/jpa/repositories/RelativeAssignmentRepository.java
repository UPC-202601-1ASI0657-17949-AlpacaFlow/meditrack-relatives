package com.alpacaflow.meditrack.relatives.relatives.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.entities.RelativeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelativeAssignmentRepository extends JpaRepository<RelativeAssignment, Long> {
    List<RelativeAssignment> findBySeniorCitizenId(Long seniorCitizenId);
    boolean existsByRelativeIdAndSeniorCitizenId(Long relativeId, Long seniorCitizenId);
}
