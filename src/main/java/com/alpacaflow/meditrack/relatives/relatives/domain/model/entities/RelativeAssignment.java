package com.alpacaflow.meditrack.relatives.relatives.domain.model.entities;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class RelativeAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relative_id", nullable = false)
    private Relative relative;

    @Column(name = "senior_citizen_id", nullable = false)
    private Long seniorCitizenId;

    public RelativeAssignment() {}

    public RelativeAssignment(Relative relative, Long seniorCitizenId) {
        this.relative = relative;
        this.seniorCitizenId = seniorCitizenId;
    }
}
