import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faLightbulb, faRoute, faVolumeHigh } from "@fortawesome/free-solid-svg-icons";
import { PiBoundingBoxFill } from "react-icons/pi";
import { useAnimalContext } from '../contexts/AnimalContext';
import Map from './Map';
import axios from 'axios';
import { baseUrl } from '../consts';

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
        if (!selectedAnimal) {
            navigate("/map");
            return;
        }
        console.log("Selected animal:", selectedAnimal);

        if (!selectedAnimal.latitude || !selectedAnimal.longitude) {
            console.log("Missing latitude/longitude data. Waiting...");
        }
    }, [selectedAnimal, navigate]);

    useEffect(() => {
        if (!selectedAnimal || !selectedAnimal.id) {
            console.log('No selected animal to fetch data for.');
            return;
        }

        const fetchAnimalData = async () => {
            try {
                const headers = {
                    Authorization: `Bearer ${localStorage.getItem('authToken')}`,
                };

                // Fetch latitude, longitude, and battery data
                const [latitudeResponse, longitudeResponse, latestResponse] = await Promise.all([
                    axios.get(`${baseUrl}/animaldata/historic/${selectedAnimal.id}/latitude`, {
                        params: {
                            start: "-1d",
                            end: "now()",
                            interval: "15m",
                            aggregate: "last",
                        },
                        headers,
                    }),
                    axios.get(`${baseUrl}/animaldata/historic/${selectedAnimal.id}/longitude`, {
                        params: {
                            start: "-1d",
                            end: "now()",
                            interval: "15m",
                            aggregate: "last",
                        },
                        headers,
                    }),
                    axios.get(`${baseUrl}/animaldata/latest/${selectedAnimal.id}`, {
                        headers,
                    }),
                ]);

                const latitudeData = latitudeResponse.data;
                const longitudeData = longitudeResponse.data;
                const latestData = latestResponse.data;


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
                setLatestAnimalData(latestData);
            } catch (error) {
                console.error("Failed to fetch animal data:", error);
                setRouteData([]);
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
                        <span className="text-white font-semibold text-sm">Battery: {latestAnimalData?.batteryPercentage || "Unknown"}%</span>
                    </div>
                </div>

                {/* Animal Info */}
                <div className="flex flex-col items-center">
                    <span className="text-white font-semibold text-lg">{selectedAnimal.name}</span>
                    <img
                        src={selectedAnimal?.imageUrl || "https://placedog.net/300/300"}

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
