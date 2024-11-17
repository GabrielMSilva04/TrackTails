import React, { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

const Map = ({ petMarkersData }) => {
    const mapRef = useRef(null);

    useEffect(() => {
        // Setting zoomControl to false to hide the zoom buttons
        mapRef.current = L.map('map', {
            zoomControl: false
        }).setView([petMarkersData[0].lat, petMarkersData[0].lng], 13);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(mapRef.current);

        petMarkersData.forEach((petMarkerData) => {
            const marker = L.marker([petMarkerData.lat, petMarkerData.lng]).addTo(mapRef.current);

            const animalIcon = L.divIcon({
                className: '',
                html: `
                    <div style="
                        width: 50px;
                        height: 50px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        border-radius: 50%;
                        overflow: hidden;
                    "
                    class="border-4 border-secondary"
                    >
                        <img src="${petMarkerData.image}" alt="${petMarkerData.name}" style="
                            width: 100%;
                            height: 100%;
                            object-fit: cover;
                        " />
                    </div>
                `,
                iconSize: [50, 50],
                iconAnchor: [25, 25],
                popupAnchor: [0, -25],
            });

            marker.setIcon(animalIcon);
            marker.bindPopup(`<b>${petMarkerData.name}</b>`);
        });

        return () => {
            mapRef.current.remove();
        };
    }, [petMarkersData]);

    return <div id="map" style={{ height: '100%', width: '100%', zIndex: 0 }}></div>;
};

export default Map;


