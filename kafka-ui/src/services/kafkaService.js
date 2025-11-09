

// kafkaService.js

const BASE_URL = "http://localhost:8080/kafka";

// List Kafka Topics
export const listTopics = async () => {
    const response = await fetch(`${BASE_URL}/topics`);
    if (!response.ok) throw new Error("Failed to fetch topics");
    return response.json();
};

// Get Topic Metadata
export const getTopicMetadata = async (topic) => {
    const response = await fetch(`${BASE_URL}/topics/${topic}`);
    if (!response.ok) throw new Error("Failed to fetch topic metadata");
    return response.json();
};

export const createTopic = async (topicName, numPartitions, replicationFactor) => {
  const response = await fetch(`${BASE_URL}/topics/create`, {
      method: "POST",
      headers: {
          "Content-Type": "application/json",
      },
      body: JSON.stringify({
          topicName: topicName,
          numPartitions: numPartitions,
          replicationFactor: replicationFactor,
      }),
  });

  if (!response.ok) throw new Error("Failed to create topic");
  return response.text();
};

// delete a topic
export const deleteTopic = async (topic) => {
    const response = await fetch(`${BASE_URL}/topics/${topic}/delete`, {
        method: "DELETE",
    });
    if (!response.ok) throw new Error("Failed to delete topic");
    return response.text();
};


// Create a Consumer Group
export const createConsumerGroup = async (groupId, topic) => {
    const response = await fetch(`${BASE_URL}/consumergroups/${topic}/create`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            groupId: groupId,
        }),

    });
    if (!response.ok) throw new Error("Failed to create consumer group");
    return response.text();
};

// List Consumer Groups
export const listConsumerGroups = async () => {
    const response = await fetch(`${BASE_URL}/consumergroups`);
    if (!response.ok) throw new Error("Failed to fetch consumer groups");
    return response.json();
};

// Get Consumer Groups for a Specific Topic
export const getConsumerGroupsForTopic = async (topic) => {
    const response = await fetch(`${BASE_URL}/topics/${topic}/consumergroups`);
    if (!response.ok) throw new Error("Failed to fetch consumer groups for topic");
    return response.json();
};

// Get Message Count in a Consumer Group for a Specific Topic
export const getMessageCountForConsumerGroup = async (consumerGroupId, topic) => {
    const response = await fetch(`${BASE_URL}/consumergroups/${consumerGroupId}/topics/${topic}/messagecount`);
    if (!response.ok) throw new Error("Failed to fetch message count");
    return response.json();
};

// Get Throughput Metrics for a Topic
export const getThroughputMetrics = async (topic) => {
    const response = await fetch(`${BASE_URL}/topics/${topic}/throughput`);
    if (!response.ok) throw new Error("Failed to fetch throughput metrics");
    return response.json();
};

// Peek Messages Without Acknowledging
export const peekMessages = async (topic, numMessages) => {
    const response = await fetch(`${BASE_URL}/topics/${topic}/peek/${numMessages}`);
    if (!response.ok) throw new Error("Failed to peek messages");
    return response.json();
};

export const peekAllMessages = async (topic) => {
    const response = await fetch(`${BASE_URL}/topics/${topic}/peekall`);
    if (!response.ok) throw new Error("Failed to peek messages");
    return response.json();
};
