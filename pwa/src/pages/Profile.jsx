import React, { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCat, faDog, faVenus, faMars } from '@fortawesome/free-solid-svg-icons';
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css';
import axios from 'axios';

const base_url = "http://localhost/api/v1";

export default function Profile() {
    const [pets, setPets] = useState([]);
    const [user, setUser] = useState(null);

    useEffect(() => {
        const fetchUser = async () => {
            const token = localStorage.getItem('authToken');
            if (!token) {
                console.warn('No token found. User is not logged in.');
                return;
            }
            try {
                const response = await axios.get(`${base_url}/users/me`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                console.log('User API Response:', response.data);
                setUser(response.data);
            } catch (err) {
                console.error('Error fetching user:', err);
            }
        };

        fetchUser();
    }, []);

    useEffect(() => {
        const fetchPets = async () => {
            const token = localStorage.getItem('authToken');
            if (!token) {
                console.warn('No token found. User is not logged in.');
                return;
            }
            try {
                const response = await axios.get(`${base_url}/animals`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                console.log('Pets API Response:', response.data);
                setPets(response.data);
            } catch (err) {
                console.error('Error fetching pets:', err);
            }
        };

        fetchPets();
    }, []);

    const speciesIcon = {
        Cat: <FontAwesomeIcon icon={faCat} />,
        Dog: <FontAwesomeIcon icon={faDog} />,
    };

    const sexIcon = {
        F: <FontAwesomeIcon icon={faVenus} />,
        M: <FontAwesomeIcon icon={faMars} />,
    };

    const PetCard = ({ pet }) => (
        <div className="flex flex-col items-center bg-primary rounded-xl p-4 pt-16 w-40 shadow-lg mt-12">
            <div className="absolute -translate-y-24 rounded-full overflow-hidden h-20 w-20 shadow-md">
                <img
                    src={pet.imagePath || 'https://placehold.co/300'}
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

    const UserCard = ({ user }) => (
        <div className="flex flex-col items-center mt-4 z-10">
            <div className="avatar placeholder">
                <div className="bg-neutral text-neutral-content w-20 h-20 rounded-full flex items-center justify-center shadow-lg">
                    <span className="text-3xl font-bold">{user.displayName || "User"}</span>
                </div>
            </div>
            <div className="text-gray-600 mb-4 text-sm">{user.email}</div>
            <div className="flex flex-col justify-center z-0">
                <button className="btn btn-primary text-white mb-3 w-40">Edit Profile</button>
                <button className="btn btn-secondary text-white mt-6 w-40">Logout</button>
            </div>
        </div>
    );

    if (!user) {
        return <div>Loading user profile...</div>;
    }

    return (
        <div className="profile-page">
            <div className="flex space-x-4 p-4 relative">
                <UserCard user={user} />
            </div>

            <div className="p-6">
                <h2 className="text-2xl font-bold text-primary mb-6">My Pets</h2>
                {pets.length > 0 ? (
                    <Swiper spaceBetween={40} slidesPerView={2} className="w-full pt-8">
                        {pets.map((pet) => (
                            <SwiperSlide key={pet.id}>
                                <PetCard pet={pet} />
                            </SwiperSlide>
                        ))}
                    </Swiper>
                ) : (
                    <p className="text-gray-500">You have no pets registered.</p>
                )}
            </div>
        </div>
    );
}




