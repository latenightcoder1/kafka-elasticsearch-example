package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.model.MessageDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private KafkaProducerService kafkaProducerService;

    private ObjectMapper objectMapper;

    public MessageServiceImpl(final KafkaProducerService kafkaProducerService,
        final ObjectMapper objectMapper) {
        this.kafkaProducerService = kafkaProducerService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<MessageDetails> sendMessage(final List<MessageDetails> messages) {
        List<MessageDetails> result = new ArrayList<>();
        for (final MessageDetails messageDetails : messages) {
            messageDetails.setTimestamp(System.currentTimeMillis());
            kafkaProducerService.writeToTopic1(messageDetails.getMessage());
            kafkaProducerService.writeToTopic2(messageDetails);
            try {
                kafkaProducerService.writeToTopic3(objectMapper.writeValueAsString(messageDetails));
            } catch (JsonProcessingException e) {
                log.error("An error occurred while writing message as string json", e);
            }
            result.add(messageDetails);
        }
        return result;
    }
}
