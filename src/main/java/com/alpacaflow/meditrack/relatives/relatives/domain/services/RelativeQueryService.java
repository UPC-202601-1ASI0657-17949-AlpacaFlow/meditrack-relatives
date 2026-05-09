package com.alpacaflow.meditrack.relatives.relatives.domain.services;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface RelativeQueryService {
    List<Relative> handle(GetAllRelativesQuery query);
    Optional<Relative> handle(GetRelativeByIdQuery query);
    Optional<Relative> handle(GetRelativeByUserIdQuery query);
    List<Relative> handle(GetRelativesBySeniorCitizenIdQuery query);
}
