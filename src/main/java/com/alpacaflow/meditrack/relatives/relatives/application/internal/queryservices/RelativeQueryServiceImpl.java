package com.alpacaflow.meditrack.relatives.relatives.application.internal.queryservices;

import com.alpacaflow.meditrack.relatives.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrack.relatives.relatives.domain.model.queries.*;
import com.alpacaflow.meditrack.relatives.relatives.domain.services.RelativeQueryService;
import com.alpacaflow.meditrack.relatives.relatives.infrastructure.persistence.jpa.repositories.RelativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelativeQueryServiceImpl implements RelativeQueryService {
    private final RelativeRepository relativeRepository;

    public RelativeQueryServiceImpl(RelativeRepository relativeRepository) {
        this.relativeRepository = relativeRepository;
    }

    @Override
    public List<Relative> handle(GetAllRelativesQuery query) {
        return relativeRepository.findAll();
    }

    @Override
    public Optional<Relative> handle(GetRelativeByIdQuery query) {
        return relativeRepository.findById(query.relativeId());
    }

    @Override
    public Optional<Relative> handle(GetRelativeByUserIdQuery query) {
        return relativeRepository.findByUserId(query.userId());
    }

    @Override
    public List<Relative> handle(GetRelativesBySeniorCitizenIdQuery query) {
        return relativeRepository.findBySeniorCitizenId(query.seniorCitizenId());
    }
}
