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


const mbajkIcon = new Icon({
  iconUrl: "/mbajk_logo.svg",
  iconSize: [35, 35]
});

const bikestandIcon = new Icon({
  iconUrl: "/bikestand_icon.png",
  iconSize: [45, 45]
});

const fetcher = (...args) => fetch(...args).then(response => response.json());


function App() {

  const position = [46.554649, 15.645881];

  const url = "https://api.jcdecaux.com/vls/v3/stations?apiKey=frifk0jbxfefqqniqez09tw4jvk37wyf823b5j1i&contract=maribor";
  const { data, error } = useSwr(url, { fetcher });

  const mbajk_locations = data && !error ? data : [];

  const center = [51.505, -0.09];

  return (

    <div>
      <form id="filter-form">
        <table>
          <tr>
            <td><input type="checkbox" id="mbajk" name="mbajk" value="mbajk"></input></td>
            <td><label>MBAJK LOCATIONS</label></td>
          </tr>
          <tr>
            <td><input type="checkbox" id="bike-stand" name="bike-stand" value="bike-stand"></input></td>
            <td><label>BIKE STANDS</label></td>
          </tr>
          <tr>
            <td><input type="checkbox" id="bike-shed" name="bike-shed" value="bike-shed"></input></td>
            <td><label>BIKE SHEDS</label></td>
          </tr>
          <tr>
            <td><input type="checkbox" id="bike-path" name="bike-path" value="bike-path"></input></td>
            <td><label>BIKE PATHS</label></td>
          </tr>
          <tr>
            <td><input type="checkbox" id="tour-path" name="tour-path" value="tour-path"></input></td>
            <td><label>TOUR PATHS</label></td>
          </tr>
          <tr>
            <td><input type="checkbox" id="coridor" name="coridor" value="coridor"></input></td>
            <td><label>CORIDORS</label></td>
          </tr>
        </table>
      </form>
      
      <MapContainer center={position} zoom={14} scrollWheelZoom={true} id="map-div">
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />



        <LayersControl position="topright">
          <LayersControl.Overlay name="mbajk locations">
            <Marker position={center}>
            </Marker>
          </LayersControl.Overlay>
        </LayersControl>
          

        {mbajk_locations.map(location => (
          <Marker
            key={location.number} 
            position={[location.position.latitude, location.position.longitude]}
            icon={mbajkIcon}>
            <Popup>
              <h2>{location.name}</h2>
              <h3>{location.address}</h3>
              <h3>{location.status}</h3>
            </Popup>
          </Marker>
        ))}

        {bikeStandLocations.locations.map(location => (
          <Marker
            key={location.name}
            position={[location.latitude, location.longitude]}
            icon={bikestandIcon}>
            <Popup>
              <h2>{location.name}</h2>
              <h3>kapacitete: {location.parkSpots}</h3>
            </Popup>
          </Marker>
        ))}


      </MapContainer>
    </div>
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