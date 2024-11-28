import React from 'react';
import { MapContainer, TileLayer, Marker, Popup, Polygon, Polyline, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import pin from '../assets/pin.png';

function Map({ animals, fence, showFence, routeData, showRoute, addingFence, setFence, clickHandler }) {
    const MapEvents = () => {
        useMapEvents({
            click(e) {
                if (addingFence && fence.length < 4) {
                    const newFence = [...fence, e.latlng];
                    setFence(newFence);
                }
            },
        });

        return null;
    };

    // Default center position if no animals
    const centerPosition = animals.length > 0 ? [animals[0].latitude, animals[0].longitude] : [40.63316, -8.65939];

    const customIcon = L.divIcon({
        html: `
        <div style="position: relative; width: 75px; height: 70px;">
            <img src="${pin}" style="width: 100%; height: 100%;" />
            <img src="https://placedog.net/300/300" 
                 style="width: 30px; height: 30px; border-radius: 50%; position: absolute; top: 11px; left: 22.3px;" />
        </div>
    `,
        iconAnchor: [37.5, 70],
        className: '',
    });

    return (
        <MapContainer center={centerPosition} zoom={17} zoomControl={false} style={{ height: "100vh", width: "100%" }}>
            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

            {showFence && fence.length > 0 && <Polygon positions={fence} color="green" />}

            {showRoute && routeData.length > 0 && (
                <Polyline positions={routeData} color="blue" className="z-30" />
            )}

            {/* Render Animal Markers */}
            {animals.map(animal => (
                animal.latitude && animal.longitude && (
                    <Marker
                        key={animal.animalId}
                        position={[animal.latitude, animal.longitude]}
                        icon={customIcon}
                        eventHandlers={{
                            click: () => clickHandler(animal),
                        }}
                    />
                )
            ))}

            {/* Use Leaflet Events for adding a fence */}
            {addingFence && (
                <MapEvents />
            )}
        </MapContainer>
    );
}

export default Map;
