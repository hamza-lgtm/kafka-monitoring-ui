package com.example.kafka_api.DTO;

public class TopicRequest {
    private String topicName;
    private int numPartitions;
    private short replicationFactor;

    public TopicRequest() {
    }

    public TopicRequest(String topicName, int numPartitions, short replicationFactor) {
        this.topicName = topicName;
        this.numPartitions = numPartitions;
        this.replicationFactor = replicationFactor;
    }   

    // Getters and Setters
    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getNumPartitions() {
        return numPartitions;
    }

    public void setNumPartitions(int numPartitions) {
        this.numPartitions = numPartitions;
    }

    public short getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(short replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    // toString
    
}

