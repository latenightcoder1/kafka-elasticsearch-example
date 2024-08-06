package org.example.repository;

import org.example.model.EventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EventDocumentRepository extends ElasticsearchRepository<EventDocument, String> {

}

