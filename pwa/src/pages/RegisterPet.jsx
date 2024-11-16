import {InputField} from "../components/InputField.jsx";
import {Link} from "react-router-dom";
import React, { useState } from 'react';

export function RegisterPet() {
    const [formData, setFormData] = useState({
        name: '',
        species: '',
        sex: '',
    });

    const speciesOptions = [
        { value: 'dog', label: 'Dog' },
        { value: 'cat', label: 'Cat' },
    ];

    const sexOptions = [
        { value: 'male', label: 'Male' },
        { value: 'female', label: 'Female' },
    ];

    const [errors, setErrors] = useState({});
    const [submitted, setSubmitted] = useState(false);

    const handleInputChange = (field, value) => {
        setFormData({ ...formData, [field]: value });
    };

    const validateForm = () => {
        const newErrors = {};
        if (!formData.name) {
            newErrors.name = 'Name is required.';
        }
        if (!formData.species) {
            newErrors.species = 'Species is required.';
        }
        if (!formData.deviceId) {
            newErrors.deviceId = 'Device ID is required.';
        }
        return newErrors;
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Form Data:', formData);
        setSubmitted(true);

        const validationErrors = validateForm();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors); // Display errors
        } else {
            setErrors({});
            console.log('Form Data:', formData);
            alert('Form submitted successfully!');
        }
    }

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
                    <form className="flex flex-col gap-2 px-6 mb-10" onSubmit={handleSubmit}>
                        <label className="form-control w-full">
                            <div className="label">
                                <span className="label-text text-secondary font-bold">Photo</span>
                            </div>
                            <input
                                type="file"
                                className="file-input file-input-bordered file-input-primary w-full"/>
                        </label>
                        <InputField
                            label="Pet Name*"
                            name="name"
                            type="text"
                            placeholder="Enter your pet's name"
                            value={formData.name}
                            onChange={(value) => handleInputChange('name', value)}
                            error={submitted && errors.name}
                        />
                        <div className="flex flex-line gap-2">
                            {/* Species Dropdown */}
                            <InputField
                                label="Species*"
                                name="species"
                                type="select"
                                placeholder="Select a species"
                                value={formData.species}
                                onChange={(value) => handleInputChange('species', value)}
                                options={speciesOptions}
                                error={submitted && errors.species}
                            />

                            {/* Sex Dropdown */}
                            <InputField
                                label="Sex"
                                name="sex"
                                type="select"
                                placeholder="Select the sex"
                                value={formData.sex}
                                onChange={(value) => handleInputChange('sex', value)}
                                options={sexOptions}
                            />
                        </div>
                        <InputField
                            label="Breed"
                            type="text"
                            placeholder="Breed"
                        />
                        <div className="flex flex-line gap-2">
                            <InputField
                                label="Weight"
                                type="number"
                                placeholder="Weight"
                            />
                            <InputField
                                label="Height"
                                type="number"
                                placeholder="Height"
                            />
                        </div>
                        <InputField
                            label="Birthday"
                            type="date"
                            placeholder="Birthday"
                        />
                        <InputField
                            label="Device ID*"
                            type="text"
                            placeholder="Device ID"
                            value={formData.deviceId}
                            onChange={(value) => handleInputChange('deviceId', value)}
                            error={submitted && errors.deviceId}
                        />
                        <button className="btn btn-primary text-white w-full mt-4">Register</button>
                    </form>
                </div>
            </div>
        </>
    );
}