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
                const response = await axios.get(`${base_url}/animals`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('authToken')}`,
                    },
                });
                console.log('API Response (AnimalContext):', response.data);
                if (!Array.isArray(response.data)) {
                    throw new Error('API response is not an array');
                }
                setAnimals(response.data); // Ensure this is an array
                let selectedAnimalId = localStorage.getItem('selectedAnimal');
                // print type
                console.log(typeof selectedAnimalId);
                if (selectedAnimalId) {
                    console.log('Selected animal ID:', selectedAnimalId);
                    console.log(response.data);
                    const selected = response.data.find((animal) => animal.id == selectedAnimalId);
                    console.log('Selected animal:', selected);
                    setSelectedAnimal(selected);
                }
            } catch (err) {
                console.error(err);
            }
        };
        fetchAnimals();
    }, []);

    useEffect(() => {
        if (selectedAnimal) {
            localStorage.setItem('selectedAnimal', selectedAnimal.id);
        }
    }, [selectedAnimal]);

    return (
        <AnimalContext.Provider value={{ animals, selectedAnimal, setSelectedAnimal }}>
            {children}
        </AnimalContext.Provider>
    );
};

export const useAnimalContext = () => useContext(AnimalContext);
