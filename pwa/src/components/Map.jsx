import React from 'react';
import {MapContainer, TileLayer, Polygon, useMapEvents, Polyline, Marker} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import pin from '../assets/pin.png';


const customIcon = L.divIcon({
    html: `
        <div style="position: relative; width: 75px; height: 70px;">
            <img src="${pin}" style="width: 100%; height: 100%;" />
            <img src="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.espritdog.com%2Fwp-content%2Fuploads%2F2020%2F09%2Fborder-collie-700810_1920.jpg&f=1&nofb=1&ipt=29bc36e03332cdad92cedd625b2d47519ce9c4bda3a261ca261d17cf3d34b5bd&ipo=images" 
                 style="width: 30px; height: 30px; border-radius: 50%; position: absolute; top: 11px; left: 22.3px;" />
        </div>
    `,
    iconAnchor: [37.5, 70], // Ajuste conforme necessário para centralizar corretamente o ícone
    className: ''
});


function Map({ fence, setFence, addingFence, showFence, showRoute, routeData }) {
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

    const position = [40.633039, -8.659193];

    return (
        <MapContainer center={position} zoom={17} zoomControl={false} style={{ height: '100vh', width: '100%' }}>
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {showFence && fence.length > 0 && (
                <Polygon positions={fence} color="green" />
            )}
            {showRoute && routeData.length > 0 && (
                <Polyline positions={routeData} color="green" />
            )}
            <Marker position={position} icon={customIcon}>
                Jack
            </Marker>
            <MapEvents />
        </MapContainer>

    );
}

export default Map;





