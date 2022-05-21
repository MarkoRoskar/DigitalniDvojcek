import './App.css';
import React from 'react';
import { useState, useEffect, useContext } from 'react';
import { MapContainer, TileLayer, Marker, Popup, LayersControl } from 'react-leaflet';
import { Icon } from 'leaflet';
import MBajkLocations from './data/mbajk_locations.json';
import bikePathLocations from './data/bikepaths.json';
import bikeStandLocations from './data/bikestand_locations.json';
// displaying remote json
import useSwr from 'swr';
import { Button } from 'antd';
import MapButton from './components/MapButton';
import Map from './components/Map';
import { BrowserRouter, Link, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import HomePage from './components/HomePage';
import Graphs from './components/Graphs';
import PieChart from './components/PieChart';



function App() {

  return (
    <BrowserRouter>
      <Header/>
      <Routes>
        <Route path="/" element={<HomePage/>}/>
        <Route path="/map" element={<Map/>}/>
        <Route path="/graphs" element={<Graphs/>}/>
        <Route path="/pie_charts" element={<PieChart/>}/>
      </Routes>
    </BrowserRouter>
  );

}

export default App;

/*
      {MBajkLocations.locations.map(mbajk => (
        <Marker 
          key={mbajk._id} 
          position={[
            mbajk.geometry.coordinates[1],
            mbajk.geometry.coordinates[0]
          ]}
          icon={mbajkLogo}>
          <Popup>
            <h2>{mbajk.name}</h2>
            <h3>{mbajk.address}</h3>
            <h3>{mbajk.currentStatus}</h3>
          </Popup>
        </Marker>
      ))}
*/