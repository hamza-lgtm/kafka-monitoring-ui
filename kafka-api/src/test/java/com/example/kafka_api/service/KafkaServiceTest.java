package com.example.kafka_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.kafka_api.DTO.TopicRequest;

import org.apache.kafka.common.KafkaFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.PartitionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class KafkaServiceTest {

    @Mock
    private AdminClient kafkaAdminClient;

    @Mock
    private KafkaConsumer<String, String> kafkaConsumer;

    @InjectMocks
    private KafkaService kafkaService;

    @Test
    public void testListTopics() throws ExecutionException, InterruptedException {
        // Mock the ListTopicsResult and TopicListing
        ListTopicsResult listTopicsResult = mock(ListTopicsResult.class);
        TopicListing topicListing = new TopicListing("test-topic", false);
        Collection<TopicListing> topicListings = Collections.singleton(topicListing);

        // Configure mocks
        when(kafkaAdminClient.listTopics()).thenReturn(listTopicsResult);
        KafkaFuture<Collection<TopicListing>> futureTopicListings = mock(KafkaFuture.class);
        when(listTopicsResult.listings()).thenReturn(futureTopicListings);
        when(futureTopicListings.get()).thenReturn(topicListings);

        // Call the method to test
        List<String> topics = kafkaService.listTopics();

        // Verify the result
        assertEquals(1, topics.size());
        assertEquals("test-topic", topics.get(0));
    }
    // @Test
    // public void testGetTopicMetadata() throws ExecutionException, InterruptedException {
    //     DescribeTopicsResult describeTopicsResult = mock(DescribeTopicsResult.class);
    //     TopicPartitionInfo partitionInfo = new TopicPartitionInfo(0, null, Collections.emptyList(), Collections.emptyList());
    //     List<TopicPartitionInfo> partitions = Collections.singletonList(partitionInfo);

    //     when(kafkaAdminClient.describeTopics(Collections.singletonList("test-topic"))).thenReturn(describeTopicsResult);
    //     when(describeTopicsResult.values().get("test-topic").get().partitions()).thenReturn(partitions);

    //     Map<String, Object> metadata = kafkaService.getTopicMetadata("test-topic");

    //     assertEquals(1, metadata.get("partitions"));
    //     assertEquals(0, metadata.get("replicationFactor"));
    // }

    // @Test
    // public void testCreateTopic() throws ExecutionException, InterruptedException {
    //     NewTopic newTopic = new NewTopic("new-topic", 1, (short) 1);
    //     CreateTopicsResult createTopicsResult = mock(CreateTopicsResult.class);

    //     when(kafkaAdminClient.createTopics(Collections.singletonList(newTopic))).thenReturn(createTopicsResult);
    //     KafkaFuture<Void> future = mock(KafkaFuture.class);
    //     when(createTopicsResult.all()).thenReturn(future);
    //     when(future.get()).thenReturn(null);

    //     String result = kafkaService.createTopic("new-topic", 1, (short) 1);

    //     assertEquals("Topic new-topic created successfully.", result);
    // }

    // @Test
    // public void testDeleteTopic() throws ExecutionException, InterruptedException {
    //     DeleteTopicsResult deleteTopicsResult = mock(DeleteTopicsResult.class);

    //     when(kafkaAdminClient.deleteTopics(Collections.singletonList("test-topic"))).thenReturn(deleteTopicsResult);
    //     KafkaFuture<Void> future = mock(KafkaFuture.class);
    //     when(deleteTopicsResult.all()).thenReturn(future);
    //     when(future.get()).thenReturn(null);

    //     String result = kafkaService.deleteTopic("test-topic");

    //     assertEquals("Topic test-topic deleted successfully.", result);
    // }

    // @Test
    // public void testListConsumerGroups() throws ExecutionException, InterruptedException {
    //     ListConsumerGroupsResult listConsumerGroupsResult = mock(ListConsumerGroupsResult.class);
    //     Collection<ConsumerGroupListing> groupListings = Collections.singleton(new ConsumerGroupListing("group1", false));

    //     when(kafkaAdminClient.listConsumerGroups()).thenReturn(listConsumerGroupsResult);
    //     KafkaFuture<Collection<ConsumerGroupListing>> futureGroupListings = mock(KafkaFuture.class);
    //     when(listConsumerGroupsResult.all()).thenReturn(futureGroupListings);
    //     when(futureGroupListings.get()).thenReturn(groupListings);

    //     List<String> consumerGroups = kafkaService.listConsumerGroups();

    //     assertEquals(1, consumerGroups.size());
    //     assertEquals("group1", consumerGroups.get(0));
    // }

    // @Test
    // public void testGetConsumerGroupsForTopic() throws ExecutionException, InterruptedException {
    //     ListConsumerGroupsResult listConsumerGroupsResult = mock(ListConsumerGroupsResult.class);
    //     DescribeConsumerGroupsResult describeConsumerGroupsResult = mock(DescribeConsumerGroupsResult.class);
    //     ConsumerGroupListing groupListing = new ConsumerGroupListing("group1", false);
    //     Collection<ConsumerGroupListing> consumerGroups = Collections.singleton(groupListing);

    //     // Mock the MemberDescription and ConsumerGroupDescription for the topic
    //     MemberDescription memberDescription = mock(MemberDescription.class);
    //     ConsumerGroupDescription groupDescription = mock(ConsumerGroupDescription.class);
    //     TopicPartition topicPartition = new TopicPartition("test-topic", 0);

    //     // Set up mock behavior
    //     when(kafkaAdminClient.listConsumerGroups()).thenReturn(listConsumerGroupsResult);
    //     KafkaFuture<Collection<ConsumerGroupListing>> futureConsumerGroups = mock(KafkaFuture.class);
    //     when(listConsumerGroupsResult.all()).thenReturn(futureConsumerGroups);
    //     when(futureConsumerGroups.get()).thenReturn(consumerGroups);
    //     when(kafkaAdminClient.describeConsumerGroups(Collections.singletonList("group1"))).thenReturn(describeConsumerGroupsResult);
    //     KafkaFuture<Map<String, ConsumerGroupDescription>> kafkaFuture = mock(KafkaFuture.class);
    //     when(kafkaFuture.get()).thenReturn(Map.of("group1", groupDescription));
    //     when(describeConsumerGroupsResult.all()).thenReturn(kafkaFuture);
        
    //     // Configure mock MemberDescription to simulate assignment to the topic partition
    //     when(groupDescription.members()).thenReturn(Collections.singleton(memberDescription));
    //     when(memberDescription.assignment()).thenReturn(new MemberAssignment(Collections.singleton(topicPartition)));

    //     // Call the method under test
    //     List<String> result = kafkaService.getConsumerGroupsForTopic("test-topic");

    //     // Assertions
    //     assertEquals(1, result.size());
    //     assertEquals("group1", result.get(0));
    // }

    // @Test
    // public void testGetMessageCountForConsumerGroup() throws ExecutionException, InterruptedException {
    //     ListConsumerGroupOffsetsResult listConsumerGroupOffsetsResult = mock(ListConsumerGroupOffsetsResult.class);
    //     Map<TopicPartition, OffsetAndMetadata> offsets = Map.of(new TopicPartition("test-topic", 0), new OffsetAndMetadata(100));

    //     when(kafkaAdminClient.listConsumerGroupOffsets("group1")).thenReturn(listConsumerGroupOffsetsResult);
    //     when(listConsumerGroupOffsetsResult.partitionsToOffsetAndMetadata()).thenReturn(KafkaFuture.completedFuture(offsets));

    //     long messageCount = kafkaService.getMessageCountForConsumerGroup("group1", "test-topic");

    //     assertEquals(100, messageCount);
    // }

    // @Test
    // public void testGetThroughputMetrics() {
    //     PartitionInfo partitionInfo = new PartitionInfo("test-topic", 0, null, null, null);
    //     when(kafkaConsumer.partitionsFor("test-topic")).thenReturn(Collections.singletonList(partitionInfo));
    //     TopicPartition topicPartition = new TopicPartition("test-topic", 0);
    //     when(kafkaConsumer.position(topicPartition)).thenReturn(100L);

    //     Map<String, Long> metrics = kafkaService.getThroughputMetrics("test-topic");

    //     assertEquals(100, metrics.get("messagesPerSecond"));
    //     assertEquals(100 * 100, metrics.get("bytesPerSecond"));
    // }

    // @Test
    // public void testPeekMessages() {
    //     ConsumerRecords<String, String> records = mock(ConsumerRecords.class);
    //     ConsumerRecord<String, String> record = new ConsumerRecord<>("test-topic", 0, 0, "key", "message");

    //     when(kafkaConsumer.poll(Duration.ofMillis(100))).thenReturn(records);
    //     when(records.isEmpty()).thenReturn(false);
    //     when(records.iterator()).thenReturn(Collections.singletonList(record).iterator());

    //     List<String> messages = kafkaService.peekMessages("test-topic", 1);

    //     assertEquals(1, messages.size());
    //     assertEquals("message", messages.get(0));
    // }

    // @Test
    // public void testPeekAllMessages() {
    //     ConsumerRecords<String, String> records = mock(ConsumerRecords.class);
    //     ConsumerRecord<String, String> record1 = new ConsumerRecord<>("test-topic", 0, 0, "key1", "message1");
    //     ConsumerRecord<String, String> record2 = new ConsumerRecord<>("test-topic", 0, 1, "key2", "message2");

    //     when(kafkaConsumer.poll(Duration.ofMillis(500)))
    //         .thenReturn(records)
    //         .thenReturn(records);
    //     when(records.isEmpty()).thenReturn(false).thenReturn(true);
    //     when(records.iterator()).thenReturn(Arrays.asList(record1, record2).iterator());

    //     List<String> messages = kafkaService.peekAllMessages("test-topic");

    //     assertEquals(2, messages.size());
    //     assertEquals("message1", messages.get(0));
    //     assertEquals("message2", messages.get(1));
    // }

    // private static <T> CompletableFuture<T> completedFuture(T value) {
    //     return CompletableFuture.completedFuture(value);
    // }
    // @Test
    // public void testCreateConsumerGroup() {
    //     // Arrange
    //     String groupId = "test-group";
    //     String topic = "test-topic";
        
    //     // Mock the behavior of the consumer
    //     doNothing().when(kafkaConsumer).subscribe(Collections.singletonList(topic));
    //     when(kafkaConsumer.poll(Duration.ofMillis(100))).thenReturn(null); // Simulate poll for subscription

    //     // Act
    //     String result = kafkaService.createConsumerGroup(groupId, topic);

    //     // Assert
    //     assertEquals("Consumer group " + groupId + " created and subscribed to topic " + topic, result);

    //     // Verify that the consumer subscribed to the specified topic
    //     verify(kafkaConsumer).subscribe(Collections.singletonList(topic));
    //     verify(kafkaConsumer).poll(Duration.ofMillis(100));
    // }
}