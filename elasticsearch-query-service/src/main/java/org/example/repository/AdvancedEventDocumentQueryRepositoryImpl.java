package org.example.repository;

import static org.example.constant.ElasticsearchConstants.EVENTS_INDEX;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.ElasticSearchExecutionException;
import org.example.model.DocumentSearchRequest;
import org.example.model.EventDocument;
import org.example.model.EventDocumentResponse;
import org.example.model.Pagination;
import org.example.model.SearchElement;
import org.example.model.Sorting;
import org.example.model.TimeRange;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class AdvancedEventDocumentQueryRepositoryImpl implements
    AdvancedEventDocumentQueryRepository {

    private ElasticsearchClient elasticsearchClient;

    public AdvancedEventDocumentQueryRepositoryImpl(final ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }


    @Override
    public EventDocumentResponse searchDocuments(DocumentSearchRequest documentSearchRequest) {
        //using given search elements and given ranges
        Query query = buildQuery(documentSearchRequest);
        List<SortOptions> sortOptions = getSortOptions(documentSearchRequest.getSortings());
        Pagination pagination = documentSearchRequest.getPagination();

        // Create the search request with sorting and pagination
        //page starts from 0
        SearchRequest searchRequest = new SearchRequest.Builder().index(EVENTS_INDEX).query(query)
            .sort(sortOptions).from(pagination.getPage() * pagination.getPageSize())
            .size(pagination.getPageSize()).build();

        try {
            //execute search
            SearchResponse<EventDocument> searchResponse = elasticsearchClient.search(
                searchRequest, EventDocument.class);
            long totalCount = searchResponse.hits().total().value();
            //set _id value as id of EventDocument
            List<EventDocument> eventDocuments = searchResponse.hits().hits().stream()
                .map(hit -> {
                    EventDocument eventDocument = hit.source();
                    eventDocument.setId(hit.id());
                    return eventDocument;
                }).toList();
            return EventDocumentResponse.builder().data(eventDocuments)
                .page(pagination.getPage()).pageSize(eventDocuments.size()).total(totalCount)
                .build();
        } catch (IOException e) {
            log.error("Elasticsearch execution encountered error", e);
            throw new ElasticSearchExecutionException(
                "Error experienced while searching from elasticsearch");
        }
    }

    private List<SortOptions> getSortOptions(List<Sorting> sortings) {
        List<SortOptions> sortOptions = new ArrayList<>();
        sortings.forEach(sorting -> sortOptions.add(new SortOptions.Builder().field(
                f -> f.field(sorting.getField()).order(
                    sorting.getOrder().equalsIgnoreCase(SortOrder.Desc.toString()) ? SortOrder.Desc
                        : SortOrder.Asc))
            .build()));
        return sortOptions;
    }

    /**
     * Each element in searchElements can have multiple conditions that are ANDed together. All
     * these elements are ORed together.
     * </br>
     * Each element in range from the ranges list should be ORed together.
     * </br>
     * The result should be a query where the ORed search elements are ANDed with the ORed ranges.
     *
     * @param documentSearchRequest {@link DocumentSearchRequest}
     * @return {@link Query}
     */
    private Query buildQuery(DocumentSearchRequest documentSearchRequest) {

        final List<Query> searchElementQueries = getSearchElementQueries(
            documentSearchRequest.getSearchElements());
        final List<Query> rangeQueries = getRangeQueries(documentSearchRequest.getRanges());

        Query finalQuery = Query.of(q -> q.bool(BoolQuery.of(b -> b
            .must(
                List.of(
                    Query.of(q1 -> q1.bool(BoolQuery.of(b1 -> b1.should(searchElementQueries)))),
                    Query.of(q2 -> q2.bool(BoolQuery.of(b2 -> b2.should(rangeQueries))))
                )
            )
        )));
        return finalQuery;
    }

    private List<Query> getRangeQueries(List<TimeRange> ranges) {
        List<Query> rangeQueries = new ArrayList<>();
        for (TimeRange timeRange : ranges) {
            RangeQuery.Builder rangeQueryBuilder = new RangeQuery.Builder().field("createdTime");
            if (timeRange.getGte() != null) {
                rangeQueryBuilder.gte(JsonData.of(timeRange.getGte()));
            }
            if (timeRange.getLte() != null) {
                rangeQueryBuilder.lte(JsonData.of(timeRange.getLte()));
            }
            rangeQueries.add(Query.of(q -> q.range(rangeQueryBuilder.build())));
        }
        return rangeQueries;
    }

    private List<Query> getSearchElementQueries(List<SearchElement> searchElements) {
        final List<Query> searchElementQueries = new ArrayList<>();
        for (final SearchElement searchElement : searchElements) {
            List<Query> mustQueries = new ArrayList<>();
            if (searchElement.getId() != null) {
                mustQueries.add(Query.of(q -> q.term(
                    TermQuery.of(t -> t.field("id").value(searchElement.getId())))));
            }
            if (searchElement.getMessage() != null) {
                mustQueries.add(Query.of(q -> q.term(
                    TermQuery.of(t -> t.field("message").value(searchElement.getMessage())))));
            }
            if (searchElement.getUsername() != null) {
                mustQueries.add(Query.of(q -> q.term(
                    TermQuery.of(t -> t.field("username").value(searchElement.getUsername())))));
            }
            searchElementQueries.add(Query.of(q -> q.bool(BoolQuery.of(b -> b.must(mustQueries)))));
        }
        return searchElementQueries;
    }
    
}
