import React, { useEffect, useRef, useState } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

const Map = () => {
    const petMarkerData = {
        lat: 51.505,
        lng: -0.09,
        name: 'Jack',
        species: 'Dog',
        image: 'https://placedog.net/300/300',
    };

    // Ref to store the map instance
    const mapRef = useRef(null);

    const addDynamicMarker = (map, position, image, name) => {
        const marker = L.marker(position).addTo(map);
        marker.bindPopup(`
            <b>${name}</b><br>
            <img src="${image}" width="100" height="100" alt="${name}">
        `);
        return marker;
    };

    useEffect(() => {
        mapRef.current = L.map('map').setView([petMarkerData.lat, petMarkerData.lng], 13);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(mapRef.current);

        addDynamicMarker(
            mapRef.current,
            [petMarkerData.lat, petMarkerData.lng],
            petMarkerData.image,
            petMarkerData.name
        );

        // Cleanup function to remove the map when the component unmounts
        return () => {
            mapRef.current.remove();
        };
    }, []); // Empty dependency array ensures this runs only once

    return(
        <>
            <div id="map" style={{ height: '100%', width: '100%' , zIndex: 0}} />;
        </>
    );
};

export default Map;
