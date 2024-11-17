import React, { useState } from 'react';
import { Link, Outlet } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars, faHouse, faLocationDot, faPaw, faBell, faUser } from "@fortawesome/free-solid-svg-icons";

export function LayoutMap() {
    const [isSidebarOpen, setSidebarOpen] = useState(false);
    const [search, setSearch] = useState("");
    const pets = [
        { name: "Jack", species: "Dog", image: "https://placedog.net/300/300", link: "/map/jack" },
        { name: "Cookie", species: "Cat", image: "https://placedog.net/300/300", link: "/map/cookie" },
        { name: "Tico", species: "Bird", image: "https://placedog.net/300/300", link: "/map/tico" },
        { name: "Milu", species: "Dog", image: "https://placedog.net/300/300", link: "/map/milu" },
        { name: "Jack", species: "Dog", image: "https://placedog.net/300/300", link: "/map/jack" },
        { name: "Cookie", species: "Cat", image: "https://placedog.net/300/300", link: "/map/cookie" },
        { name: "Tico", species: "Bird", image: "https://placedog.net/300/300", link: "/map/tico" },
        { name: "Milu", species: "Dog", image: "https://placedog.net/300/300", link: "/map/milu" },
    ];

    return (
        <div className="relative min-h-screen">
            <main className="w-full h-screen">
                <Outlet/>
            </main>

            <header className="fixed top-2 left-0 right-0 w-full h-16 z-40">
                <div className="flex justify-between items-center px-4 h-full">
                    <button
                        onClick={() => setSidebarOpen(!isSidebarOpen)}
                        className="btn btn-square btn-sm bg-primary text-base-100 border-none hover:bg-primary/90"
                    >
                        <FontAwesomeIcon icon={faBars} size="lg"/>
                    </button>

                    {!isSidebarOpen && (
                        <h1 className="text-2xl font-bold text-primary mx-auto">
                            trackTails.
                        </h1>
                    )}

                    <div className="w-12 h-12 invisible">
                        <FontAwesomeIcon icon={faBars} size="lg" style={{visibility: 'hidden'}}/>
                    </div>
                </div>
            </header>

            <aside
                className={`fixed top-16 left-0 w-64 bg-primary shadow-xl transform transition-transform duration-300 ease-in-out z-30 m-4 rounded-xl
                ${isSidebarOpen ? 'translate-x-0' : '-translate-x-full'}`}
                style={{height: '60vh'}}
            >
                <div className="flex flex-col h-full">
                    <div className="p-4 bg-primary rounded-t-xl">
                        <input
                            type="text"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                            placeholder="Search Pets..."
                            className="input input-sm w-full bg-base-100/90 placeholder-base-content/50"
                        />
                    </div>

                    <div className="flex-1 overflow-y-auto px-4 pb-4">
                        <div className="space-y-2">
                            {pets.map((pet, index) => (
                                <Link
                                    key={index}
                                    to={`/map/${pet.name}`}  // Usando o nome do pet como parâmetro na URL
                                    className="block bg-base-100/90 rounded-lg p-2 hover:bg-base-100 transition-colors"
                                    onClick={() => setSidebarOpen(false)}
                                >
                                    <div className="flex items-center gap-3">
                                        <img src={pet.image} alt={pet.name}
                                             className="w-10 h-10 rounded-full object-cover"/>
                                        <h3 className="font-medium">{pet.name}</h3>
                                    </div>
                                </Link>
                            ))}
                        </div>
                    </div>
                </div>
            </aside>

            <nav className="fixed bottom-4 left-1/2 transform -translate-x-1/2 w-3/4 z-50">
                <div className="btm-nav bg-primary rounded-lg">
                    <button>
                        <Link to="/home" className="tooltip" data-tip="Home">
                            <FontAwesomeIcon icon={faHouse} className="text-base-100"/>
                        </Link>
                    </button>
                    <button>
                        <Link to="/map" className="tooltip" data-tip="Map">
                            <FontAwesomeIcon icon={faLocationDot} className="text-base-100"/>
                        </Link>
                    </button>
                    <button>
                        <Link to="/animals" className="tooltip" data-tip="My Animals">
                            <FontAwesomeIcon icon={faPaw} className="text-base-100"/>
                        </Link>
                    </button>
                    <button>
                        <Link to="/notifications" className="tooltip" data-tip="Notifications">
                            <FontAwesomeIcon icon={faBell} className="text-base-100"/>
                        </Link>
                    </button>
                    <button>
                        <Link to="/profile" className="tooltip" data-tip="Profile">
                            <FontAwesomeIcon icon={faUser} className="text-base-100"/>
                        </Link>
                    </button>
                </div>
            </nav>
        </div>
    );
}