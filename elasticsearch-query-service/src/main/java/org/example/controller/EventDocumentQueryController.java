package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.model.DocumentSearchRequest;
import org.example.model.EventDocumentResponse;
import org.example.service.EventDocumentQueryService;
import org.example.validator.EventDocumentQueryRequestValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("events")
@Slf4j
public class EventDocumentQueryController {

    private EventDocumentQueryService eventDocumentQueryService;

    private EventDocumentQueryRequestValidator eventDocumentQueryRequestValidator;

    public EventDocumentQueryController(EventDocumentQueryService eventDocumentQueryService,
        EventDocumentQueryRequestValidator eventDocumentQueryRequestValidator) {
        this.eventDocumentQueryService = eventDocumentQueryService;
        this.eventDocumentQueryRequestValidator = eventDocumentQueryRequestValidator;
    }


    @GetMapping
    public EventDocumentResponse getDocuments(@RequestParam final String username) {
        log.info("Request received to fetch data with username : {}", username);
        return eventDocumentQueryService.getDocuments(username);
    }

    @PostMapping(value = "search")
    public EventDocumentResponse search(@RequestBody DocumentSearchRequest documentSearchRequest) {
        log.info("Search request received with payload : {}", documentSearchRequest);
        documentSearchRequest = eventDocumentQueryRequestValidator.validateAndSanitizePayload(
            documentSearchRequest);
        return eventDocumentQueryService.searchDocuments(documentSearchRequest);
    }


}
