import React from 'react';
import { Navbar, Container, Nav } from 'react-bootstrap';
import { FaChartBar } from 'react-icons/fa'; // Import an icon for branding

const Header = () => {
  return (
    <Navbar bg="dark" variant="dark" expand="lg" className="header-navbar">
      <Container fluid>
        <Navbar.Brand href="#" className="d-flex align-items-center">
          <FaChartBar className="me-2" size={24} /> {/* Icon beside brand */}
          UI for Kafka Monitor
        </Navbar.Brand>
       
        
      </Container>
    </Navbar>
  );
};

export default Header;

