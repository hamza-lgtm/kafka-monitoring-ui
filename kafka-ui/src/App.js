import React, { useState } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import Header from './components/Header';

import Kafka from './components/Kafka';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
 

  return (
    <div className="app-container">
      <Header />
      <Container fluid className="main-container">
      
          
         
            <div className="content-header">
              <h2>Welcome to Kafka Monitor</h2>
              <p className="content-subtitle">
                Monitor and manage Kafka topics efficiently with real-time insights.
              </p>
            </div>
            <Kafka/>
            
         
       
      </Container>
    </div>
  );
}

export default App;
