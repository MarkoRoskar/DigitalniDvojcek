import './App.css';
import React from 'react';
import { useState, useEffect, useContext } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { Icon } from 'leaflet';
import MBajkLocations from './data/mbajk_locations.json';
import bikePathLocations from './data/bikepaths.json';

function App() {

  const position = [46.554649, 15.645881];

  return (
    <MapContainer center={position} zoom={14}>
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      />

      {MBajkLocations.locations.map(mbajk => (
        <Marker 
          key={mbajk._id} 
          position={[
            mbajk.geometry.coordinates[1],
            mbajk.geometry.coordinates[0]
          ]}>
          <Popup>
            <h2>{mbajk.name}</h2>
            <h3>{mbajk.address}</h3>
            <h3>{mbajk.currentStatus}</h3>
          </Popup>
        </Marker>
      ))}
      
    </MapContainer>
  );
}

export default App;
