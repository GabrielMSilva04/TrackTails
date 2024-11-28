import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Map from './Map';
import { useAnimalContext } from '../contexts/AnimalContext';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars, faTimes } from '@fortawesome/free-solid-svg-icons';

function LayoutMap() {
    const { animals, setSelectedAnimal } = useAnimalContext();
    const navigate = useNavigate();
    const [isDrawerOpen, setIsDrawerOpen] = useState(false);

    const handleAnimalClick = (animal) => {
        setSelectedAnimal(animal);
        navigate(`/map/details`);
    };

    const toggleDrawer = () => {
        setIsDrawerOpen(!isDrawerOpen);
    };

    return (
        <>
            {/* Drawer Menu */}
            <div className="flex flex-row">
                <div className="z-50 fixed top-8 left-8">
                    {!isDrawerOpen && (
                        <button
                            onClick={toggleDrawer}
                            className="btn btn-primary"
                        >
                            <FontAwesomeIcon icon={faBars} color="white" />
                        </button>
                    )}

                    {isDrawerOpen && (
                        <div className="bg-primary text-base-content w-60 rounded-lg">
                            <div className="flex flex-row justify-between mb-4">
                                <button
                                    className="btn btn-ghost mt-2"
                                    onClick={toggleDrawer}
                                >
                                    <FontAwesomeIcon icon={faTimes} className="text-white" size="2x" />
                                </button>
                                <div className="form-control">
                                    <input
                                        type="text"
                                        placeholder="Search..."
                                        className="input input-bordered w-40 mt-2 mr-2"
                                    />
                                </div>
                            </div>

                            <div className="flex flex-col gap-2 p-2">
                                {animals.map((animal) => (
                                    <div
                                        key={animal.animalId}
                                        className="bg-white rounded-lg text-primary p-2 flex flex-row items-center cursor-pointer hover:bg-gray-100 transition-colors focus:outline-none"
                                        onClick={() => handleAnimalClick(animal)}
                                    >
                                        <div>
                                            <img
                                                src={animal.image ?? "https://placehold.co/100x100"}
                                                alt={animal.name}
                                                className="h-8 w-8 rounded-full"
                                            />
                                        </div>
                                        <div className="flex flex-col ml-2">
                                            <div className="text-sm font-semibold">
                                                {animal.name}
                                            </div>
                                            <div className="text-xs text-gray-500">
                                                {animal.type}
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}
                </div>
            </div>

            {/* Map Component */}
            <div className="absolute top-0 bottom-0 left-0 right-0 z-0">
                <Map
                    animals={animals} // Pass animals data to the Map component
                    fence={[]} // Example props, replace or modify as needed
                    setFence={() => {}} // Example props, replace or modify as needed
                    addingFence={false} // Example props, replace or modify as needed
                    showFence={false} // Example props, replace or modify as needed
                    showRoute={false} // Example props, replace or modify as needed
                    routeData={[]} // Example props, replace or modify as needed
                />
            </div>
        </>
    );
}

export default LayoutMap;
