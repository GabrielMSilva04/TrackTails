import React, { useState, useMemo, useEffect } from 'react';
import { Link, useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faLightbulb, faRoute, faVolumeHigh } from "@fortawesome/free-solid-svg-icons";
import { PiBoundingBoxFill } from "react-icons/pi";
import { useAnimalContext } from '../contexts/AnimalContext'; // Import the context hook
import Map from './Map';
import axios from 'axios';

const base_url = "http://localhost/api/v1";

export default function LayoutMapDetails() {
    const { selectedAnimal } = useAnimalContext();
    const [latestAnimalData, setLatestAnimalData] = useState(null);
    const navigate = useNavigate();
    const [fence, setFence] = useState([]);
    const [addingFence, setAddingFence] = useState(false);
    const [showFence, setShowFence] = useState(true);
    const [showRoute, setShowRoute] = useState(false);
    const [showFenceControls, setShowFenceControls] = useState(false);
    const [routeData, setRouteData] = useState([]);

    useEffect(() => {
        if (!selectedAnimal || !selectedAnimal.latitude || !selectedAnimal.longitude) {
            navigate("/map");
        }
        console.log("Selected animal:", selectedAnimal);
    }, [selectedAnimal, navigate]);

    useEffect(() => {
        const fetchAnimalData = async () => {
            if (!selectedAnimal) return;

            try {
                const headers = {
                    Authorization: `Bearer ${localStorage.getItem('authToken')}`, // Replace with your token retrieval logic
                };

                // Fetch latitude, longitude, and battery data
                const [latitudeResponse, longitudeResponse, latestResponse] = await Promise.all([
                    axios.get(`${base_url}/animaldata/historic/${selectedAnimal.animalId}/latitude`, {
                        params: {
                            start: "-1d",
                            end: "now()",
                            interval: "15m",
                            aggregate: "last",
                        },
                        headers,
                    }),
                    axios.get(`${base_url}/animaldata/historic/${selectedAnimal.animalId}/longitude`, {
                        params: {
                            start: "-1d",
                            end: "now()",
                            interval: "15m",
                            aggregate: "last",
                        },
                        headers,
                    }),
                    axios.get(`${base_url}/animaldata/latest/${selectedAnimal.animalId}`, {
                        headers,
                    }),
                ]);

                const latitudeData = latitudeResponse.data;
                const longitudeData = longitudeResponse.data;
                const latestData = latestResponse.data;

                console.log("Latitude data:", latitudeData);
                console.log("Longitude data:", longitudeData);
                console.log("Latest data (battery):", latestData);

                const combinedData = latitudeData.map(latPoint => {
                    const matchingLonPoint = longitudeData.find(
                        lonPoint => lonPoint.timestamp === latPoint.timestamp
                    );
                    if (matchingLonPoint) {
                        return [latPoint.latitude, matchingLonPoint.longitude];
                    }
                    return null;
                }).filter(point => point !== null);

                console.log("Combined Route Data:", combinedData);

                setRouteData(combinedData);

                if (latestData.battery) {
                    setLatestAnimalData(prevAnimal => ({
                        ...prevAnimal,
                        battery: latestData.battery,
                    }));
                }
            } catch (error) {
                console.error("Failed to fetch animal data:", error);
                setRouteData([]); // Clear route data on error
            }
        };

        fetchAnimalData();
    }, [selectedAnimal]);


    const closeCurrentFence = () => {
        setAddingFence(false);
    };

    const undoLastVertex = () => {
        if (fence.length > 0) {
            setFence(fence.slice(0, -1));
        }
    };


    if (!selectedAnimal) {
        return <div>Loading...</div>;
    }

    return (
        <>
            {/* Top Bar */}
            <div className="fixed top-0 left-0 right-0 bg-primary p-3 w-full h-28 z-20 flex flex-col items-center">
                <div className="max-w-7xl mx-auto flex justify-between w-full">
                    <button>
                        <Link to="/map" className="tooltip" data-tip="Back to Map">
                            <FontAwesomeIcon icon={faArrowLeft} color="white" />
                        </Link>
                    </button>
                    <div>
                        <span className="text-white font-semibold text-sm">Battery: {latestAnimalData?.battery * 100 || "Unknown"}</span>
                    </div>
                </div>

                {/* Animal Info */}
                <div className="flex flex-col items-center">
                    <span className="text-white font-semibold text-lg">{selectedAnimal.name}</span>
                    <img
                        src={selectedAnimal?.image || "https://placedog.net/300/300"}
                        alt={selectedAnimal?.name || "Unknown Animal"}
                        className="h-20 w-20 rounded-full border-4 border-primary mt-2"
                    />
                </div>
            </div>

            {/* Map Section */}
            <div className="absolute right-0 top-26 left-0 bottom-28 -z-20 overflow-auto">
                <Map
                    animals={[selectedAnimal]}
                    fence={fence}
                    setFence={setFence}
                    addingFence={addingFence}
                    showFence={showFence}
                    routeData={showRoute ? routeData : []}
                    showRoute={showRoute}
                    clickHandler={() => {}}
                />
            </div>

            {/* Bottom Controls */}
            <div className="fixed bottom-0 left-0 right-0 bg-primary p-3 w-full h-28 z-20 flex items-center">
                <div className="max-w-7xl mx-auto flex justify-around w-full">
                    {showFenceControls ? (
                        <>
                            {!addingFence && (
                                <>
                                    <button
                                        onClick={() => {
                                            setFence([]);
                                            setAddingFence(true);
                                            setShowFence(true);
                                        }}
                                        className="bg-white text-primary text-sm font-bold mx-2 p-2 rounded-lg"
                                    >
                                        Start Fence
                                    </button>
                                    <button
                                        onClick={() => setShowFence((prev) => !prev)}
                                        className="bg-white text-primary text-sm font-bold mx-2 p-2 rounded-lg"
                                    >
                                        {showFence ? "Hide Fence" : "Show Fence"}
                                    </button>
                                </>
                            )}
                            {addingFence && (
                                <>
                                    <button
                                        onClick={closeCurrentFence}
                                        className="bg-white text-primary text-sm font-bold mx-2 p-2 rounded-lg"
                                    >
                                        Close Fence
                                    </button>
                                    <button
                                        onClick={undoLastVertex}
                                        className="bg-white text-primary text-sm font-bold mx-2 p-2 rounded-lg"
                                    >
                                        Undo Last Vertex
                                    </button>
                                </>
                            )}
                            <button
                                onClick={() => setShowFenceControls((prev) => !prev)}
                                className="bg-white text-primary text-sm font-bold p-2 rounded-lg"
                            >
                                Go Back
                            </button>
                        </>
                    ) : (
                        <>
                            <div className="tooltip" data-tip="Light">
                                <FontAwesomeIcon icon={faLightbulb} color="white" size="lg" />
                                <div className="text-white text-sm mt-1">Light</div>
                            </div>
                            <div className="tooltip" data-tip="Sound">
                                <FontAwesomeIcon icon={faVolumeHigh} color="white" size="lg" />
                                <div className="text-white text-sm mt-1">Sound</div>
                            </div>
                            <button onClick={() => setShowRoute((prev) => !prev)}>
                                <FontAwesomeIcon icon={faRoute} color="white" size="lg" />
                                <div className="text-white text-sm mt-1">
                                    {showRoute ? "Hide Route" : "Show Route"}
                                </div>
                            </button>
                            <button onClick={() => setShowFenceControls((prev) => !prev)}>
                                <PiBoundingBoxFill className="ml-7" style={{ color: 'white', fontSize: '24px' }} />
                                <div className="text-white text-sm mt-1">Fence Control</div>
                            </button>
                        </>
                    )}
                </div>
            </div>
        </>
    );
}
