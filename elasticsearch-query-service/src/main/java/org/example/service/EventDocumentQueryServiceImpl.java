package org.example.service;

import java.util.List;
import org.example.model.DocumentSearchRequest;
import org.example.model.EventDocument;
import org.example.model.EventDocumentResponse;
import org.example.repository.AdvancedEventDocumentQueryRepository;
import org.example.repository.EventDocumentRepository;
import org.springframework.stereotype.Service;

@Service
public class EventDocumentQueryServiceImpl implements EventDocumentQueryService {

    private EventDocumentRepository eventDocumentRepository;

    private AdvancedEventDocumentQueryRepository advancedEventDocumentQueryRepository;

    public EventDocumentQueryServiceImpl(EventDocumentRepository eventDocumentRepository,
        AdvancedEventDocumentQueryRepository advancedEventDocumentQueryRepository) {
        this.eventDocumentRepository = eventDocumentRepository;
        this.advancedEventDocumentQueryRepository = advancedEventDocumentQueryRepository;
    }

    @Override
    public EventDocumentResponse getDocuments(final String username) {
        final List<EventDocument> eventDocumentList = eventDocumentRepository.findByUsername(
            username);
        return new EventDocumentResponse(eventDocumentList, 0, eventDocumentList.size(),
            eventDocumentList.size());
    }

    @Override
    public EventDocumentResponse searchDocuments(
        final DocumentSearchRequest documentSearchRequest) {
        return advancedEventDocumentQueryRepository.searchDocuments(documentSearchRequest);
    }
}
