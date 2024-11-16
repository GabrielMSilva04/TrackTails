import {Link, Outlet} from 'react-router-dom';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowLeft, faEdit, faPlus, faTrash} from "@fortawesome/free-solid-svg-icons";

export default function LayoutAnimal() {
    const animal = {
        name: "Jack",
        age: 3,
        species: "Dog",
        breed: "Border Collie",
        weight: 30,
        height: 50,
        sex: "m",
        img: "https://ilovemydogsomuch.com/wp-content/uploads/2023/02/323839-1600x1066-border-collie-breed-1400x933.jpg"
    }
    return (
        <div className="bg-primary h-screen flex flex-col">
            <Link to="/" className="text-white text-2xl font-bold absolute left-4 mt-5">
                <FontAwesomeIcon icon={faArrowLeft}/>
            </Link>
            <div className="w-full flex justify-center items-center mt-12">
                <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
            </div>
            <div className="avatar placeholder">
                <div className="bg-neutral border-8 border-white text-neutral-content w-32 rounded-full z-10 mx-auto mt-8">
                    <img src={animal.img} alt={animal.name}/>
                </div>
            </div>
            <div className="bg-primary">
                <div className="bg-white w-full h-3/4 rounded-t-3xl p-1 flex flex-col items-center absolute bottom-0">
                    <div className="mt-7 text-primary font-bold text-2xl items-center justify-center">
                        <button className="absolute top-3 right-3 text-lg text-red-700 border rounded-full border-red-700 w-7 h-7 items-center justify-center"><FontAwesomeIcon icon={faTrash}/></button>
                        {animal.name}
                        <button className="ml-1.5 text-lg text-neutral border rounded-full border-neutral w-7 h-7 items-center justify-center"><FontAwesomeIcon icon={faEdit}/></button>
                    </div>
                    <div className="mt-2 text-secondary font-bold text-xs">
                        Age: {animal.age}, {animal.species}, Sex: {animal.sex === 'm' ? 'Male' : 'Female'}
                    </div>
                    <div className="mt-2 text-secondary font-bold text-xs">
                        Last Weight: {animal.weight}kg,
                        Last Height:{animal.height}cm
                        {/*<button className="ml-1.5 text-sm text-neutral border rounded-full border-neutral w-5 h-5 items-center justify-center"><FontAwesomeIcon icon={faPlus}/></button>*/}
                    </div>
                    <div className="overflow-y-auto">
                        <Outlet/>
                    </div>
                </div>
            </div>
        </div>
    );
}