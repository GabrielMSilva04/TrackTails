import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { InputField } from "../components/InputField.jsx";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useAnimalContext } from "../contexts/AnimalContext";
import { baseUrl } from "../consts";

export default function EditPet() {
    const navigate = useNavigate();
    const { selectedAnimal } = useAnimalContext(); // Get selected animal from context
    const [animalData, setAnimalData] = useState({});
    const {
        register,
        handleSubmit,
        setValue,
        formState: { errors },
    } = useForm();

    const MAX_FILE_SIZE_MB = 5;
    const ALLOWED_FORMATS = ["image/png", "image/jpeg", "image/jpg", "image/heic", "image/heif"];

    // Pre-fill form with selected animal data
    useEffect(() => {
        if (selectedAnimal) {
            Object.keys(selectedAnimal).forEach((key) => {
                if (key === "birthday" && selectedAnimal[key]) {
                    const date = new Date(selectedAnimal[key]);
                    const formattedDate = date.toISOString().split("T")[0]; // Format as YYYY-MM-DD
                    setValue("birthday", formattedDate);
                } else {
                    setValue(key, selectedAnimal[key]);
                }
            });

            // Fetch latest animal data (weight, height, etc.)
            const fetchAnimalData = async () => {
                try {
                    const token = localStorage.getItem("authToken");
                    const animalDataResponse = await axios.get(
                        `${baseUrl}/animaldata/latest/${selectedAnimal.id}`,
                        {
                            headers: {
                                Authorization: `Bearer ${token}`,
                            },
                        }
                    );
                    const data = animalDataResponse.data;
                    setAnimalData(data);

                } catch (error) {
                    console.error("Error fetching animal data:", error);
                }
            };

            fetchAnimalData();
        }
    }, [selectedAnimal, setValue]);

    const onSubmit = async (data) => {
        try {
            const token = localStorage.getItem("authToken");
            if (!token) {
                return;
            }

            console.log("Updated Pet Information:", data);

            // Update pet details
            await axios.put(
                `${baseUrl}/animals/${selectedAnimal.id}`,
                {
                    ...data,
                    weight: undefined,
                    height: undefined,
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            // Update animal data
            const updatedAnimalData = {
                animalId: selectedAnimal.id,
                weight: data.weight ? parseFloat(data.weight) : null,
                height: data.height ? parseFloat(data.height) : null,
                timestamp: new Date().toISOString(),
            };

            await axios.post(`${baseUrl}/animaldata`, updatedAnimalData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            // Update image if a new file is provided
            if (data.image && data.image.length > 0) {
                const file = data.image[0];
                if (file) {
                    if (file.size > MAX_FILE_SIZE_MB * 1024 * 1024) {
                        alert(`File size exceeds the maximum limit of ${MAX_FILE_SIZE_MB}MB.`);
                        return;
                    }
                    if (!ALLOWED_FORMATS.includes(file.type)) {
                        alert("Invalid file format. Only PNG, JPEG, JPG, HEIC, and HEIF are allowed.");
                        return;
                    }

                    const formData = new FormData();
                    formData.append("image", file);

                    await axios.post(`${baseUrl}/animals/${selectedAnimal.id}/upload`, formData, {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "multipart/form-data",
                        },
                    });

                    console.log("Image updated successfully");
                }
            }

            window.location.href = "/mypets";
        } catch (error) {
            console.error("Error updating pet or saving data:", error.response?.data || error.message);
        }
    };

    if (!selectedAnimal) {
        return (
            <div className="text-center text-primary mt-10">
                <h2>Loading pet data...</h2>
            </div>
        );
    }

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
                        onClick={() => navigate(-1)}
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
                    {/* Disclaimer */}
                    <div
                        className="text-sm text-secondary mb-4 p-3 border border-dashed border-primary rounded-md bg-gray-50">
                        <strong>Note:</strong> Filling in the optional fields especially weight and birthday can help better determine if
                        an health parameter is out of the ordinary.
                    </div>
                    {/* Scrollable Content */}
                    <div className="flex-grow overflow-y-auto space-y-4 px-4">
                        {/* Form Fields */}
                        <label className="form-control w-full">
                            <div className="label">
                                <span className="label-text text-secondary font-bold">Photo</span>
                            </div>
                            <input
                                type="file"
                                {...register("image")}
                                className="file-input file-input-bordered file-input-primary w-full"
                            />
                            {errors.image && <p className="text-error text-sm">{errors.image.message}</p>}
                        </label>

                        <InputField
                            label="Pet Name*"
                            name="name"
                            type="text"
                            placeholder="Enter your pet's name"
                            register={register}
                            required={{
                                value: true,
                                message: "Pet name is required",
                            }}
                            error={errors.name && errors.name.message}
                        />

                        <InputField
                            label="Breed"
                            name="breed"
                            type="text"
                            placeholder="Breed"
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
                                    message: "Species is required",
                                }}
                                options={[
                                    {value: "", label: "Select a species"},
                                    {value: "dog", label: "Dog"},
                                    {value: "cat", label: "Cat"},
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
                                    {value: "\u0000", label: "Select the sex"},
                                    {value: "m", label: "Male"},
                                    {value: "f", label: "Female"},
                                ]}
                                error={errors.sex && errors.sex.message}
                            />
                        </div>

                        <div className="flex gap-2">
                            <div className="flex flex-col w-full">
                                <InputField
                                    label="Weight (kg)"
                                    name="weight"
                                    type="number"
                                    placeholder="Weight (kg)"
                                    register={register}
                                    required={false}
                                    error={errors.weight && errors.weight.message}
                                />
                                <span className="text-xs text-gray-500 mt-1">
            {animalData.weight ? `Last: ${animalData.weight} kg` : "No measurements"}
        </span>
                            </div>

                            <div className="flex flex-col w-full">
                                <InputField
                                    label="Height (cm)"
                                    name="height"
                                    type="number"
                                    placeholder="Height (cm)"
                                    register={register}
                                    required={false}
                                    error={errors.height && errors.height.message}
                                />
                                <span className="text-xs text-gray-500 mt-1">
            {animalData.height ? `Last: ${animalData.height} cm` : "No measurements"}
        </span>
                            </div>
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

                        {/* Device ID */}
                        <InputField
                            label="Device ID*"
                            name="deviceId"
                            type="text"
                            placeholder="Enter the device ID"
                            register={register}
                            required={{
                                value: true,
                                message: "Device ID is required",
                            }}
                            error={errors.deviceId && errors.deviceId.message}
                        />

                        {/* Be Careful With */}
                        <InputField
                            label="Be Careful With"
                            name="beCarefulWith"
                            type="textarea"
                            placeholder="Enter any special care instructions"
                            register={register}
                            required={false}
                            error={errors.beCarefulWith && errors.beCarefulWith.message}
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
}