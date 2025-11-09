package com.example.kafka_api.controller;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.kafka_api.service.KafkaService;
import com.example.kafka_api.DTO.TopicRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaService kafkaService;
    private final KafkaConsumer<String, String> kafkaConsumer;

    @Autowired
    public KafkaController(KafkaService kafkaService, KafkaConsumer<String, String> kafkaConsumer) {
        this.kafkaService = kafkaService;
        this.kafkaConsumer = kafkaConsumer;
    }

    // List Kafka Topics
    @GetMapping("/topics")
    public List<String> listTopics() throws ExecutionException, InterruptedException {
        return kafkaService.listTopics();
    }

    // Get Topic Metadata
    @GetMapping("/topics/{topic}")
    public Map<String, Object> getTopicMetadata(@PathVariable String topic) throws ExecutionException, InterruptedException {
        return kafkaService.getTopicMetadata(topic);
    }

    @PostMapping("/topics/create")
    public String createTopic(@RequestBody TopicRequest topicRequest) throws ExecutionException, InterruptedException {
        return kafkaService.createTopic(
            topicRequest.getTopicName(), 
            topicRequest.getNumPartitions(), 
            topicRequest.getReplicationFactor()
        );
    }
    // Create a Consumer Group
     @PostMapping("/consumergroups/{topic}/create")
     public String createConsumerGroup(@RequestBody String groupId, @PathVariable String topic) {
    return kafkaService.createConsumerGroup(groupId, topic);
}


    // List Consumer Groups
    @GetMapping("/consumergroups")
    public List<String> listConsumerGroups() throws ExecutionException, InterruptedException {
        return kafkaService.listConsumerGroups();
    }

    // Get Consumer Groups for a Specific Topic
    @GetMapping("/topics/{topic}/consumergroups")
    public List<String> getConsumerGroupsForTopic(@PathVariable String topic) throws ExecutionException, InterruptedException {
        return kafkaService.getConsumerGroupsForTopic(topic);
    }

    // Delete a Kafka Topic
    @DeleteMapping("/topics/{topic}/delete")
    public String deleteTopic(@PathVariable String topic) throws ExecutionException, InterruptedException {
        return kafkaService.deleteTopic(topic);
    }

    // Get Message Count in a Consumer Group for a Specific Topic
    @GetMapping("/consumergroups/{consumerGroupId}/topics/{topic}/messagecount")
    public long getMessageCountForConsumerGroup(@PathVariable String consumerGroupId, @PathVariable String topic) throws ExecutionException, InterruptedException {
        return kafkaService.getMessageCountForConsumerGroup(consumerGroupId, topic);
    }

    // Get Throughput Metrics for a Topic
    @GetMapping("/topics/{topic}/throughput")
    public Map<String, Long> getThroughputMetrics(@PathVariable String topic) {
        return kafkaService.getThroughputMetrics(topic);
    }

  // Peek Messages Without Acknowledging
@GetMapping("/topics/{topic}/peek/{numMessages}")
public ResponseEntity<List<String>> peekMessages(@PathVariable String topic, @PathVariable int numMessages) {
  
    System.out.println("Peeking into messages from topic: " + topic + ".........................................................");
    System.out.println("Number of messages to peek: " + numMessages + ".........................................................");
    try {
        List<String> messages = kafkaService.peekMessages(topic, numMessages);
        return ResponseEntity.ok(messages);
    } catch (Exception e) {
        // Log the error (you might use a logger)
        System.err.println("Error peeking messages: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonList("Error peeking messages from topic: " + topic));
    }
}
// Peek All Messages Without Acknowledging
@GetMapping("/topics/{topic}/peekall")
public ResponseEntity<List<String>> peekAllMessages(@PathVariable String topic) {
    System.out.println("Peeking into all messages from topic: " + topic + ".........................................................");
    try {
        List<String> messages = kafkaService.peekAllMessages(topic);
        return ResponseEntity.ok(messages);
    } catch (Exception e) {
        // Log the error (you might use a logger)
        System.err.println("Error peeking messages: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonList("Error peeking messages from topic: " + topic));
    }


}
}
