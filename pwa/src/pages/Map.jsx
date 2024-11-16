import React, { useEffect, useRef, useState } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

const Map = () => {
    const petMarkersData = [
        {
            lat: 51.505,
            lng: -0.09,
            name: 'Jack',
            species: 'Dog',
            image: 'https://placedog.net/300/300',
        },
        {
            lat: 51.515,
            lng: -0.09,
            name: 'Whiskers',
            species: 'Cat',
            image: 'https://placecats.com/300/300',
        }
    ];

    // Ref to store the map instance
    const mapRef = useRef(null);

    const addDynamicMarker = (map, position, image, name) => {
        const marker = L.marker(position).addTo(map);

        const animalIcon = L.divIcon({
            className: '', // Keep it blank or add global classes if needed
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
                class="border-4 border-secondary""
                >
                    <img src="${image}" alt="${name}" style="
                        width: 100%;
                        height: 100%;
                        object-fit: cover; /* Ensure the image scales properly */
                    " />
                </div>
            `,
            iconSize: [50, 50], // Size of the marker icon
            iconAnchor: [25, 25], // Center the icon
            popupAnchor: [0, -25], // Position the popup relative to the marker
        });


        marker.bindPopup(`
            <b>${name}</b><br>
        `);
        marker.setIcon(animalIcon);

        return marker;
    };

    useEffect(() => {
        mapRef.current = L.map('map').setView([petMarkersData[1].lat, petMarkersData[1].lng], 13);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(mapRef.current);

        petMarkersData.forEach((petMarkerData) => {
            addDynamicMarker(
                mapRef.current,
                [petMarkerData.lat, petMarkerData.lng],
                petMarkerData.image,
                petMarkerData.name
            );
        });

        // Cleanup function to remove the map when the component unmounts
        return () => {
            mapRef.current.remove();
        };
    }, []);

    return(
        <>
            <div id="map" style={{ height: '100%', width: '100%' , zIndex: 0}} />;
        </>
    );
};

export default Map;
