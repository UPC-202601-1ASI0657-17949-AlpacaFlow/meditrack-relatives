package com.alpacaflow.meditrack.relatives.relatives.domain.model.events;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import org.springframework.context.ApplicationEvent;

public class RelativeCreatedEvent extends ApplicationEvent {
    private final Long relativeId;

    public RelativeCreatedEvent(Relative source, Long relativeId) {
        super(source);
        this.relativeId = relativeId;
    }

    public Long getRelativeId() { return relativeId; }
}
