import React, { createContext, useContext, useEffect, useState } from 'react';
import axios from 'axios';

const AnimalContext = createContext();

export const AnimalProvider = ({ children }) => {
    const [animals, setAnimals] = useState([
        {
            animalId: 1,
            species: "Dog",
            name: "Rex",
            image: "https://placedog.net/300/300",
            weight: 40.5,
            height: 40,
            latitude: 40.63316,
            longitude: -8.65939,
            speed: 12.3,
            heartRate: 78,
            breathRate: 15,
            battery: 0.8,
            additionalTags: {
                species: "Dog",
                status: "Healthy",
            },
            timestamp: new Date().toISOString(),
        },
        {
            animalId: 2,
            species: "Cat",
            name: "Whiskers",
            image: "https://placecats.com/300/300",
            weight: 55.2,
            height: 50,
            latitude: 40.73415,
            longitude: -8.37021,
            speed: 8.5,
            heartRate: 90,
            breathRate: 20,
            battery: 0.5,
            additionalTags: {
                species: "Cat",
                status: "Running",
            },
            timestamp: new Date().toISOString
        },
    ]);
    const [selectedAnimal, setSelectedAnimal] = useState(null);

    // useEffect(() => {
    //     // Fetch or initialize animals (example)
    //     const fetchAnimals = async () => {
    //         try {
    //             const response = await fetch("/api/v1/animals");
    //             const data = await response.json();
    //             setAnimals(data);
    //         } catch (error) {
    //             console.error("Error fetching animals:", error);
    //         }
    //     };
    //     fetchAnimals();
    // }, []);

    return (
        <AnimalContext.Provider value={{ animals, selectedAnimal, setSelectedAnimal }}>
            {children}
        </AnimalContext.Provider>
    );
};

export const useAnimalContext = () => {
    return useContext(AnimalContext);
};