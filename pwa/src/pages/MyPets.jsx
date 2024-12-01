import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCat, faDog, faVenus, faMars } from '@fortawesome/free-solid-svg-icons'
import {Link} from "react-router-dom";
import {useEffect, useState} from "react";
import { useAnimalContext } from "../contexts/AnimalContext";
import { useNavigate } from 'react-router-dom';

export default function MyPets({ onAnimalSelect }) {
    const [error, setError] = useState(null); // State to track errors
    const { setSelectedAnimal, animals, loading } = useAnimalContext();
    const navigate = useNavigate();

    // State for filtering
    const [filters, setFilters] = useState({ name: '', species: '' });

    const selectHandle = (pet) => {
        onAnimalSelect(pet.id);
        console.log("SET SELECTED ANIMAL", pet);
        setSelectedAnimal(pet);
        navigate(`/animal/monitoring`);
    }

    // Filtered pets
    const filteredPets = animals.filter((pet) => {
        const matchesName = pet.name.toLowerCase().includes(filters.name.toLowerCase());
        const matchesSpecies = filters.species ? pet.species === filters.species : true;
        return matchesName && matchesSpecies;
    });

    // Update filter state
    const handleFilterChange = (field, value) => {
        setFilters((prev) => ({ ...prev, [field]: value }));
    };


    const speciesIcon = {
        cat: <FontAwesomeIcon icon={faCat} />,
        dog: <FontAwesomeIcon icon={faDog} />,
    };

    const sexIcon = {
        f: <FontAwesomeIcon icon={faVenus} />,
        m: <FontAwesomeIcon icon={faMars} />,
    };

    const petCard = (pet) => {
        return (
            <div className="relative flex flex-col items-center bg-primary rounded-xl p-4 w-40 shadow-lg mt-10">
                {/* Pet Image */}
                <div className="absolute -top-10 rounded-full overflow-hidden h-20 w-20 shadow-md">
                    <img
                        src={pet.imageUrl}
                        alt={pet.name}
                        className="object-cover w-full h-full"
                        onError={(e) => {
                            e.target.src = 'https://placehold.co/300'; // Fallback image on error
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
    };

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
                    <option value="dog">Dog</option>
                    <option value="cat">Cat</option>
                </select>
            </div>

            {/* Pet Cards Section */}
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
                        <button key={pet.id} onClick={() => selectHandle(pet)} className="focus:outline-none">
                            {petCard(pet)}
                        </button>
                    ))
                ) : (
                    <p className="text-gray-500 text-center w-full">No pets found.</p>
                )}
            </div>
        </>
    );
}