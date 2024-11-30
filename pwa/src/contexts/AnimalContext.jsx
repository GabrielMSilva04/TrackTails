import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AnimalContext = createContext();

const base_url = "http://localhost/api/v1";

export const AnimalProvider = ({ children }) => {
    const [animals, setAnimals] = useState([]);
    const [selectedAnimal, setSelectedAnimal] = useState(null);

    useEffect(() => {
        const fetchAnimals = async () => {
            try {
                const response = await axios.get(`${base_url}/animals`);
                console.log('API Response (AnimalContext):', response.data);
                if (!Array.isArray(response.data)) {
                    throw new Error('API response is not an array');
                }
                setAnimals(response.data); // Ensure this is an array
            } catch (err) {
                console.error(err);
            }
        };
        fetchAnimals();
    }, []);

    return (
        <AnimalContext.Provider value={{ animals, selectedAnimal, setSelectedAnimal }}>
            {children}
        </AnimalContext.Provider>
    );
};

export const useAnimalContext = () => useContext(AnimalContext);
