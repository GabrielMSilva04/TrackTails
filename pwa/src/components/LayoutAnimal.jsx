import {Outlet, Link, useNavigate, useLocation} from "react-router-dom";
import NavBar from "./Navbar";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faEdit, faTrash } from "@fortawesome/free-solid-svg-icons";
import PropTypes from "prop-types";
import { useEffect } from "react";
import { useAnimalContext } from "../contexts/AnimalContext";
import axios from "axios";
import { baseUrl } from "../consts";

export default function LayoutAnimal({ showButtons = "all" }) {
    const { selectedAnimal, setSelectedAnimal } = useAnimalContext();
    const navigate = useNavigate();
    const location = useLocation();
    LayoutAnimal.propTypes = {
        showButtons: PropTypes.oneOf(["all", "back-only", "none"]),
    };

    if (!selectedAnimal) {
        return (
            <div className="text-center text-primary mt-10">
                <h2>Loading animal data...</h2>
            </div>
        );
    }

    useEffect(() => {
        console.log("Selected animal in LayoutAnimal:", selectedAnimal);
    }, [selectedAnimal]);

    const handleDelete = async () => {
        const confirmDelete = window.confirm(
            `Are you sure you want to delete ${selectedAnimal.name}? This action cannot be undone.`
        );

        if (!confirmDelete) {
            return;
        }

        try {
            const response = await axios.delete(`${baseUrl}/animals/${selectedAnimal.id}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("authToken")}`,
                },
            });
            console.log("Delete response:", response.data);

            setSelectedAnimal(null);
            navigate("/mypets");
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
                    <img src={selectedAnimal.imageUrl || "https://via.placeholder.com/150"} alt={selectedAnimal.name} />
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
                            {selectedAnimal.name}
                            <button
                                onClick={handleEdit}
                                className="ml-1.5 text-lg text-neutral border rounded-full border-neutral w-7 h-7 items-center justify-center"
                            >
                                <FontAwesomeIcon icon={faEdit} />
                            </button>
                        </div>
                    ) : (
                        <div className="mt-14 text-primary font-bold text-2xl items-center justify-center">
                            {selectedAnimal.name}
                        </div>
                    )}

                    <div className="overflow-y-auto">
                        <Outlet context={{ selectedAnimal }} />
                    </div>
                </div>
            </div>
            <NavBar />
        </div>
    );
}