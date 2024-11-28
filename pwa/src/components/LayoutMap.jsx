import { useState } from 'react';
import { Link, Outlet, useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    faBars,
    faBell,
    faHouse,
    faLocationDot,
    faPaw,
    faTimes,
    faUser,
} from "@fortawesome/free-solid-svg-icons";
import Navbar from "./Navbar.jsx";

export default function LayoutMap() {
    const [isDrawerOpen, setIsDrawerOpen] = useState(false);
    const navigate = useNavigate();

    const animals = [
        {
            animalId: 1,
            name: "Buddy",
            image: "https://placedog.net/300/300",
            weight: 40.5,
            height: 40,
            latitude: 40.63316,
            longitude: -8.65939,
            speed: 12.3,
            heartRate: 78,
            breathRate: 15,
            battery: 0.8,
            additionalTags: {
                species: "Dog",
                status: "Healthy",
            },
            timestamp: new Date().toISOString(),
        },
        {
            animalId: 2,
            name: "Whiskers",
            image: "https://placecats.com/300/300",
            weight: 55.2,
            height: 50,
            latitude: 40.73415,
            longitude: -8.37021,
            speed: 8.5,
            heartRate: 90,
            breathRate: 20,
            battery: 0.5,
            additionalTags: {
                species: "Cat",
                status: "Running",
            },
            timestamp: new Date().toISOString(),
        },
    ];

    const toggleDrawer = () => {
        setIsDrawerOpen(!isDrawerOpen);
    };

    const handleAnimalClick = (animalName) => {
        navigate(`/map/${animalName.toLowerCase()}`);
        setIsDrawerOpen(false);
    };

    return (
        <>
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

                    {/* Drawer */}
                    {isDrawerOpen && (
                        <div className="bg-primary text-base-content w-60 rounded-lg">
                            <div className="flex flex-row justify-between mb-4">
                                <button
                                    className="btn btn-ghost mt-2"
                                    onClick={toggleDrawer}
                                >
                                    <FontAwesomeIcon icon={faTimes} className="text-white" size="2x"/>
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
                                        onClick={() =>
                                            handleAnimalClick(animal.name)
                                        }
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

                {!isDrawerOpen && (
                    <div className="fixed w-full z-50">
                        <div className="w-32 mx-auto mt-8">
                            <h1 className="btn btn-ghost text-2xl text-primary fixed left-1/2 z-50 ml-[-4rem]">
                                trackTails.
                            </h1>
                        </div>
                    </div>
                )}
            </div>

            <div className="absolute right-0 top-0 left-0 bottom-0 z-0">
                <Outlet/>
            </div>

            <Navbar/>
        </>
    );
}