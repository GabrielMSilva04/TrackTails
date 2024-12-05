import React, { useEffect, useState, useMemo } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Polygon, Polyline, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import pin from '../assets/pin.png';
import axios from 'axios';
import { useWebSocket } from '../useWebSocket';
import { baseUrl } from '../consts';

function Map({ animals, fence, showFence, routeData, showRoute, addingFence, setFence, clickHandler }) {
    const [myPetsData, setMyPetsData] = useState([]);
    // useWebSocket(1);

    useEffect(() => {
        console.log('Map Page Rendered with Animals:', animals);
    }, [animals]);

    useEffect(() => {
        const fetchDynamicData = async () => {
            try {
                const headers = {
                    Authorization: `Bearer ${localStorage.getItem('authToken')}`,
                }
                
                const updatedAnimals = await Promise.all(
                    animals.map(async (animal) => {
                        const response = await axios.get(
                            `${baseUrl}/animaldata/latest/${animal.id}`,
                            { headers }
                        );
                      
                        return { ...animal, ...response.data };
                      
                    })
                );
                setMyPetsData(updatedAnimals);
            } catch (error) {
                console.error('Failed to fetch dynamic data:', error);
            }
        };

        if (animals && animals.length > 0) {
            fetchDynamicData();
        }
    }, [animals]);

    // useEffect(() => {
    //     // Set up WebSocket for real-time updates
    //     const socket = new WebSocket('ws://localhost:8080/realtime/updates');
    //
    //     socket.onopen = () => {
    //         console.log('WebSocket connection established.');
    //     };
    //
    //     socket.onmessage = (event) => {
    //         const updatedAnimal = JSON.parse(event.data);
    //
    //         // Update the specific animal's data
    //         setMyPetsData((prevData) =>
    //             prevData.map((animal) =>
    //                 animal.id === updatedAnimal.id ? { ...animal, ...updatedAnimal } : animal
    //             )
    //         );
    //     };
    //
    //     socket.onerror = (error) => {
    //         console.error('WebSocket error:', error);
    //     };
    //
    //     socket.onclose = () => {
    //         console.log('WebSocket connection closed.');
    //     };
    //
    //     return () => {
    //         socket.close(); // Clean up WebSocket on component unmount
    //     };
    // }, []);

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

    const centerPosition = useMemo(() => {
        if (myPetsData.length > 0 && myPetsData[0].latitude && myPetsData[0].longitude) {
            return [myPetsData[0].latitude, myPetsData[0].longitude];
        }
        return [40.63316, -8.65939];
    }, [myPetsData]);
  
    const customIcon = (animal) =>
        L.divIcon({
            html: `
            <div style="position: relative; width: 75px; height: 70px;">
                <img src="${pin}" style="width: 100%; height: 100%;" />
                <img src="${animal.imageUrl}"
                     style="width: 30px; height: 30px; border-radius: 50%; position: absolute; top: 11px; left: 22.3px;" />
            </div>
        `,
            iconAnchor: [37.5, 70],
            className: '',
        }
    );

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
                        icon={customIcon(animal)}
                        eventHandlers={{
                            click: () => clickHandler(animal),
                        }}
                    />
                ) : null
            )}

            {/* Use Leaflet Events for adding a fence */}
            {addingFence && <MapEvents />}
        </MapContainer>
    );
}

export default Map;
