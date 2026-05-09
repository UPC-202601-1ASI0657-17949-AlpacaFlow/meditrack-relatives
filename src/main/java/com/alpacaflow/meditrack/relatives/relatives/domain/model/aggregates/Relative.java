package com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.entities.RelativeAssignment;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.events.RelativeCreatedEvent;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.events.RelativeDeletedEvent;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.events.RelativeUpdatedEvent;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.events.RelativeAssignedToSeniorCitizenEvent;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.events.RelativeUnassignedFromSeniorCitizenEvent;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.Email;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.valueobjects.RelationshipType;
import com.alpacaflow.meditrack.relatives.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Relative extends AuditableAbstractAggregateRoot<Relative> {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Embedded
    private Email email;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_type", nullable = false)
    private RelationshipType relationshipType;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "relative", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RelativeAssignment> assignments = new ArrayList<>();

    public Relative() { super(); }

    public Relative(String firstName, String lastName, Email email, String phone,
                    RelationshipType relationshipType, Long userId) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.relationshipType = relationshipType;
        this.userId = userId;
    }

    public void publishCreatedEvent() {
        this.addDomainEvent(new RelativeCreatedEvent(this, this.getId()));
    }

    public Relative updateInformation(String firstName, String lastName, Email email,
                                      String phone, RelationshipType relationshipType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.relationshipType = relationshipType;
        this.addDomainEvent(new RelativeUpdatedEvent(this, this.getId()));
        return this;
    }

    public void markForDeletion() {
        this.addDomainEvent(new RelativeDeletedEvent(this, this.getId()));
    }

    public void assignToSeniorCitizen(Long seniorCitizenId) {
        if (seniorCitizenId == null) {
            throw new IllegalArgumentException("Senior citizen ID is required");
        }
        var alreadyAssigned = assignments.stream()
                .anyMatch(a -> a.getSeniorCitizenId().equals(seniorCitizenId));
        if (alreadyAssigned) {
            throw new IllegalArgumentException(
                    "Relative is already assigned to senior citizen with id " + seniorCitizenId);
        }
        var assignment = new RelativeAssignment(this, seniorCitizenId);
        assignments.add(assignment);
        this.addDomainEvent(new RelativeAssignedToSeniorCitizenEvent(this, this.getId(), seniorCitizenId));
    }

    public void unassignFromSeniorCitizen(Long seniorCitizenId) {
        var assignment = assignments.stream()
                .filter(a -> a.getSeniorCitizenId().equals(seniorCitizenId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Relative is not assigned to senior citizen with id " + seniorCitizenId));
        assignments.remove(assignment);
        this.addDomainEvent(new RelativeUnassignedFromSeniorCitizenEvent(this, this.getId(), seniorCitizenId));
    }

    public boolean isAssignedToSeniorCitizen(Long seniorCitizenId) {
        return assignments.stream().anyMatch(a -> a.getSeniorCitizenId().equals(seniorCitizenId));
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
