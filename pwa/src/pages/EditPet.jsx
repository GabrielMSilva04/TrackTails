import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import {InputField} from "../components/InputField.jsx";

export default function EditPet(){
    // Example pet information
    const petData = {
        name: 'Buddy',
        species: 'dog',
        breed: 'Golden Retriever',
        weight: 30,
        height: 60,
        birthday: '2018-05-20',
        sex: 'male',
    };

    const {
        register,
        handleSubmit,
        setValue,
        formState: { errors },
    } = useForm();

    // Pre-fill form with pet data
    useEffect(() => {
        Object.keys(petData).forEach((key) => {
            setValue(key, petData[key]);
        });
    }, [setValue]);

    const onSubmit = (data) => {
        console.log('Updated Pet Information:', data);
        alert('Pet information updated successfully!');
    };

    return (
        <div className="bg-primary h-screen w-full flex flex-col overflow-hidden">
            {/* Logo Section */}
            <div className="h-1/4 w-full flex justify-center items-center bg-primary">
                <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
            </div>

            {/* Formulary Section */}
            <div className="bg-white w-full rounded-t-3xl p-8 flex flex-col items-center h-3/4 overflow-hidden">
                <div className="flex items-center justify-between w-full relative mb-6">
                    <button
                        onClick={() => window.history.back()}
                        className="text-primary font-bold text-lg absolute left-0"
                    >
                        ← Back
                    </button>
                    <h2 className="text-2xl font-bold text-primary mx-auto">Edit Pet</h2>
                </div>

                {/* Form Section */}
                <form
                    onSubmit={handleSubmit(onSubmit)}
                    className="flex flex-col gap-2 h-full w-full"
                >
                    {/* Scrollable Content */}
                    <div className="flex-grow overflow-y-auto space-y-4 px-4">
                        {/* Form Fields */}
                        <InputField
                            label="Pet Name*"
                            name="name"
                            type="text"
                            placeholder="Enter your pet's name"
                            register={register}
                            required={{
                                value: true,
                                message: 'Pet name is required',
                            }}
                            error={errors.name && errors.name.message}
                        />

                        <InputField
                            label="Breed"
                            name="breed"
                            type="text"
                            placeholder="Enter the breed"
                            register={register}
                            required={false}
                            error={errors.breed && errors.breed.message}
                        />

                        <div className="flex flex-line gap-2">
                            <InputField
                                label="Species*"
                                name="species"
                                type="select"
                                placeholder="Select a species"
                                register={register}
                                required={{
                                    value: true,
                                    message: 'Species is required',
                                }}
                                options={[
                                    {value: '', label: 'Select a species'},
                                    {value: 'dog', label: 'Dog'},
                                    {value: 'cat', label: 'Cat'},
                                ]}
                                error={errors.species && errors.species.message}
                            />

                            <InputField
                                label="Sex"
                                name="sex"
                                type="select"
                                placeholder="Select the sex"
                                register={register}
                                required={false}
                                options={[
                                    {value: '', label: 'Select the sex'},
                                    {value: 'male', label: 'Male'},
                                    {value: 'female', label: 'Female'},
                                ]}
                                error={errors.sex && errors.sex.message}
                            />
                        </div>

                        <div className="flex gap-2">
                            <InputField
                                label="Weight (kg)"
                                name="weight"
                                type="number"
                                placeholder="Enter the weight"
                                register={register}
                                required={false}
                                error={errors.weight && errors.weight.message}
                            />

                            <InputField
                                label="Height (cm)"
                                name="height"
                                type="number"
                                placeholder="Enter the height"
                                register={register}
                                required={false}
                                error={errors.height && errors.height.message}
                            />
                        </div>

                        <InputField
                            label="Birthday"
                            name="birthday"
                            type="date"
                            placeholder="Select the birthday"
                            register={register}
                            required={false}
                            error={errors.birthday && errors.birthday.message}
                        />
                    </div>

                    {/* Button Section */}
                    <div className="py-4 px-4 bg-white w-full">
                        <button type="submit" className="btn btn-primary w-full">
                            Update Pet
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};
