import React, { useEffect, useState, useMemo } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Polygon, Polyline, useMap, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import pin from '../assets/pin.png';
import axios from 'axios';
import { wsBaseUrl, baseUrl } from '../consts';

function Map({ animals, fence, showFence, routeData, showRoute, addingFence, setFence, clickHandler }) {
    const [myPetsData, setMyPetsData] = useState([]);
    const [userLocation, setUserLocation] = useState(null);

    useEffect(() => {
        console.log('Map Page Rendered with Animals:', animals);
    }, [animals]);

    useEffect(() => {
        const websockets = [];
        const authToken = localStorage.getItem("authToken");
        const wsUrl = `${wsBaseUrl}/animaldata?auth=${authToken}`;

        const fetchDynamicData = async () => {
            const headers = {
                Authorization: `Bearer ${localStorage.getItem('authToken')}`,
            }

            const updatedAnimals = await Promise.all(
                animals.map(async (animal) => {
                    const socket = new WebSocket(wsUrl);

                    socket.onopen = () => {
                        console.log("WebSocket connection established");
                        socket.send(
                            JSON.stringify({
                                action: "subscribe",
                                animalId: animal.id,
                            })
                        );
                    };

                    socket.onmessage = (event) => {
                        console.log("Message received:", event.data);
                        const data = JSON.parse(event.data);

                        setMyPetsData((prevData) => {
                            const updatedData = prevData.map((animal) => {
                                if (animal.id == data.animalId) {
                                    console.log("Updating animal data:", data);
                                    return { ...animal, ...data };
                                }
                                return animal;
                            });

                            return updatedData;
                        });
                    };

                    socket.onerror = (error) => {
                        console.error("WebSocket error:", error);
                    };

                    socket.onclose = () => {
                        console.log("WebSocket connection closed");
                    };

                    websockets.push(socket);

                    const response = await axios.get(
                        `${baseUrl}/animaldata/latest/${animal.id}`,
                        { headers }
                    );
                    return { ...animal, ...response.data };

                })
            );
            setMyPetsData(updatedAnimals);
        };

        if (animals && animals.length > 0) {
            fetchDynamicData();
        }

        return () => {
            for (let ws of websockets) {
                ws.close();
                console.log("WebSocket connection closed by unmounting component");
            }
        };

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

    const centerPosition = useMemo(() => {
        if (myPetsData.length > 0 && myPetsData[0].latitude && myPetsData[0].longitude) {
            console.log("Centering map to animal position:", myPetsData[0]);
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
        });

    const UpdateMapCenter = ({ center }) => {
        const map = useMap();

        useEffect(() => {
            if (center) {
                map.setView(center, map.getZoom());
            }
        }, [center, map]);

        return null;
    };

    return (
        <MapContainer center={centerPosition} zoom={18} zoomControl={false} style={{ height: "100vh", width: "100%" }}>
            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />


            <UpdateMapCenter center={centerPosition} />

            {showFence && fence.length > 0 && <Polygon positions={fence} color="green" />}


            {showRoute && routeData.length > 0 && <Polyline positions={routeData} color="blue" className="z-30" />}

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


