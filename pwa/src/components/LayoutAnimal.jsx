import { Outlet, Link, useLocation } from "react-router-dom";
import NavBar from "./Navbar";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faEdit, faTrash } from "@fortawesome/free-solid-svg-icons";
import PropTypes from "prop-types";
import { useEffect, useState } from "react";
import axios from "axios";

const baseUrl = "http://localhost/api/v1";
const animalsBaseUrl = `${baseUrl}/animals`;

export default function LayoutAnimal({ showButtons = "all" }) {
    const location = useLocation();
    const { animal: passedAnimal } = location.state || {}; // Get animal data from state
    const [animal, setAnimal] = useState(passedAnimal || null);
    const [imageUrl, setImageUrl] = useState(null); // State for the animal image URL

    // Fetch animal details if not passed via state
    useEffect(() => {
        if (!passedAnimal && animal?.id) {
            const fetchAnimal = async () => {
                const token = localStorage.getItem("authToken");
                try {
                    const response = await axios.get(`${animalsBaseUrl}/${animal.id}`, {
                        headers: { Authorization: `Bearer ${token}` },
                    });
                    setAnimal(response.data);
                } catch (error) {
                    console.error("Error fetching animal details:", error);
                }
            };

            fetchAnimal();
        }
    }, [animal, passedAnimal]);

    console.log(animal);

    // Fetch animal image if not passed
    useEffect(() => {
        const fetchImage = async () => {
            if (animal?.id) {
                const token = localStorage.getItem("authToken");
                try {
                    const response = await axios.get(`${animalsBaseUrl}/${animal.id}/image`, {
                        headers: { Authorization: `Bearer ${token}` },
                        responseType: "blob", // Get binary data for the image
                    });
                    setImageUrl(URL.createObjectURL(response.data)); // Convert blob to URL
                } catch (error) {
                    console.error("Error fetching animal image:", error);
                    setImageUrl("https://placehold.co/300"); // Fallback image
                }
            }
        };

        if (animal && !imageUrl) fetchImage();
    }, [animal, imageUrl]);

    LayoutAnimal.propTypes = {
        showButtons: PropTypes.oneOf(["all", "back-only", "none"]),
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
                <Link to="/mypets" className="text-white text-2xl font-bold absolute left-4 mt-5 z-10 fixed">
                    <FontAwesomeIcon icon={faArrowLeft} />
                </Link>
            )}
            <div className="w-full flex justify-center items-center mt-4">
                <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
            </div>
            <div className="avatar placeholder justify-center">
                <div className="bg-neutral border-8 border-base-100 text-neutral-content w-32 rounded-full z-10 mx-auto mt-4 absolute">
                    <img src={imageUrl || "https://via.placeholder.com/150"} alt={animal.name} />
                </div>
            </div>
            <div className="h-full pt-20">
                <div className="bg-base-100 w-full h-full rounded-t-3xl flex flex-col pb-36 items-center">
                    {showButtons === "all" && (
                        <button className="text-lg text-red-700 border rounded-full border-red-700 w-7 h-7 m-2 ml-auto z-20">
                            <FontAwesomeIcon icon={faTrash} />
                        </button>
                    )}
                    {showButtons === "all" ? (
                        <div className="mt-4 text-primary font-bold text-2xl items-center justify-center">
                            {animal.name}
                            <button className="ml-1.5 text-lg text-neutral border rounded-full border-neutral w-7 h-7 items-center justify-center">
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
            <NavBar />
        </div>
    );
}