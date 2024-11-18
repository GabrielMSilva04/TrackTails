import React from 'react';
import { MapContainer, TileLayer, Polygon, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

function Map({ fence, setFence, addingFence, showFence }) {
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

    return (
        <MapContainer center={[40.7128, -74.0060]} zoom={13} style={{ height: '500px', width: '100%' }}>
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {showFence && fence.length > 0 && (
                <Polygon positions={fence} color="green" />
            )}
            <MapEvents />
        </MapContainer>
    );
}

export default Map;





