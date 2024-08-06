package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.model.MessageDetails;
import org.example.service.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
@Slf4j
public class MessageController {

    private final MessageService messageService;

    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageDetails sendMessage(@RequestBody final MessageDetails message) {
        log.info("Request Received to ingest : {}", message);
        return messageService.sendMessage(message);
    }
}

