package org.example.validator;

import java.util.ArrayList;
import org.example.exception.InvalidRequestException;
import org.example.model.DocumentSearchRequest;
import org.example.model.Pagination;
import org.springframework.stereotype.Component;

@Component
public class EventDocumentQueryRequestValidator {
    
    private Integer validateAndSanitizeInteger(Integer value, String parameterName,
        Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value < 0) {
            throw new InvalidRequestException(parameterName + " is having negative value " + value);
        }
        return value;
    }

    private Pagination validateAndSanitizePaginationDetails(Pagination pagination) {
        if (pagination == null) {
            pagination = Pagination.builder().build();
        }
        pagination.setPage(validateAndSanitizeInteger(pagination.getPage(), "page", 0));
        pagination.setPageSize(
            validateAndSanitizeInteger(pagination.getPageSize(), "pageSize", 20));
        return pagination;
    }

    public DocumentSearchRequest validateAndSanitizePayload(
        final DocumentSearchRequest documentSearchRequest) {
        documentSearchRequest.setPagination(
            validateAndSanitizePaginationDetails(documentSearchRequest.getPagination()));
        if (documentSearchRequest.getSearchElements() == null) {
            documentSearchRequest.setSearchElements(new ArrayList<>());
        }
        if (documentSearchRequest.getSortings() == null) {
            documentSearchRequest.setSortings(new ArrayList<>());
        }
        if (documentSearchRequest.getRanges() == null) {
            documentSearchRequest.setRanges(new ArrayList<>());
        }
        return documentSearchRequest;
    }
}
