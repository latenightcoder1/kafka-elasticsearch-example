package org.example.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.example.model.MessageDetails;


public class MessageDetailsSerializer implements Serializer<MessageDetails> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, MessageDetails data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing MessageDetails", e);
        }
    }
}

