import React, { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCat, faDog, faVenus, faMars } from '@fortawesome/free-solid-svg-icons';
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css';
import axios from 'axios';
import { useAnimalContext } from "../contexts/AnimalContext";
import { baseUrl } from '../consts';
import {useNavigate} from "react-router-dom";

export default function Profile({ onAnimalSelect }) {
    const [user, setUser] = useState(null);
    const [editMode, setEditMode] = useState(false);
    const { animals,setSelectedAnimal } = useAnimalContext();
    const [formData, setFormData] = useState({
        displayName: '',
        email: '',
        password: '',
    });
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUser = async () => {
            const token = localStorage.getItem('authToken');
            if (!token) {
                console.warn('No token found. User is not logged in.');
                return;
            }
            try {
                const response = await axios.get(`${baseUrl}/users/me`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                setUser(response.data);
                setFormData({
                    displayName: response.data.displayName || '',
                    email: response.data.email || '',
                    password: '',
                });
            } catch (err) {
                console.error('Error fetching user:', err);
            }
        };

        fetchUser();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleLogout = () => {
        localStorage.removeItem('authToken');
        window.location.href = '/';
    };

    const handleUpdateUser = async (userId) => {
        const token = localStorage.getItem('authToken');
        if (!token) {
            console.warn('No token found. User is not logged in.');
            return;
        }

        if (!userId) {
            console.error('User ID is undefined.');
            return;
        }

        try {
            const response = await axios.put(`${baseUrl}/users/${userId}`, formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });
            setUser(response.data);
            setEditMode(false);
        } catch (err) {
            console.error('Error updating user:', err);
            alert('Failed to update the profile. Please try again.');
        }
    };

    const speciesIcon = {
        Cat: <FontAwesomeIcon icon={faCat} />,
        Dog: <FontAwesomeIcon icon={faDog} />,
    };

    const sexIcon = {
        F: <FontAwesomeIcon icon={faVenus} />,
        M: <FontAwesomeIcon icon={faMars} />,
    };

    const selectHandle = (pet) => {
        // Atualiza o animal selecionado no contexto e chama a prop
        if (onAnimalSelect) {
            onAnimalSelect(pet.id);
        }
        console.log("SET SELECTED ANIMAL", pet); // Debug
        setSelectedAnimal(pet);
        navigate('/animal/monitoring', { state: { fromProfile: true } });// Redireciona para a rota
    };


    const PetCard = ({ pet }) => (
        <div className="flex flex-col items-center bg-primary rounded-xl p-4 pt-16 w-40 shadow-lg mt-12">
            <div className="absolute -translate-y-24 rounded-full overflow-hidden h-20 w-20 shadow-md">
                <img
                    src={pet.imageUrl || 'https://placehold.co/300'}
                    alt={pet.name}
                    className="object-cover w-full h-full"
                />
            </div>
            <div className="flex items-center justify-between w-full mt-4 text-white">
                <div>{speciesIcon[pet.species]}</div>
                <span className="font-bold text-lg">{pet.name}</span>
                <div>{sexIcon[pet.sex]}</div>
            </div>
        </div>
    );

    if (!user) {
        return <div>Loading user profile...</div>;
    }

    return (
        <div className="profile-page flex flex-col items-center space-y-6 p-4">
            <div className="flex flex-col items-center">
                <div className="avatar placeholder">
                    <div
                        className="bg-neutral text-neutral-content w-32 h-32 rounded-full flex items-center justify-center shadow-lg"
                    >
                        <span className="text-4xl font-bold">
                            {user.displayName ? user.displayName.charAt(0).toUpperCase() : 'U'}
                        </span>
                    </div>
                </div>
                <div className="text-gray-600 text-lg font-medium mt-4">{user.displayName || "User"}</div>
                <div className="text-gray-600 text-sm">{user.email}</div>
            </div>

            {editMode ? (
                <div className="flex flex-col items-center mt-4 z-10">
                    <h2 className="text-2xl font-bold mb-4">Edit Profile</h2>
                    <form className="flex flex-col items-center">
                        <div className="mb-4">
                            <label className="block text-sm font-medium">Display Name</label>
                            <input
                                type="text"
                                name="displayName"
                                value={formData.displayName}
                                onChange={handleInputChange}
                                className="input input-bordered w-full max-w-xs"
                            />
                        </div>
                        <div className="mb-4">
                            <label className="block text-sm font-medium">Email</label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleInputChange}
                                className="input input-bordered w-full max-w-xs"
                            />
                        </div>
                        <div className="mb-4">
                            <label className="block text-sm font-medium">Password</label>
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleInputChange}
                                className="input input-bordered w-full max-w-xs"
                            />
                        </div>
                        <div className="flex space-x-4">
                            <button
                                type="button"
                                onClick={() => handleUpdateUser(user.userId)}
                                className="btn btn-primary text-white"
                            >
                                Save Changes
                            </button>
                            <button
                                type="button"
                                onClick={() => setEditMode(false)}
                                className="btn btn-secondary"
                            >
                                Cancel
                            </button>
                        </div>
                    </form>
                </div>
            ) : (
                <>
                    <div className="w-full max-w-lg">
                        {animals.length > 0 ? (
                            <Swiper spaceBetween={40} slidesPerView={2} className="w-full">
                                {animals.map((animal) => (
                                    <SwiperSlide key={animal.id}>
                                        <button onClick={() => selectHandle(animal)} className="focus:outline-none">
                                            <PetCard pet={animal}/>
                                        </button>
                                    </SwiperSlide>
                                ))}
                            </Swiper>
                        ) : (
                            <p className="text-gray-500 text-center">You have no pets registered.</p>
                        )}
                    </div>

                    <div className="flex flex-col items-center space-y-4">
                        <button
                            className="btn btn-primary text-white w-40"
                            onClick={() => setEditMode(true)}
                        >
                            Edit Profile
                        </button>
                        <button
                            className="btn btn-secondary text-white w-40"
                            onClick={handleLogout}
                        >
                            Logout
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}






