package org.example.repository;

import org.example.model.DocumentSearchRequest;
import org.example.model.EventDocumentResponse;

public interface AdvancedEventDocumentQueryRepository {

    EventDocumentResponse searchDocuments(DocumentSearchRequest documentSearchRequest);
}
