package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.model.MessageDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {

    @Value("${kafka.topic1}")
    private String topic1;

    @Value("${kafka.topic2}")
    private String topic2;


    @Value("${kafka.topic3}")
    private String topic3;

    private final KafkaTemplate<String, String> stringKafkaTemplate;

    private final KafkaTemplate<String, MessageDetails> messageDetailsKafkaTemplate;

    //stringKafkaTemplate could also be used here
    private final KafkaTemplate<String, String> messageDetailsStringKafkaTemplate;


    public KafkaProducerService(final KafkaTemplate<String, String> stringKafkaTemplate,
        final KafkaTemplate<String, MessageDetails> messageDetailsKafkaTemplate,
        final KafkaTemplate<String, String> messageDetailsStringKafkaTemplate) {
        this.stringKafkaTemplate = stringKafkaTemplate;
        this.messageDetailsKafkaTemplate = messageDetailsKafkaTemplate;
        this.messageDetailsStringKafkaTemplate = messageDetailsStringKafkaTemplate;
    }


    public void writeToTopic1(String message) {
        stringKafkaTemplate.send(topic1, message);
    }

    public void writeToTopic2(MessageDetails messageDetails) {
        //this if else could be avoided by writing
        //messageDetailsKafkaTemplate.send(topic2, messageDetails.getUsername(), messageDetails)
        //if username would be null, then key would have been null else some value
        //just to show overloaded method provision, have kept this way
        if (messageDetails.getUsername() != null) {
            messageDetailsKafkaTemplate.send(topic2, messageDetails.getUsername(), messageDetails);
        } else {
            messageDetailsKafkaTemplate.send(topic2, messageDetails);
        }
    }

    public void writeToTopic3(String messageDetailsString) {
        messageDetailsStringKafkaTemplate.send(topic3, messageDetailsString);
    }
}

