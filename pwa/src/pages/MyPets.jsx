import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCat, faDog, faVenus, faMars } from '@fortawesome/free-solid-svg-icons'
import {Link} from "react-router-dom";

export function MyPets() {
    const pets = [
        {
            name: 'Fluffy',
            species: 'Cat',
            sex: 'F',
            image: 'https://placecats.com/300/300',
        },
        {
            name: 'Cookie',
            species: 'Dog',
            breed: 'Golden Retriever',
            sex: 'M',
            image: 'https://placedog.net/300/300',
        },
        {
            name: 'Bella',
            species: 'Cat',
            breed: 'Siamese',
            sex: 'F',
            // image: '',
        },
    ]
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
            <div className="flex flex-line justify-between items-center mb-10">
                <h1 className="text-3xl text-primary font-bold">My Pets</h1>
                <Link to="/petregister" className="btn btn-primary text-white mt-4">+ Add Pet</Link>
            </div>
            <div className="flex flex-wrap justify-center gap-4">
                {pets.map((pet, index) => (
                    <div key={index}>
                        {petCard(pet)}
                    </div>
                ))}
            </div>
        </>
    )
}