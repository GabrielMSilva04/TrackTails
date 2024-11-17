import React, { useEffect } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

function Map() {
    const defaultCenter = [40.633039, -8.659193];
    const defaultZoom = 13;

    useEffect(() => {
        const map = L.map('map', {
            center: defaultCenter,
            zoom: defaultZoom,
            zoomControl: false
        });

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            minZoom: 2
        }).addTo(map);

        return () => {
            map.remove();
        };
    }, []);

    return (
        <div id="map" style={{ height: '100vh', width: '100%' }} />
    );
}

export default Map;
