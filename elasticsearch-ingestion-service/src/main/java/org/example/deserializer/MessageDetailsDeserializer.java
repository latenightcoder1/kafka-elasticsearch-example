package org.example.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.example.model.MessageDetails;


@Slf4j
public class MessageDetailsDeserializer implements Deserializer<MessageDetails> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public MessageDetails deserialize(String topic, byte[] data) {
        try {
            String jsonString = new String(data, StandardCharsets.UTF_8);
            log.info("jsonString : {}", jsonString);
            return objectMapper.readValue(data, MessageDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MessageDetails deserialize(String topic, Headers headers, byte[] data) {
        try {
            return objectMapper.readValue(data, MessageDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MessageDetails deserialize(String topic, Headers headers, ByteBuffer data) {
        return null;
    }

    @Override
    public void close() {

    }
}
