import React, { useState, useMemo, useEffect } from 'react';
import { Link, useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faLightbulb, faRoute, faVolumeHigh } from "@fortawesome/free-solid-svg-icons";
import { PiBoundingBoxFill } from "react-icons/pi";
import { useAnimalContext } from '../contexts/AnimalContext'; // Import the context hook
import Map from './Map';

export default function LayoutMapDetails() {
    const { selectedAnimal } = useAnimalContext(); // Access the selected animal from context
    const navigate = useNavigate();

    // If no animal is selected or animal data is missing, redirect to /map
    useEffect(() => {
        if (!selectedAnimal || !selectedAnimal.latitude || !selectedAnimal.longitude) {
            navigate("/map"); // Redirect to the map page if no valid animal is selected
        }
        console.log("Selected animal:", selectedAnimal); // Debug log
    }, [selectedAnimal, navigate]); // Only run when selectedAnimal changes

    // States for map interaction and toggles
    const [fence, setFence] = useState([]);
    const [addingFence, setAddingFence] = useState(false);
    const [showFence, setShowFence] = useState(true);
    const [showRoute, setShowRoute] = useState(false);
    const [showFenceControls, setShowFenceControls] = useState(false);

    // Predefined routeData for demonstration
    const routeData = selectedAnimal?.routeData || [
        [40.633039, -8.659193],
        [40.633139, -8.659293],
        [40.633239, -8.659393],
    ];

    if (!selectedAnimal) {
        return <div>Loading...</div>; // Show loading if selectedAnimal is still undefined
    }

    return (
        <>
            {/* Top Bar */}
            <div className="fixed top-0 left-0 right-0 bg-primary p-3 w-full h-24 -z-10 flex items-center">
                <div className="max-w-7xl flex justify-between w-full mx-2">
                    <button>
                        <Link to="/map" className="tooltip" data-tip="Back to Map">
                            <FontAwesomeIcon icon={faArrowLeft} color="white" />
                        </Link>
                    </button>
                    <div>
                        <span className="text-white font-semibold text-sm">Battery: {selectedAnimal.battery * 100}%</span>
                    </div>
                </div>
            </div>

            {/* Animal Info */}
            <div className="fixed top-4 w-full flex flex-col items-center z-10">
                <div className="flex-1 text-center flex flex-col items-center">
                    <span className="text-white font-semibold text-lg">
                        {selectedAnimal.name}
                    </span>
                </div>
                <img
                    src={selectedAnimal?.image || "https://placedog.net/300/300"} // Default image fallback
                    alt={selectedAnimal?.name || "Unknown Animal"}
                    className="h-20 w-20 rounded-full border-4 border-primary mt-2"
                />
            </div>

            {/* Map Section */}
            <div className="absolute right-0 top-26 left-0 bottom-28 -z-20 overflow-auto">
                <Map
                    animals={[selectedAnimal]} // Display only the selected animal
                    fence={fence}
                    setFence={setFence}
                    addingFence={addingFence}
                    showFence={showFence}
                    routeData={showRoute ? routeData : []} // Conditionally show route data
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
                                            setFence([]); // Reset fence
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
                                        onClick={() => setAddingFence(false)}
                                        className="bg-white text-primary text-sm font-bold p-2 rounded-lg"
                                    >
                                        Close Fence
                                    </button>
                                    <button
                                        onClick={() => setFence((prevFence) => prevFence.slice(0, -1))}
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
                            <Link to="/" className="tooltip" data-tip="Home">
                                <FontAwesomeIcon icon={faLightbulb} color="white" size="lg" />
                                <div className="text-white text-sm mt-1">Light</div>
                            </Link>
                            <Link to="/map" className="tooltip" data-tip="Map">
                                <FontAwesomeIcon icon={faVolumeHigh} color="white" size="lg" />
                                <div className="text-white text-sm mt-1">Sound</div>
                            </Link>
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
