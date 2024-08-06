package org.example.mapper;

import org.example.model.EventDocument;
import org.example.model.MessageDetails;
import org.springframework.stereotype.Component;

@Component
public class EventDocumentMapper {

    public EventDocument mapMessageDetailsToEventDocument(final MessageDetails messageDetails) {
        return EventDocument.builder().message(messageDetails.getMessage()).username(
            messageDetails.getUsername()).createdTime(messageDetails.getTimestamp()).build();
    }

    public EventDocument mapMessageToEventDocument(final String message) {
        return EventDocument.builder().message(message).build();
    }
}
