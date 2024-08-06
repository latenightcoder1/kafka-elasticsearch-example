package org.example.repository;

import java.util.List;
import org.example.model.EventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface EventDocumentRepository extends ElasticsearchRepository<EventDocument, String> {

    List<EventDocument> findByUsername(String username);


}
