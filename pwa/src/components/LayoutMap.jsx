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

                            <div className="p-2">
                                <div
                                    className="bg-white rounded-lg text-primary p-2 flex flex-row items-center cursor-pointer hover:bg-gray-100 transition-colors focus:outline-none"
                                    onClick={() => handleAnimalClick('Jack')}
                                >
                                    <div>
                                        <img
                                            src="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.espritdog.com%2Fwp-content%2Fuploads%2F2020%2F09%2Fborder-collie-700810_1920.jpg&f=1&nofb=1&ipt=29bc36e03332cdad92cedd625b2d47519ce9c4bda3a261ca261d17cf3d34b5bd&ipo=images"
                                            alt="Animal Name"
                                            className="h-8 w-8 rounded-full"
                                        />
                                    </div>
                                    <div className="flex flex-col ml-2">
                                        <div className="text-sm font-semibold">
                                            Jack
                                        </div>
                                        <div className="text-xs text-gray-500">
                                            Dog
                                        </div>
                                    </div>
                                </div>
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