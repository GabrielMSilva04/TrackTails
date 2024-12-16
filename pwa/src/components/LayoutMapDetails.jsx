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
    const [light_on, setLightOn] = useState(false);
    const [playing_sound, setPlayingSound] = useState(false);

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

    // Fetch latest animal data and route data
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
                const [latitudeResponse, longitudeResponse, latestResponse] = await Promise.all([
                    axios.get(`${baseUrl}/animaldata/historic/${selectedAnimal.id}/latitude`, {
                        params: { start: "-10m", end: "now()", interval: "10s", aggregate: "last" },
                        headers,
                    }),
                    axios.get(`${baseUrl}/animaldata/historic/${selectedAnimal.id}/longitude`, {
                        params: { start: "-10m", end: "now()", interval: "10s", aggregate: "last" },
                        headers,
                    }),
                    axios.get(`${baseUrl}/animaldata/latest/${selectedAnimal.id}`, { headers }),
                ]);

                const latitudeData = latitudeResponse.data;
                const longitudeData = longitudeResponse.data;
                const latestData = latestResponse.data;

                // verify if latestData includes blinking
                console.log("Latest Animal Data:", latestData);
                if (latestData.blinking) {
                    setLightOn(true);
                }

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

        const intervalId = setInterval(() => {
            fetchAnimalData();
        }, 10000); // Fetch every 10 seconds

        // Cleanup interval on component unmount or dependency change
        return () => {
            clearInterval(intervalId);
        };
    }, [selectedAnimal]);

    // Fetch existing fence data
    useEffect(() => {
        if (!selectedAnimal || !selectedAnimal.id) return;

        const fetchFenceData = async () => {
            try {
                const headers = { Authorization: `Bearer ${localStorage.getItem('authToken')}` };
                const response = await axios.get(`${baseUrl}/fences/${selectedAnimal.id}`, { headers });
                const fenceData = response.data;
                console.log("Fetched fence data:", fenceData);

                setFence([
                    [fenceData.point1Latitude, fenceData.point1Longitude],
                    [fenceData.point2Latitude, fenceData.point2Longitude],
                    [fenceData.point3Latitude, fenceData.point3Longitude],
                    [fenceData.point4Latitude, fenceData.point4Longitude],
                ]);
            } catch (error) {
                console.error("Failed to fetch fence data:", error);
            }
        };

        fetchFenceData();
    }, [selectedAnimal]);

    // Save or update fence
    const saveFence = async () => {
        console.log("Fence Coordinates:", fence);
        if (fence.length < 4) {
            setFence([]);
            return;
        }

        const [point1, point2, point3, point4] = fence;

        const fenceData = {
            animalId: selectedAnimal.id,
            point1Latitude: fence[0].lat,
            point1Longitude: fence[0].lng,
            point2Latitude: fence[1].lat,
            point2Longitude: fence[1].lng,
            point3Latitude: fence[2].lat,
            point3Longitude: fence[2].lng,
            point4Latitude: fence[3].lat,
            point4Longitude: fence[3].lng,
        };

        try {
            console.log("Fence Data:", fenceData);
            const headers = { Authorization: `Bearer ${localStorage.getItem('authToken')}` };
            await axios.post(`${baseUrl}/fences`, fenceData, { headers });
            setAddingFence(false);
        } catch (error) {
            console.error("Failed to save fence:", error);
        }
    };

    // Delete fence
    const deleteFence = async () => {
        try {
            const headers = { Authorization: `Bearer ${localStorage.getItem('authToken')}` };
            await axios.delete(`${baseUrl}/fences/${selectedAnimal.id}`, { headers });
            setFence([]);
        } catch (error) {
            console.error("Failed to delete fence:", error);
        }
    };

    const closeCurrentFence = () => {
        setAddingFence(false);
        saveFence();
    };

    const undoLastVertex = () => {
        if (fence.length > 0) {
            setFence(fence.slice(0, -1));
        }
    };

    const execAction = (actionType) => {
        const headers = { Authorization: `Bearer ${localStorage.getItem('authToken')}` };
        axios.post(`${baseUrl}/actions`, {
            actionType: actionType,
            animalId: selectedAnimal.id,
        }, { headers })

        if (actionType === "Sound" && !playing_sound) {
            setPlayingSound(true);
            setTimeout(() => setPlayingSound(false), 2000);
        } else if (actionType === "Blink") {
            setLightOn(!light_on);
        }
    }

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
                    targetAnimal={selectedAnimal}
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
                                        className="bg-white text-primary text-xs font-bold mx-2 p-2 rounded-lg"
                                    >
                                        Start Fence
                                    </button>
                                    <button
                                        onClick={() => setShowFence((prev) => !prev)}
                                        className="bg-white text-primary text-xs font-bold mx-2 p-2 rounded-lg"
                                    >
                                        {showFence ? "Hide Fence" : "Show Fence"}
                                    </button>
                                    <button
                                        onClick={deleteFence}
                                        className="bg-white text-primary text-xs font-bold mx-2 p-2 rounded-lg"
                                    >
                                        Delete Fence
                                    </button>
                                </>
                            )}
                            {addingFence && (
                                <>
                                    <button
                                        onClick={closeCurrentFence}
                                        className="bg-white text-primary text-xs font-bold mx-2 p-2 rounded-lg"
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
                            <button onClick={() => execAction("Blink")}>
                                <FontAwesomeIcon icon={faLightbulb} color={light_on ? "yellow" : "white"} size="lg" />
                                <div className="text-white text-sm mt-1">Light</div>
                            </button>
                            <button onClick={() => execAction("Sound")}>
                                <FontAwesomeIcon icon={faVolumeHigh} color={playing_sound ? "yellow" : "white"} size="lg" />
                                <div className="text-white text-sm mt-1">Sound</div>
                            </button>
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
