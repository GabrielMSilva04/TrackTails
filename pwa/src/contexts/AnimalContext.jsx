import { createContext, useState, useEffect, useContext } from 'react';
import axios from 'axios';
import { baseUrl } from '../consts';

const AnimalContext = createContext();

export const AnimalProvider = ({ children }) => {
    const [animals, setAnimals] = useState([]);
    const [selectedAnimal, setSelectedAnimal] = useState(null);
    const [loading, setLoading] = useState(true); // Add loading state

    useEffect(() => {
        const fetchAnimals = async () => {
            try {
                const response = await axios.get(`${baseUrl}/animals`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('authToken')}`,
                    },
                });

                console.log('API Response (AnimalContext):', response.data);

                if (!Array.isArray(response.data)) {
                    throw new Error('API response is not an array');
                }

                const animalsWithImages = await addImageUrlsToPets(response.data, localStorage.getItem('authToken'));
                setAnimals(animalsWithImages);

                setLoading(false);
            } catch (err) {
                console.error('Error fetching animals:', err);
                setLoading(false);
            }
        };

        fetchAnimals();
    }, []);

    // Fetch image for a single pet by ID
    const fetchImageUrl = async (petId, token) => {
        try {
            const response = await axios.get(`${baseUrl}/animals/${petId}/image`, {
                headers: { Authorization: `Bearer ${token}` },
                responseType: 'blob',
            });

            // Convert Blob to URL and return
            return URL.createObjectURL(response.data);
        } catch (err) {
            console.error(`Failed to fetch image for pet ID ${petId}:`, err);
            return 'https://placehold.co/300';
        }
    };

    // Add image URL to each pet object
    const addImageUrlsToPets = async (pets, token) => {
        const updatedPets = await Promise.all(
            pets.map(async (pet) => {
                const imageUrl = await fetchImageUrl(pet.id, token);
                return { ...pet, imageUrl };
            })
        );
        return updatedPets;
    };

    return (
        <AnimalContext.Provider value={{ animals, selectedAnimal, setSelectedAnimal, loading }}>
            {children}
        </AnimalContext.Provider>
    );
};

export const useAnimalContext = () => useContext(AnimalContext);
