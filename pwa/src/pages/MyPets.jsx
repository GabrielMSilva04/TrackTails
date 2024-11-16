import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCat, faDog, faVenus, faMars } from '@fortawesome/free-solid-svg-icons'
import {Link} from "react-router-dom";
import {useState} from "react";

export function MyPets() {
    const pets = [
        {
            id: 1,
            name: 'Fluffy',
            species: 'Cat',
            sex: 'F',
            image: 'https://placecats.com/300/300',
        },
        {
            id: 2,
            name: 'Cookie',
            species: 'Dog',
            breed: 'Golden Retriever',
            sex: 'M',
            image: 'https://placedog.net/300/300',
        },
        {
            id: 3,
            name: 'Bella',
            species: 'Cat',
            breed: 'Siamese',
            sex: 'F',
            // image: '',
        },
    ]

    // State for filtering
    const [filters, setFilters] = useState({ name: '', species: '' });

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
                    <img src={pet.image ? pet.image : 'https://placehold.co/300'}
                     alt={pet.name} className="object-cover w-full h-full"/>
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
                {filteredPets.length > 0 ? (
                    filteredPets.map((pet, index) => (
                        <div key={index}>
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