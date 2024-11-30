import React, { useEffect, useState } from 'react';
import { useAnimalContext } from '../contexts/AnimalContext';
import { MapContainer, TileLayer, Marker, Popup, Polygon, Polyline, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import pin from '../assets/pin.png';
import axios from 'axios';

const base_url = 'http://localhost/api/v1';

function Map({ animals, fence, showFence, routeData, showRoute, addingFence, setFence, clickHandler }) {
    const [myPetsData, setMyPetsData] = useState([]);

    useEffect(() => {
        const fetchDynamicData = async () => {
            try {
                if (!animals || animals.length === 0) {
                    console.log('No animals to fetch');
                } else {
                    const updatedAnimals = await Promise.all(
                        animals.map(async (animal) => {
                            console.log('Fetching dynamic data for:', animal);
                            const response = await axios.get(`${base_url}/animaldata/latest/${animal.id}`);
                            return { ...animal, ...response.data }; // Merge permanent and dynamic data
                        })
                    );
                    setMyPetsData(updatedAnimals);
                }
            } catch (error) {
                console.error('Failed to fetch dynamic data:', error);
            }
        };

        fetchDynamicData();
    }, [animals]);

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
    const centerPosition =
        myPetsData.length > 0 && myPetsData[0].latitude && myPetsData[0].longitude
            ? [myPetsData[0].latitude, myPetsData[0].longitude]
            : [40.63316, -8.65939]; // Default fallback position

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

            {/* Render Fence if showFence is true */}
            {showFence && fence.length > 0 && <Polygon positions={fence} color="green" />}

            {/* Render Routes if showRoute is true */}
            {showRoute && routeData.length > 0 && <Polyline positions={routeData} color="blue" className="z-30" />}

            {/* Render Animal Markers */}
            {myPetsData.map(animal =>
                animal.latitude && animal.longitude ? (
                    <Marker
                        key={animal.id}
                        position={[animal.latitude, animal.longitude]}
                        icon={customIcon}
                        eventHandlers={{
                            click: () => clickHandler(animal),
                        }}
                    >
                        <Popup>
                            <strong>{animal.name}</strong>
                            <br />
                            Species: {animal.species}
                            <br />
                            Speed: {animal.speed || 'N/A'} km/h
                        </Popup>
                    </Marker>
                ) : null
            )}

            {/* Use Leaflet Events for adding a fence */}
            {addingFence && <MapEvents />}
        </MapContainer>
    );
}

export default Map;
