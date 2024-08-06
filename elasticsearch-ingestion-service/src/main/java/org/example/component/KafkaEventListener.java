package org.example.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.mapper.EventDocumentMapper;
import org.example.model.EventDocument;
import org.example.model.MessageDetails;
import org.example.repository.EventDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaEventListener {

    @Autowired
    private EventDocumentRepository eventDocumentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventDocumentMapper eventDocumentMapper;

    @KafkaListener(topics = "${kafka.topic1}", containerFactory = "stringContainerFactory")
    public void consumeStringMessage(List<ConsumerRecord<String, String>> messages,
        Acknowledgment acknowledgment) {
        log.info("size of received string messages : {}", messages.size());
        try {
            List<EventDocument> eventList = new ArrayList<>();
            for (ConsumerRecord<String, String> message : messages) {
                log.info("string message recieved : {}", message);
                eventList.add(eventDocumentMapper.mapMessageToEventDocument(message.value()));
            }
            eventDocumentRepository.saveAll(eventList);  // Write batch of events to Elasticsearch
            acknowledgment.acknowledge();    // Manually acknowledge the processing of the batch
        } catch (Exception e) {
            log.error("An error occurred while writing string messages : {} to elasticsearch",
                messages, e);
        }
    }

    @KafkaListener(topics = "${kafka.topic3}", containerFactory = "stringContainerFactory")
    public void consumeMessageDetailsJsonStringMessage(
        List<ConsumerRecord<String, String>> messages, Acknowledgment acknowledgment) {
        log.info("size of received json string messages : {}", messages.size());
        try {
            List<EventDocument> eventList = new ArrayList<>();
            for (ConsumerRecord<String, String> message : messages) {
                log.info("json string message received : {}", message);
                final MessageDetails messageDetails = objectMapper.readValue(message.value(),
                    MessageDetails.class);
                eventList.add(eventDocumentMapper.mapMessageDetailsToEventDocument(messageDetails));
            }
            eventDocumentRepository.saveAll(eventList);  // Write batch of events to Elasticsearch
            acknowledgment.acknowledge();    // Manually acknowledge the processing of the batch
        } catch (Exception e) {
            log.error("An error occurred while writing messageDetails : {} to elasticsearch",
                messages, e);
        }
    }

    @KafkaListener(topics = "${kafka.topic2}", containerFactory = "messageDetailsContainerFactory")
    public void consumeCustomObject(List<ConsumerRecord<String, MessageDetails>> messages,
        Acknowledgment acknowledgment) {
        log.info("size of received messageDetails messages : {}", messages.size());
        try {
            List<EventDocument> eventList = new ArrayList<>();
            for (ConsumerRecord<String, MessageDetails> message : messages) {
                log.info("message received : {}", message);
                eventList.add(
                    eventDocumentMapper.mapMessageDetailsToEventDocument(message.value()));
            }
            eventDocumentRepository.saveAll(eventList);  // Write batch of events to Elasticsearch
            acknowledgment.acknowledge();    // Manually acknowledge the processing of the batch
        } catch (Exception e) {
            log.error(
                "An error occurred while writing messageDetails string messages : {} to elasticsearch",
                messages, e);
        }
    }

}
