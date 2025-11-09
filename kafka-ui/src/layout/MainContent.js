import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { Routes, Route } from 'react-router-dom';
import Sidebar from '../components/Sidebar';
import Topics from '../components/Kafka';
import Consumers from '../components/Consumers';
import KafkaConnect from '../components/KafkaConnect';

const MainContent = () => (
  <Container fluid>
    <Row>
      <Col xs={2}>
        <Sidebar />
      </Col>
      <Col xs={10}>
        <Routes>
          <Route path="/topics" element={<Topics />} />
          <Route path="/consumers" element={<Consumers />} />
          <Route path="/kafka-connect" element={<KafkaConnect />} />
        </Routes>
      </Col>
    </Row>
  </Container>
);

export default MainContent;
