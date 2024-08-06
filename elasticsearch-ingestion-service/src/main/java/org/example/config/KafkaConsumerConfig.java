package org.example.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.deserializer.MessageDetailsDeserializer;
import org.example.model.MessageDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${kafka.server}")
    private String kafkaServer;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> stringContainerFactory(
        ConsumerFactory<String, String> stringConsumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory);
        factory.setBatchListener(true);// Enable batch processing
        factory.getContainerProperties()
            .setPollTimeout(300000); // poll timeout for batching
        factory.getContainerProperties()
            .setAckMode(ContainerProperties.AckMode.MANUAL);  // Enable manual acknowledgment
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group1");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
            OffsetResetStrategy.LATEST.toString().toLowerCase());
        //latest to start receiving from where it was left
        //earliest to start from beginning
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 50);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageDetails> messageDetailsContainerFactory(
        ConsumerFactory<String, MessageDetails> messageDetailsConsumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, MessageDetails> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messageDetailsConsumerFactory);
        factory.setBatchListener(true);// Enable batch processing
        factory.getContainerProperties()
            .setPollTimeout(300000); // poll timeout for batching
        factory.getContainerProperties()
            .setAckMode(ContainerProperties.AckMode.MANUAL);  // Enable manual acknowledgment
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MessageDetails> messageDetailsConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group2");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
            OffsetResetStrategy.LATEST.toString().toLowerCase());
        //latest to start receiving from where it was left
        //earliest to start from beginning
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 50);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDetailsDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }
}

