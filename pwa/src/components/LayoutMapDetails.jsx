import React, { useState } from 'react';
import { useParams, Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faLightbulb, faRoute, faVolumeHigh } from "@fortawesome/free-solid-svg-icons";
import { PiBoundingBoxFill } from "react-icons/pi";
import Map from './Map'; // Ajuste este caminho se necessário

export default function LayoutMapDetails() {
    const { animalName } = useParams();
    const [fence, setFence] = useState([]);
    const [addingFence, setAddingFence] = useState(false);
    const [showFence, setShowFence] = useState(true);
    const [showFenceControls, setShowFenceControls] = useState(false);
    const [showRoute, setShowRoute] = useState(false);

    const routeData = [
        [40.633039, -8.659193],
        [40.633139, -8.659293],
        [40.633239, -8.659393],
        [40.633339, -8.659493],
        [40.633439, -8.659593],
        [40.633539, -8.659693]
    ];


    const startAddingFence = () => {
        setFence([]);
        setAddingFence(true);
        setShowFence(true);
    };

    const closeCurrentFence = () => {
        setAddingFence(false);
    };

    const toggleFenceVisibility = () => {
        setShowFence(!showFence);
    };

    const toggleFenceControls = () => {
        setShowFenceControls(!showFenceControls);
    };

    const toggleRouteVisibility = () => {
        console.log('Current state before toggle:', showRoute);
        setShowRoute(!showRoute);
        console.log('State after toggle:', !showRoute);
    };

    const undoLastVertex = () => {
        if (fence.length > 0) {
            setFence(fence.slice(0, -1));  // Remove o último ponto do array de vértices
        }
    };

    return (
        <>
            <div className="fixed top-0 left-0 right-0 bg-primary p-3 w-full h-24 -z-10 flex items-center">
                <div className="max-w-7xl mx-auto flex justify-between w-full">
                    <button style={{zIndex: 1010}}>
                        <Link to="/map" className="tooltip z-20" data-tip="Back Map"
                              style={{position: 'relative', zIndex: 1020}}>
                            <FontAwesomeIcon icon={faArrowLeft} color="white"/>
                        </Link>
                    </button>
                    <div>
                        <span className="text-white font-semibold text-sm">Battery</span>
                    </div>
                </div>
            </div>

            <div className="w-full flex flex-col items-center mt-4 z-20">
                <div className="flex-1 text-center flex flex-col items-center">
                    <span className="text-white font-semibold text-lg">{animalName}</span>
                </div>
                <img
                    src="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.espritdog.com%2Fwp-content%2Fuploads%2F2020%2F09%2Fborder-collie-700810_1920.jpg&f=1&nofb=1&ipt=29bc36e03332cdad92cedd625b2d47519ce9c4bda3a261ca261d17cf3d34b5bd&ipo=images"
                    alt="Dog Avatar"
                    className="h-20 w-20 rounded-full border-4 border-primary mt-2"/>
            </div>

            <div className="absolute right-0 top-20 left-0 bottom-28 -z-10 overflow-auto">
                <Map fence={fence} setFence={setFence} addingFence={addingFence} showFence={showFence}
                     routeData={routeData} showRoute={showRoute}/>
            </div>

            <div className="fixed bottom-0 left-0 right-0 bg-primary p-3 w-full h-28 z-10 flex items-center">
                <div className="max-w-7xl mx-4 flex justify-around w-full">
                    {showFenceControls ? (
                        <>
                            {!addingFence && (
                                <>
                                    <button onClick={startAddingFence}
                                            className="bg-white text-primary text-sm font-bold mx-2 p-2 rounded-lg">Start
                                        Fence
                                    </button>
                                    <button onClick={toggleFenceVisibility}
                                            className="bg-white text-primary text-sm font-bold mx-2 p-2 rounded-lg">
                                        {showFence ? 'Hide Fence' : 'Show Fence'}
                                    </button>
                                </>
                            )}
                            {addingFence && (
                                <>
                                    <button onClick={closeCurrentFence}
                                            className="bg-white text-primary text-sm font-bold p-2 rounded-lg">Close
                                        Fence
                                    </button>
                                    <button onClick={undoLastVertex}
                                            className="bg-white text-primary text-sm font-bold mx-2 p-2 rounded-lg">Undo
                                        Last Vertex
                                    </button>
                                </>
                            )}
                            <button onClick={toggleFenceControls}
                                    className="bg-white text-primary text-sm font-bold p-2 rounded-lg">Go Back
                            </button>
                        </>
                    ) : (
                        <>
                            <Link to="/" className="tooltip" data-tip="Home">
                                <FontAwesomeIcon icon={faLightbulb} color="white" size="lg"/>
                                <div className="text-white text-sm mt-1">Light</div>
                            </Link>
                            <Link to="/map" className="tooltip" data-tip="Map">
                                <FontAwesomeIcon icon={faVolumeHigh} color="white" size="lg"/>
                                <div className="text-white text-sm mt-1">Sound</div>
                            </Link>
                            <button onClick={toggleRouteVisibility}>
                                <FontAwesomeIcon icon={faRoute} color="white" size="lg"/>
                                <div className="text-white text-sm mt-1">{showRoute ? 'Hide Route' : 'Show Route'}</div>
                            </button>
                            <button onClick={toggleFenceControls}>
                                <PiBoundingBoxFill className="ml-7" style={{color: 'white', fontSize: '24px'}}/>
                                <div className="text-white text-sm mt-1">Fence Control</div>
                            </button>
                        </>
                    )}
                </div>
            </div>
        </>
    );
}











