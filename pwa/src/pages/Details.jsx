import React from 'react';
import Map from './Map';

const petMarkersData = [
    {
        lat: 51.505,
        lng: -0.09,
        name: 'Jack',
        species: 'Dog',
        image: 'https://placedog.net/300/300',
    },
    {
        lat: 51.515,
        lng: -0.09,
        name: 'Whiskers',
        species: 'Cat',
        image: 'https://placecats.com/300/300',
    }
];

const Details = () => {
    return (
        <div>
            <h2>Detalhes</h2>
            <Map petMarkersData={petMarkersData} />
        </div>
    );
};

export default Details;

