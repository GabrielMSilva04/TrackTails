import {Outlet, Link, useNavigate, useLocation, useParams} from "react-router-dom";
import NavBar from "./Navbar";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faEdit, faTrash } from "@fortawesome/free-solid-svg-icons";
import PropTypes from "prop-types";
import { useEffect, useState } from "react";
import { useAnimalContext } from "../contexts/AnimalContext";
import axios from "axios";
import { baseUrl } from "../consts";

export default function LayoutAnimal({ showButtons = "all", useUrl = false }) {
    const animalContext = useAnimalContext?.();
    const { selectedAnimal, setSelectedAnimal } = animalContext || {};
    const [animal, setAnimal] = useState(null); // For URL-based animal loading
    const navigate = useNavigate();
    const location = useLocation();
    const { deviceId } = useParams(); // Get deviceId from URL if useUrl is true

    LayoutAnimal.propTypes = {
        showButtons: PropTypes.oneOf(["all", "back-only", "none"]),
        useUrl: PropTypes.bool, // New prop to determine the data source
    };

    const fetchImageUrl = async (petId) => {
        try {
            const response = await axios.get(`${baseUrl}/finders/animal/${petId}/image`, {
                responseType: 'blob',
            });

            // Convert Blob to URL and return
            return URL.createObjectURL(response.data);
        } catch (err) {
            console.error(`Failed to fetch image for pet ID ${petId}:`, err);
            return 'https://placehold.co/300';
        }
    };

    useEffect(() => {
        if (useUrl && deviceId) {
            // Fetch animal data by deviceId from the URL
            const fetchAnimal = async () => {
                try {
                    const response = await axios.get(`${baseUrl}/finders/animal/${deviceId}`);
                    const fetchedAnimal = response.data;

                    const imageUrl = await fetchImageUrl(fetchedAnimal.id);

                    setAnimal({ ...fetchedAnimal, imageUrl });
                } catch (error) {
                    console.error("Error fetching animal by deviceId:", error);
                }
            };
            fetchAnimal();
        } else {
            // Use the context-based animal data
            setAnimal(selectedAnimal);
        }
    }, [useUrl, deviceId, selectedAnimal]);

    const handleDelete = async () => {
        if (!animal) return;

        const confirmDelete = window.confirm(
            `Are you sure you want to delete ${animal.name}? This action cannot be undone.`
        );

        if (!confirmDelete) {
            return;
        }

        try {
            const response = await axios.delete(`${baseUrl}/animals/${animal.id}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("authToken")}`,
                },
            });
            console.log("Delete response:", response.data);

            setSelectedAnimal(null);
            window.location.href = "/mypets";

        } catch (error) {
            console.error("Error deleting animal:", error);
        }
    };

    const handleEdit = () => {
        navigate(`/editpet`);
    };

    const fromProfile = location.state?.fromProfile;

    const onBackClick = () => {
        if (fromProfile) {
            navigate('/profile');
        } else {
            navigate(-1);
        }
        window.location.href = "/mypets";
    };

    if (!animal) {
        return (
            <div className="text-center text-primary mt-10">
                <h2>Loading animal data...</h2>
            </div>
        );
    }

    return (
        <div className="bg-primary h-screen flex flex-col overflow-hidden">
            {showButtons !== "none" && (
                <button className="text-white text-2xl font-bold absolute left-4 mt-5 z-10 fixed" onClick={onBackClick}>
                    <FontAwesomeIcon icon={faArrowLeft} />
                </button>
            )}
            <div className="w-full flex justify-center items-center mt-4">
                <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
            </div>
            <div className="avatar placeholder justify-center">
                <div className="bg-neutral border-8 border-base-100 text-neutral-content w-32 rounded-full z-10 mx-auto mt-4 absolute">
                    <img src={animal.imageUrl || "https://via.placeholder.com/150"} alt={animal.name} />
                </div>
            </div>
            <div className="h-full pt-20">
                <div className="bg-base-100 w-full h-full rounded-t-3xl flex flex-col pb-36 items-center">
                    {showButtons === "all" && (
                        <button
                            className="text-lg text-red-700 border rounded-full border-red-700 w-7 h-7 m-2 ml-auto z-20"
                            onClick={handleDelete}
                        >
                            <FontAwesomeIcon icon={faTrash} />
                        </button>
                    )}
                    {showButtons === "all" ? (
                        <div className="mt-4 text-primary font-bold text-2xl items-center justify-center">
                            {animal.name}
                            <button
                                onClick={handleEdit}
                                className="ml-1.5 text-lg text-neutral border rounded-full border-neutral w-7 h-7 items-center justify-center"
                            >
                                <FontAwesomeIcon icon={faEdit} />
                            </button>
                        </div>
                    ) : (
                        <div className="mt-14 text-primary font-bold text-2xl items-center justify-center">
                            {animal.name}
                        </div>
                    )}

                    <div className="overflow-y-auto">
                        <Outlet context={{ animal }} />
                    </div>
                </div>
            </div>
        </div>
    );
}