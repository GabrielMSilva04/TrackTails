import React from 'react';
import {MapContainer, TileLayer, Polygon, useMapEvents, Polyline, Marker, Popup} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import pin from '../assets/pin.png';


const customIcon = L.divIcon({
    html: `
        <div style="position: relative; width: 75px; height: 70px;">
            <img src="${pin}" style="width: 100%; height: 100%;" />
            <img src="https://placedog.net/300/300" 
                 style="width: 30px; height: 30px; border-radius: 50%; position: absolute; top: 11px; left: 22.3px;" />
        </div>
    `,
    iconAnchor: [37.5, 70], // Ajuste conforme necessário para centralizar corretamente o ícone
    className: ''
});


function Map({ fence, setFence, addingFence, showFence, showRoute, routeData }) {
    const animals = [
        {
            animalId: 1,
            weight: 40.5,
            height: 40,
            latitude: 40.63316,
            longitude: -8.65939,
            speed: 12.3,
            heartRate: 78,
            breathRate: 15,
            battery: 0.8,
            additionalTags: {
                species: "Dog",
                status: "Healthy",
            },
            timestamp: new Date().toISOString(),
        },
        {
            animalId: 2,
            weight: 55.2,
            height: 50,
            latitude: 40.73415,
            longitude: -8.37021,
            speed: 8.5,
            heartRate: 90,
            breathRate: 20,
            battery: 0.5,
            additionalTags: {
                species: "Cat",
                status: "Running",
            },
            timestamp: new Date().toISOString(),
        },
    ];

    const MapEvents = () => {
        useMapEvents({
            click(e) {
                if (addingFence) {
                    const newFence = [...fence, e.latlng];
                    setFence(newFence);
                }
            },
        });

        return null;
    };

    const centerposition = animals.length > 0 ? [animals[0].latitude, animals[0].longitude] : [40.63316, -8.65939];

    return (
        <MapContainer center={centerposition} zoom={17} zoomControl={false} style={{ height: "100vh", width: "100%" }}>
            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

            {/* Render the fence if showFence is true */}
            {showFence && fence.length > 0 && <Polygon positions={fence} color="green" />}

            {/* Render the route if showRoute is true */}
            {showRoute && routeData.length > 0 && <Polyline positions={routeData} color="green" />}

            {/* Render a marker for each animal */}
            {animals &&
                animals.map((animal) => {
                    if (animal.latitude && animal.longitude) {
                        return (
                            <Marker
                                key={animal.animalId}
                                position={[animal.latitude, animal.longitude]}
                                icon={customIcon}
                            >
                            </Marker>
                        );
                    }
                    return null;
                })}

            {/* Event listener for map interactions */}
            <MapEvents />
        </MapContainer>

    );
}

export default Map;





