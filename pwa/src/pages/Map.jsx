import React, { useEffect } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

const Map = () => {
    useEffect(() => {
        const map = L.map('map').setView([51.505, -0.09], 13);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(map);

        // Cleanup function to remove the map when the component unmounts
        return () => {
            map.remove();
        };
    }, []); // Empty dependency array ensures this runs only once

    return <div id="map" style={{ height: '100%', width: '100%' , zIndex: 0}} />;
};

export default Map;
