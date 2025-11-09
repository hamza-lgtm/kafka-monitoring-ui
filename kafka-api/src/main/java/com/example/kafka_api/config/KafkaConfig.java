package com.example.kafka_api.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {
    @Bean
    public AdminClient adminClient() {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return AdminClient.create(properties);
    }

    // KafkaConsumer for throughput monitoring
    @Bean
    public KafkaConsumer<String, String> kafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");  // Kafka broker address
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "throughput-monitor-group");  // Consumer group ID
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());  // Key deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());  // Value deserializer
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");  // Start from earliest offset
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");  // Disable auto commit

        return new KafkaConsumer<>(props);
    }
}