package com.example.kafka_api.controller;

import com.example.kafka_api.DTO.TopicRequest;
import com.example.kafka_api.service.KafkaService;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KafkaController.class)
public class KafkaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaService kafkaService;

    @MockBean
    private KafkaConsumer<String, String> kafkaConsumer;  // Add this line

    // Test the /topics endpoint
    @Test
    public void testListTopics() throws Exception {
        when(kafkaService.listTopics()).thenReturn(List.of("topic1", "topic2"));

        mockMvc.perform(get("/kafka/topics"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0]", is("topic1")))
            .andExpect(jsonPath("$[1]", is("topic2")));
    }

    // Test the /topics/{topic} endpoint
    @Test
    public void testGetTopicMetadata() throws Exception {
        Map<String, Object> metadata = Map.of("partitions", 3, "replicationFactor", 1);
        when(kafkaService.getTopicMetadata("topic1")).thenReturn(metadata);

        mockMvc.perform(get("/kafka/topics/topic1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.partitions", is(3)))
            .andExpect(jsonPath("$.replicationFactor", is(1)));
    }

    // Test the /topics/create endpoint
    @Test
    public void testCreateTopic() throws Exception {
        TopicRequest topicRequest = new TopicRequest("newTopic", 3, (short) 1);
        when(kafkaService.createTopic(topicRequest.getTopicName(), topicRequest.getNumPartitions(), topicRequest.getReplicationFactor()))
            .thenReturn("Topic created successfully.");

        mockMvc.perform(post("/kafka/topics/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"topicName\":\"newTopic\",\"numPartitions\":3,\"replicationFactor\":1}"))
            .andExpect(status().isOk())
            .andExpect(content().string("Topic created successfully."));
    }

    // Test the /consumergroups endpoint
    @Test
    public void testListConsumerGroups() throws Exception {
        when(kafkaService.listConsumerGroups()).thenReturn(List.of("group1", "group2"));

        mockMvc.perform(get("/kafka/consumergroups"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0]", is("group1")))
            .andExpect(jsonPath("$[1]", is("group2")));
    }

    // Test the /topics/{topic}/consumergroups endpoint
    @Test
    public void testGetConsumerGroupsForTopic() throws Exception {
        when(kafkaService.getConsumerGroupsForTopic("topic1")).thenReturn(List.of("group1"));

        mockMvc.perform(get("/kafka/topics/topic1/consumergroups"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0]", is("group1")));
    }

    // Test the /topics/{topic}/delete endpoint
    @Test
    public void testDeleteTopic() throws Exception {
        when(kafkaService.deleteTopic("topic1")).thenReturn("Topic topic1 deleted successfully.");

        mockMvc.perform(delete("/kafka/topics/topic1/delete"))
            .andExpect(status().isOk())
            .andExpect(content().string("Topic topic1 deleted successfully."));
    }

    // Test the /consumergroups/{consumerGroupId}/topics/{topic}/messagecount endpoint
    @Test
    public void testGetMessageCountForConsumerGroup() throws Exception {
        when(kafkaService.getMessageCountForConsumerGroup("group1", "topic1")).thenReturn(100L);

        mockMvc.perform(get("/kafka/consumergroups/group1/topics/topic1/messagecount"))
            .andExpect(status().isOk())
            .andExpect(content().string("100"));
    }

    // Test the /topics/{topic}/throughput endpoint
    @Test
    public void testGetThroughputMetrics() throws Exception {
        Map<String, Long> metrics = Map.of("messagesPerSecond", 50L, "bytesPerSecond", 5000L);
        when(kafkaService.getThroughputMetrics("topic1")).thenReturn(metrics);

        mockMvc.perform(get("/kafka/topics/topic1/throughput"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.messagesPerSecond", is(50)))
            .andExpect(jsonPath("$.bytesPerSecond", is(5000)));
    }

    // Test the /topics/{topic}/peek/{numMessages} endpoint
    @Test
    public void testPeekMessages() throws Exception {
        when(kafkaService.peekMessages("topic1", 2)).thenReturn(List.of("message1", "message2"));

        mockMvc.perform(get("/kafka/topics/topic1/peek/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0]", is("message1")))
            .andExpect(jsonPath("$[1]", is("message2")));
    }

    // Test the /topics/{topic}/peekall endpoint
    @Test
    public void testPeekAllMessages() throws Exception {
        when(kafkaService.peekAllMessages("topic1")).thenReturn(List.of("message1", "message2", "message3"));

        mockMvc.perform(get("/kafka/topics/topic1/peekall"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0]", is("message1")))
            .andExpect(jsonPath("$[1]", is("message2")))
            .andExpect(jsonPath("$[2]", is("message3")));
    }
}
