package com.example.kafka_api.service;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.stereotype.Service;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class KafkaService {

    private final AdminClient kafkaAdminClient;
    private final KafkaConsumer<String, String> kafkaConsumer;
    private final AdminClient adminClient;

    public KafkaService(AdminClient kafkaAdminClient, KafkaConsumer<String, String> kafkaConsumer) {
        this.kafkaAdminClient = kafkaAdminClient;
       
        this.kafkaConsumer = kafkaConsumer;
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        this.adminClient = AdminClient.create(config);
    }

    // List Kafka Topics
    public List<String> listTopics() throws ExecutionException, InterruptedException {
        Collection<TopicListing> topics = kafkaAdminClient.listTopics().listings().get();
        List<String> topicNames = new ArrayList<>();
        for (TopicListing topic : topics) {
            topicNames.add(topic.name());
        }
        return topicNames;
    }

    // Get topic metadata (partitions, replication factor)
    public Map<String, Object> getTopicMetadata(String topic) throws ExecutionException, InterruptedException {
        Map<String, Object> metadata = new HashMap<>();
        List<TopicPartitionInfo> partitions = kafkaAdminClient.describeTopics(Collections.singletonList(topic)).values().get(topic).get().partitions();
        metadata.put("partitions", partitions.size());
        if (!partitions.isEmpty()) {
            metadata.put("replicationFactor", partitions.get(0).replicas().size());
        }
        return metadata;
    }

    // Create a new Kafka topic
    public String createTopic(String topicName, int numPartitions, short replicationFactor) throws ExecutionException, InterruptedException {
        // Adjust replication factor if only one broker is available
        if (replicationFactor > 1) {
            replicationFactor = 1;
        }
        NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
        kafkaAdminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        return "Topic " + topicName + " created successfully.";
    }

    // Delete a Kafka topic
    public String deleteTopic(String topicName) throws ExecutionException, InterruptedException {
        kafkaAdminClient.deleteTopics(Collections.singletonList(topicName)).all().get();
        return "Topic " + topicName + " deleted successfully.";
    }

    // List Kafka Consumer Groups
    public List<String> listConsumerGroups() throws ExecutionException, InterruptedException {
        Collection<ConsumerGroupListing> consumerGroups = kafkaAdminClient.listConsumerGroups().all().get();
        List<String> consumerGroupNames = new ArrayList<>();
        for (ConsumerGroupListing group : consumerGroups) {
            consumerGroupNames.add(group.groupId());
        }
        return consumerGroupNames;
    }

    
    // Get Consumer Groups associated with a specific topic
public List<String> getConsumerGroupsForTopic(String topic) throws ExecutionException, InterruptedException {
    List<String> consumerGroupsForTopic = new ArrayList<>();

        // Step 1: Retrieve all consumer groups
        Collection<ConsumerGroupListing> consumerGroups = adminClient.listConsumerGroups().all().get();

        // Step 2: Check each group's subscription
        for (ConsumerGroupListing groupListing : consumerGroups) {
           
            String groupId = groupListing.groupId();
            

            // Get details for each consumer group
            ConsumerGroupDescription groupDescription = adminClient.describeConsumerGroups(Collections.singletonList(groupId))
                    .all().get().get(groupId);
           

            // Check if any member in the group is subscribed to the topic
            boolean isSubscribedToTopic = groupDescription.members().stream()
                    .anyMatch(member -> member.assignment().topicPartitions().stream()
                            .anyMatch(tp -> tp.topic().equals(topic)));

            if (isSubscribedToTopic) {
                consumerGroupsForTopic.add(groupId);
            }
        }

        return consumerGroupsForTopic;
}

    // Create and associate a Consumer Group with a specific topic
public String createConsumerGroup(String groupId, String topic) {
    Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.name().toLowerCase());

    Consumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singletonList(topic));
    consumer.poll(Duration.ofMillis(100)); // Ensures subscription
    // write message in the topic 
    // Generate and send random messages to the topic
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (Producer<String, String> producer = new KafkaProducer<>(producerProps)) {
            Random random = new Random();
            for (int i = 1; i <= 10; i++) { // Send 10 random messages
                String randomMessage = "Random message #" + i + " - " + random.nextInt(100);
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key" + i, randomMessage);
                producer.send(record);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        

   

   
    return "Consumer group " + groupId.substring(12, groupId.length() -2) + " created and subscribed to topic " + topic;
        
    
}


    // Get message count in a Consumer Group for a specific topic
    public long getMessageCountForConsumerGroup(String consumerGroupId, String topic) throws ExecutionException, InterruptedException {
        Map<TopicPartition, OffsetAndMetadata> offsets = kafkaAdminClient.listConsumerGroupOffsets(consumerGroupId).partitionsToOffsetAndMetadata().get();
        long totalMessages = 0;

        for (TopicPartition tp : offsets.keySet()) {
            if (tp.topic().equals(topic)) {
                totalMessages += offsets.get(tp).offset();
            }
        }
        return totalMessages;
    }

    // Get Throughput Metrics
    public Map<String, Long> getThroughputMetrics(String topic) {
        Map<String, Long> throughputMetrics = new HashMap<>();
        long totalMessages = 0;
        long totalBytes = 0;

        for (PartitionInfo partition : kafkaConsumer.partitionsFor(topic)) {
            TopicPartition tp = new TopicPartition(partition.topic(), partition.partition());
            kafkaConsumer.assign(Collections.singletonList(tp));
            kafkaConsumer.seekToEnd(Collections.singletonList(tp));
            long currentOffset = kafkaConsumer.position(tp);
            totalMessages += currentOffset;

            // Assuming average message size is 100 bytes for basic calculation
            totalBytes += currentOffset * 100;
        }

        throughputMetrics.put("messagesPerSecond", totalMessages);
        throughputMetrics.put("bytesPerSecond", totalBytes);
        return throughputMetrics;
    }

   // Peek into messages without acknowledging
public List<String> peekMessages(String topic, int numMessages) {
    List<String> messages = new ArrayList<>();
    try {
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        kafkaConsumer.poll(Duration.ofMillis(100)); // Initial poll to confirm subscription

        // Poll until we have the desired number of messages or a timeout
        while (messages.size() < numMessages) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            records.forEach(record -> {
                if (messages.size() < numMessages) {
                    messages.add(record.value());
                }
            });
            
            if (records.isEmpty()) {
                // Break if no more messages are available
                break;
            }
        }
    } catch (Exception e) {
        // Log error (could use a proper logging framework like SLF4J)
        System.err.println("Error fetching messages from Kafka: " + e.getMessage());
    } finally {
        kafkaConsumer.unsubscribe(); // Ensure we unsubscribe after peeking
    }

    System.out.println("Peeked messages: " + messages + ".........................................................");

    
    return messages;
}
public List<String> peekAllMessages(String topic) {
    List<String> messages = new ArrayList<>();
    
    try {
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        // kafkaConsumer.poll(Duration.ofMillis(1000)); 
        // Thread.sleep(9000);

        // Continuously poll until no more messages are available
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(500));
            System.out.println("Peeked messages: " + records + ".........................................................");
            
            if (records.isEmpty()) {
                // Break if no more messages are available
                
                
                break;
            }

            records.forEach(record -> messages.add(record.value()));
        }
    } catch (Exception e) {
        System.err.println("Error fetching messages from Kafka: " + e.getMessage());
    } 
    finally {
        kafkaConsumer.unsubscribe(); // Ensure we unsubscribe after peeking
    }

    System.out.println("Peeked all messages: " + messages);
    return messages;
}
}
