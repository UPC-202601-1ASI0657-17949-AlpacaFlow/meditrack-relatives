package com.alpacaflow.meditrack.relatives.relatives.domain.model.events;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import org.springframework.context.ApplicationEvent;

public class RelativeAssignedToSeniorCitizenEvent extends ApplicationEvent {
    private final Long relativeId;
    private final Long seniorCitizenId;

    public RelativeAssignedToSeniorCitizenEvent(Relative source, Long relativeId, Long seniorCitizenId) {
        super(source);
        this.relativeId = relativeId;
        this.seniorCitizenId = seniorCitizenId;
    }

    public Long getRelativeId() { return relativeId; }
    public Long getSeniorCitizenId() { return seniorCitizenId; }
}
