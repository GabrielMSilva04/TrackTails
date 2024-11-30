import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCat, faDog, faVenus, faMars } from '@fortawesome/free-solid-svg-icons';
import { Link, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';

const base_url = "http://localhost/api/v1";

export default function MyPets() {
    const [pets, setPets] = useState([]);
    const [loading, setLoading] = useState(true); // State to track API call status
    const [error, setError] = useState(null); // State to track errors
    const [filters, setFilters] = useState({ name: '', species: '' }); // Filter state
    const navigate = useNavigate(); // React Router's navigation hook

    // Fetch pets from the API
    useEffect(() => {
        const fetchPets = async () => {
            const token = localStorage.getItem('authToken');
            if (!token) {
                console.warn('No token found. User is not logged in.');
                setLoading(false);
                return;
            }

            try {
                const response = await axios.get(`${base_url}/animals`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                if (!Array.isArray(response.data)) {
                    throw new Error('API response is not an array');
                }

                const petsWithImages = await addImageUrlsToPets(response.data, token);
                setPets(petsWithImages);
            } catch (err) {
                console.error('Error fetching pets:', err);
                setError('Failed to fetch pets. Please try again.');
            } finally {
                setLoading(false);
            }
        };

        fetchPets();
    }, []);

    // Function to fetch the image for a specific pet
    const fetchImageUrl = async (petId, token) => {
        try {
            const response = await axios.get(`${base_url}/animals/${petId}/image`, {
                headers: { Authorization: `Bearer ${token}` },
                responseType: 'blob',
            });

            return URL.createObjectURL(response.data);
        } catch (err) {
            console.error(`Failed to fetch image for pet ID ${petId}:`, err);
            return 'https://placehold.co/300';
        }
    };

    const addImageUrlsToPets = async (pets, token) => {
        const updatedPets = await Promise.all(
            pets.map(async (pet) => {
                const imageUrl = await fetchImageUrl(pet.id, token);
                return { ...pet, imageUrl };
            })
        );
        return updatedPets;
    };

    // Filtered pets
    const filteredPets = pets.filter((pet) => {
        const matchesName = pet.name.toLowerCase().includes(filters.name.toLowerCase());
        const matchesSpecies = filters.species ? pet.species === filters.species : true;
        return matchesName && matchesSpecies;
    });

    // Handle navigation to the monitoring page
    const handlePetClick = (pet) => {
        navigate('/animal/monitoring', { state: { animal: pet } }); // Pass the pet object via state
    };

    const speciesIcon = {
        Cat: <FontAwesomeIcon icon={faCat} />,
        Dog: <FontAwesomeIcon icon={faDog} />,
    };

    const sexIcon = {
        f: <FontAwesomeIcon icon={faVenus} />,
        m: <FontAwesomeIcon icon={faMars} />,
    };

    const petCard = (pet) => (
        <div
            className="relative flex flex-col items-center bg-primary rounded-xl p-4 w-40 shadow-lg mt-10 cursor-pointer"
            onClick={() => handlePetClick(pet)} // Navigate on click
        >
            {/* Pet Image */}
            <div className="absolute -top-10 rounded-full overflow-hidden h-20 w-20 shadow-md">
                <img
                    src={pet.imageUrl}
                    alt={pet.name}
                    className="object-cover w-full h-full"
                    onError={(e) => {
                        e.target.src = 'https://placehold.co/300';
                    }}
                />
            </div>

            {/* Spacer for the image */}
            <div className="mt-6"></div>

            {/* Pet Info */}
            <div className="flex items-center justify-between w-full mt-4 text-white">
                <div className="flex items-center">{speciesIcon[pet.species]}</div>
                <span className="font-bold text-lg">{pet.name}</span>
                <div className="flex items-center">{sexIcon[pet.sex]}</div>
            </div>
        </div>
    );

    return (
        <>
            <div className="flex flex-line justify-between items-center mb-4">
                <h1 className="text-3xl text-primary font-bold">My Pets</h1>
                <Link to="/registerpet" className="btn btn-primary text-white mt-4">+ Add Pet</Link>
            </div>

            <div className="flex flex-line justify-between items-center gap-4 mb-4">
                <input
                    type="text"
                    placeholder="Search by name"
                    value={filters.name}
                    onChange={(e) => setFilters((prev) => ({ ...prev, name: e.target.value }))}
                    className="input input-bordered border-2 input-primary w-7/12"
                />
                <select
                    value={filters.species}
                    onChange={(e) => setFilters((prev) => ({ ...prev, species: e.target.value }))}
                    className="select select-bordered border-2 select-primary w-5/12"
                >
                    <option value="">All Species</option>
                    <option value="Dog">Dog</option>
                    <option value="Cat">Cat</option>
                </select>
            </div>

            <div
                className="flex flex-wrap justify-center gap-4 overflow-y-auto"
                style={{ maxHeight: '65vh' }}
            >
                {loading ? (
                    <p className="text-gray-500 text-center w-full">Loading pets...</p>
                ) : error ? (
                    <p className="text-error text-center w-full">{error}</p>
                ) : filteredPets.length > 0 ? (
                    filteredPets.map((pet) => (
                        <div key={pet.id}>{petCard(pet)}</div>
                    ))
                ) : (
                    <p className="text-gray-500 text-center w-full">No pets found.</p>
                )}
            </div>
        </>
    );
}