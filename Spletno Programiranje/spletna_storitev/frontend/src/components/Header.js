import '../App.css';
import React from 'react';
import { Link } from 'react-router-dom';

function App() {

  return (
    <header>
      <nav id="header">
        <ul>
          <li><Link to="/">HOME</Link></li>
          <li><Link to="/map">MAP</Link></li>
          <li><Link to="/graphs">GRAPHS</Link></li>
          <li><Link to="/pie_charts">PIE CHARTS</Link></li>
        </ul>
      </nav>

    </header>
  );

}

export default App;