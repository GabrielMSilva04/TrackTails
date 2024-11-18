import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCat, faDog, faVenus, faMars } from '@fortawesome/free-solid-svg-icons'
import {Link} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";

const base_url = "http://localhost/api/v1";

export default function MyPets() {
    const [pets, setPets] = useState([]);
    const [loading, setLoading] = useState(true); // State to track API call status
    const [error, setError] = useState(null); // State to track errors

    // State for filtering
    const [filters, setFilters] = useState({ name: '', species: '' });

    // Fetch pets from the API
    useEffect(() => {
        //TODO: Fetch pets only if the user is logged in and has a token
        const fetchPets = async () => {
            try {
                const response = await axios.get(`${base_url}/animals`);
                console.log('API Response:', response.data);
                if (!Array.isArray(response.data)) {
                    throw new Error('API response is not an array');
                }
                setPets(response.data); // Ensure this is an array
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchPets();
    }, []);

    // Filtered pets
    const filteredPets = pets.filter((pet) => {
        const matchesName = pet.name.toLowerCase().includes(filters.name.toLowerCase());
        const matchesSpecies = filters.species ? pet.species === filters.species : true;
        return matchesName && matchesSpecies;
    });

    // Update filter state
    const handleFilterChange = (field, value) => {
        setFilters((prev) => ({ ...prev, [field]: value }));
    };


    const speciesIcon = {
        Cat: <FontAwesomeIcon icon={faCat} />,
        Dog: <FontAwesomeIcon icon={faDog} />,
    }
    const sexIcon = {
        F: <FontAwesomeIcon icon={faVenus} />,
        M: <FontAwesomeIcon icon={faMars} />,
    }
    const petCard = (pet) => {
        return (
            <div className="relative flex flex-col items-center bg-primary rounded-xl p-4 w-40 shadow-lg mt-10">
                {/* Pet Image */}
                <div
                    className="absolute -top-10 rounded-full overflow-hidden h-20 w-20 shadow-md">
                    <img
                        src={pet.imagePath ? pet.imagePath : 'https://placehold.co/300'}
                        alt={pet.name}
                        className="object-cover w-full h-full"/>
                </div>

                {/* Spacer for the image */}
                <div className="mt-6"></div>

                {/* Pet Info */}
                <div className="flex items-center justify-between w-full mt-4 text-white">
                    {/* Pet Type */}
                    <div className="flex items-center">
                        {speciesIcon[pet.species]}
                    </div>
                    {/* Pet Name */}
                    <span className="font-bold text-lg">{pet.name}</span>
                    {/* Gender Icon */}
                    {sexIcon[pet]}

                    {/* Sex Icon */}
                    <div className="flex items-center">
                        {sexIcon[pet.sex]}
                    </div>
                </div>
            </div>
        );
    }

    return (
        <>
            {/* Header Section */}
            <div className="flex flex-line justify-between items-center mb-10">
                <h1 className="text-3xl text-primary font-bold">My Pets</h1>
                <Link to="/registerpet" className="btn btn-primary text-white mt-4">+ Add Pet</Link>
            </div>

            {/* Filter Section */}
            <div className="flex flex-line justify-between items-center gap-4 mb-6">
                {/* Name Filter */}
                <input
                    type="text"
                    placeholder="Search by name"
                    value={filters.name}
                    onChange={(e) => handleFilterChange('name', e.target.value)}
                    className="input input-bordered border-2 input-primary w-7/12"
                />

                {/* Species Filter */}
                <select
                    value={filters.species}
                    onChange={(e) => handleFilterChange('species', e.target.value)}
                    className="select select-bordered border-2 select-primary w-5/12"
                >
                    <option value="">All Species</option>
                    <option value="Dog">Dog</option>
                    <option value="Cat">Cat</option>
                </select>
            </div>

            {/* Pet Cards Section */}
            <div className="flex flex-wrap justify-center gap-4">
                {loading ? (
                    <p className="text-gray-500 text-center w-full">Loading pets...</p>
                ) : error ? (
                    <p className="text-error text-center w-full">{error}</p>
                ) : filteredPets.length > 0 ? (
                    filteredPets.map((pet) => (
                        <div key={pet.id}>
                            {petCard(pet)}
                        </div>
                    ))
                ) : (
                    <p className="text-gray-500 text-center w-full">No pets found.</p>
                )}
            </div>
        </>
    );
}
