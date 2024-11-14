import {InputField} from "../components/InputField.jsx";
import {Link} from "react-router-dom";

export function RegisterPet() {
    return (
        <>
            <div className="bg-primary h-screen flex flex-col">
                {/* Logo Section */}
                <div className="w-full flex justify-center items-center mt-12">
                    <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
                </div>

                {/* Formulary Section */}
                <div className="bg-white w-full h-fit rounded-t-3xl p-8 flex flex-col items-center absolute bottom-0">
                    <div className="flex items-center justify-between w-full relative mb-12">
                        <Link to={"/mypets"} className="text-primary font-bold text-lg absolute left-0">← Back</Link>
                        <h2 className="text-2xl font-bold text-primary mx-auto">Add pet</h2>
                    </div>
                    <form className="flex flex-col gap-2 px-6 mb-10">
                        <label className="form-control w-full">
                            <div className="label">
                                <span className="label-text text-secondary font-bold">Photo</span>
                            </div>
                            <input
                                type="file"
                                className="file-input file-input-bordered file-input-primary w-full"/>
                        </label>
                        <InputField label="Name" type="text" placeholder="Name"/>
                        <InputField label="Species" type="text" placeholder="Species"/>
                        <InputField label="Breed" type="text" placeholder="Breed"/>
                        <div className="flex flex-line gap-2">
                            <InputField label="Weight" type="number" placeholder="Weight"/>
                            <InputField label="Height" type="number" placeholder="Height"/>
                        </div>
                        <InputField label="Birthday" type="date" placeholder="Birthday"/>
                        <InputField label="Device ID" type="text" placeholder="Device ID"/>
                        <button className="btn btn-primary text-white w-full mt-4">Register</button>
                    </form>
                </div>
            </div>
        </>
        );
        }