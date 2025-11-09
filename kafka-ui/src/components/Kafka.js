import React, { useState, useEffect } from 'react';
import { listTopics,peekMessages, getTopicMetadata,getMessageCountForConsumerGroup,peekAllMessages, getThroughputMetrics, getConsumerGroupsForTopic, createTopic ,createConsumerGroup,listConsumerGroups} from '../services/kafkaService';
import {
  AppBar,
  Tabs,
  Tab,
  Typography,
  CircularProgress,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Box,
  Toolbar,
  Button,
  Drawer,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  IconButton
  
} from "@mui/material";
import AddIcon from '@mui/icons-material/Add';

import GroupAddIcon from '@mui/icons-material/GroupAdd';

const Kafka = () => {
  const [topics, setTopics] = useState([]);
  const [loading, setLoading] = useState(false);
  const [detailedTopics, setDetailedTopics] = useState([]);
  const [error, setError] = useState(null);
  const [drawerOpenConsumer, setDrawerOpenConsumer] = useState(false);
  const [drawerOpenTopic, setDrawerOpenTopic] = useState(false);
  const [selectedTopic, setSelectedTopic] = useState("");
  const [consumerGroupId, setConsumerGroupId] = useState("");
  const [newTopicName, setNewTopicName] = useState("");
  const [numPartitions, setNumPartitions] = useState(1);
  const [replicationFactor, setReplicationFactor] = useState(1);
  const [consumerGroups, setConsumerGroups] = useState([]);
  const [selectedTab, setSelectedTab] = useState(0);

  const [peekedMessages, setPeekedMessages] = useState([]);

  const [peekError, setPeekError] = useState(null);
  const [selectedPeekTopic, setSelectedPeekTopic] = useState("");

  const handleTabChange = (event, newValue) => {
    setSelectedTab(newValue);
  };
  useEffect(() => {
    const getTopics = async () => {
      try {
        setLoading(true);
        const topicNames = await listTopics();
        setTopics(topicNames);
        const consumerGroups = await listConsumerGroups();
        console.log('Consumer Groups:', consumerGroups);

        const fetchedDetails = [];
        for (const topicName of topicNames) {
          try {
            const topicDetails = await getTopicMetadata(topicName);
            const throughput = await getThroughputMetrics(topicName);
            const consumerGroups = await getConsumerGroupsForTopic(topicName);

const slicedConsumerGroups = await Promise.all(
    consumerGroups.map(async (group) => {
        const slicedGroup = group.slice(12, -2); // Adjust the slicing as needed
        const messageCount = await getMessageCountForConsumerGroup(group, topicName); // Await message count

        return {
            name: slicedGroup,
            messagecount: messageCount,
        };
    })
);

           


            console.log(`Fetched details for topic: ${topicName}`, topicDetails, throughput, slicedConsumerGroups);

            fetchedDetails.push({
              name: topicName,
              partitions: topicDetails.partitions,
              replicationFactor: topicDetails.replicationFactor,
              messagesPerSecond: throughput.messagesPerSecond,
              bytesPerSecond: throughput.bytesPerSecond,
              consumerGroups: slicedConsumerGroups,
            });
          } catch (err) {
            console.error(`Error fetching data for topic ${topicName}:`, err);
            fetchedDetails.push({
              name: topicName,
              partitions: 'N/A',
              replicationFactor: 'N/A',
              messagesPerSecond: 'N/A',
              bytesPerSecond: 'N/A',
              consumerGroups: [],
            });
          }
        }

        setDetailedTopics(fetchedDetails);
      } catch (err) {
        console.error('Error fetching topics:', err);
        setError('Failed to load topics. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    getTopics();
  }, []);

  const handleAddConsumer = async () => {
 
    // Call the function to actually add the consumer using the API
    // Reset fields and close the drawer
    try {
       const response = await createConsumerGroup(consumerGroupId, selectedTopic);
      window.alert(`${response}`);
      window.location.reload();
      setDrawerOpenConsumer(false);
      setConsumerGroupId("");
    }
    catch (err) {
      console.error("Error creating consumer group:", err);
      setError("Failed to create consumer group. Please try again.");
    }
    
  };

  const handleCreateTopic = async () => {
    try {
      
      const response = await createTopic(newTopicName, numPartitions, replicationFactor);
      window.alert(`Created topic: ${response}`);
      window.location.reload();
      console.log(`Created topic: ${response}`);
      
      // Refresh topics after creation
      // const updatedTopics = await listTopics();
      // setTopics(updatedTopics);
      setDrawerOpenTopic(false);
      setNewTopicName("");
      setNumPartitions(1);
      setReplicationFactor(1);
    } catch (err) {
      console.error("Error creating topic:", err);
      setError("Failed to create topic. Please try again.");
    }
  };
  const handlePeekMessages = async (topic,numMessagesToPeek ) => {
    try {
      setLoading(true);
      const messages = await peekAllMessages(topic);
      setPeekedMessages(messages);
      setSelectedPeekTopic(topic);
      setPeekError(null);
    } catch (error) {
      console.error("Error peeking messages:", error);
      setPeekError("Failed to peek messages. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Paper sx={{ width: "100%", overflow: "hidden", padding: 2 }}>
     
      <Toolbar
      sx={{
        backgroundColor: "#f5f5f5",
        padding: "8px 24px",
        boxShadow: "0px 2px 4px rgba(0, 0, 0, 0.1)",
        borderBottom: "1px solid #e0e0e0",
      }}
    >
      <Typography variant="h6" sx={{ flexGrow: 1, fontWeight: "bold", color: "#333" }}>
        Topics
      </Typography>
      <Box display="flex" gap={1.5} alignItems="center">
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          onClick={() => setDrawerOpenTopic(true)} // Opens the Create Topic drawer
          sx={{ textTransform: "none", fontWeight: "medium" }}
        >
          Topic
        </Button>
        <Button
          variant="contained"
          color="secondary"
          startIcon={<GroupAddIcon />}
          onClick={() => setDrawerOpenConsumer(true)} // Opens the Add Consumer drawer
          sx={{ textTransform: "none", fontWeight: "medium" }}
        >
        Consumer Group
        </Button>
      </Box>
    </Toolbar>

    <TableContainer sx={{ maxHeight: "80vh", boxShadow: 3, borderRadius: 2 }}>
  {loading ? (
    <Box display="flex" justifyContent="center" alignItems="center" sx={{ padding: "20px" }}>
      <CircularProgress />
    </Box>
  ) : error ? (
    <Typography color="error" variant="body2" align="center" sx={{ margin: 2 }}>
      {typeof error === "string" ? error : JSON.stringify(error)}
    </Typography>
  ) : detailedTopics.length > 0 ? (
    <Table stickyHeader>
      <TableHead>
        <TableRow>
          {["Topic Name", "Partitions", "Replication Factor", "Messages per Second", "Bytes per Second", "Consumer Groups", "Message Count", "Actions"].map((header) => (
            <TableCell align="center" key={header} sx={{ fontWeight: "bold", backgroundColor: "#f5f5f5" }}>
              {header}
            </TableCell>
          ))}
        </TableRow>
      </TableHead>
      <TableBody>
        {detailedTopics.map((topic, index) => (
          <TableRow key={topic.name} sx={{ backgroundColor: index % 2 === 0 ? "#fafafa" : "#fff" }}>
            <TableCell align="center">{topic.name}</TableCell>
            <TableCell align="center">{topic.partitions}</TableCell>
            <TableCell align="center">{topic.replicationFactor}</TableCell>
            <TableCell align="center">{topic.messagesPerSecond}</TableCell>
            <TableCell align="center">{`${topic.bytesPerSecond} Bytes/sec`}</TableCell>
            <TableCell align="center">
              {topic.consumerGroups.length > 0 ? (
                topic.consumerGroups.map((group, idx) => (
                  <Typography key={idx} variant="body2">
                    {group?.name}
                  </Typography>
                ))
              ) : (
                <Typography variant="body2" color="textSecondary">
                  No Groups
                </Typography>
              )}
            </TableCell>
            <TableCell align="center">
              {topic.consumerGroups.map((group, idx) => (
                <Typography key={idx} variant="body2">
                  {group?.messagecount}
                </Typography>
              ))}
            </TableCell>
            <TableCell align="center">
              <Button
                variant="contained"
                color="primary"
                size="small"
                onClick={() => handlePeekMessages(topic.name, topic.messagesPerSecond)}
                sx={{ textTransform: "none", boxShadow: 1 }}
              >
                Peek Messages
              </Button>
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  ) : (
    <Typography variant="body2" align="center" sx={{ margin: 2, color: "text.secondary" }}>
      No data available
    </Typography>
  )}
</TableContainer>


      {/* Drawer for Adding a New Consumer */}
<Drawer anchor="right" open={drawerOpenConsumer} onClose={() => setDrawerOpenConsumer(false)}>
  <Box sx={{ width: 350, padding: 3, backgroundColor: "#f9f9f9" }}>
    <Typography variant="h5" sx={{ marginBottom: 2, color: "#1976d2" }}>
      Add New Consumer
    </Typography>
    <FormControl fullWidth sx={{ marginBottom: 3 }}>
      <InputLabel id="topic-select-label">Select Topic</InputLabel>
      <Select
        labelId="topic-select-label"
        id="topic-select"
        value={selectedTopic}
        onChange={(e) => setSelectedTopic(e.target.value)}
        label="Select Topic"
      >
        {topics.map((topic) => (
          <MenuItem key={topic} value={topic}>
            {topic}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
    <TextField
      label="Consumer Group ID"
      variant="outlined"
      fullWidth
      value={consumerGroupId}
      onChange={(e) => setConsumerGroupId(e.target.value)}
      sx={{ marginBottom: 3 }}
    />
    <Button variant="contained" color="primary" onClick={handleAddConsumer} fullWidth>
      Add Consumer
    </Button>
  </Box>
</Drawer>

{/* Drawer for Creating a New Topic */}
<Drawer anchor="right" open={drawerOpenTopic} onClose={() => setDrawerOpenTopic(false)}>
  <Box sx={{ width: 350, padding: 3, backgroundColor: "#f9f9f9" }}>
    <Typography variant="h5" sx={{ marginBottom: 2, color: "#1976d2" }}>
      Create New Topic
    </Typography>
    <TextField
      label="Topic Name"
      variant="outlined"
      fullWidth
      value={newTopicName}
      onChange={(e) => setNewTopicName(e.target.value)}
      sx={{ marginBottom: 3 }}
    />
    <TextField
      label="Number of Partitions"
      variant="outlined"
      type="number"
      fullWidth
      value={numPartitions}
      onChange={(e) => setNumPartitions(Number(e.target.value))}
      sx={{ marginBottom: 3 }}
    />
    <TextField
      label="Replication Factor"
      variant="outlined"
      type="number"
      fullWidth
      value={replicationFactor}
      onChange={(e) => setReplicationFactor(Number(e.target.value))}
      sx={{ marginBottom: 3 }}
    />
    <Button variant="contained" color="primary" onClick={handleCreateTopic} fullWidth>
      Create Topic
    </Button>
  </Box>
</Drawer>

{/* Drawer to Show Peeked Messages */}
<Drawer
  anchor="right"
  open={selectedPeekTopic !== ""}
  onClose={() => {
    setSelectedPeekTopic("");
    setPeekedMessages([]);
  }}
>
  <Box sx={{ width: 400, padding: 3, backgroundColor: "#f9f9f9" }}>
    <Typography variant="h5" sx={{ marginBottom: 2, color: "#1976d2" }}>
      Messages for {selectedPeekTopic}
    </Typography>
    {peekError ? (
      <Typography color="error">{peekError}</Typography>
    ) : peekedMessages.length > 0 ? (
      peekedMessages.map((msg, index) => (
        <Box
          key={index}
          sx={{
            marginBottom: 2,
            padding: 2,
            backgroundColor: "#fff",
            borderRadius: 1,
            boxShadow: "0px 1px 3px rgba(0, 0, 0, 0.2)",
          }}
        >
          <Typography variant="body2" color="text.secondary">
            {msg}
          </Typography>
        </Box>
      ))
    ) : (
      <Typography variant="body2" color="text.secondary">
        No messages available.
      </Typography>
    )}
  </Box>
</Drawer>

    </Paper>
  );
};

export default Kafka;
