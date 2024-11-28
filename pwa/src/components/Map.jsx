import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import pin from '../assets/pin.png';

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

function Map({ animals }) {
    const centerPosition = animals.length > 0 ? [animals[0].latitude, animals[0].longitude] : [40.63316, -8.65939];

    return (
        <MapContainer center={centerPosition} zoom={17} zoomControl={false} style={{ height: "100vh", width: "100%" }}>
            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
            {animals.map(animal => (
                animal.latitude && animal.longitude && (
                    <Marker
                        key={animal.animalId}
                        position={[animal.latitude, animal.longitude]}
                        icon={customIcon}
                    >
                        <Popup>{animal.name}</Popup>
                    </Marker>
                )
            ))}
        </MapContainer>
    );
}

export default Map;
