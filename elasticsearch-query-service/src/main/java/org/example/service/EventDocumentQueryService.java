package org.example.service;

import org.example.model.DocumentSearchRequest;
import org.example.model.EventDocumentResponse;

public interface EventDocumentQueryService {

    EventDocumentResponse getDocuments(String username);

    EventDocumentResponse searchDocuments(DocumentSearchRequest documentSearchRequest);
}
