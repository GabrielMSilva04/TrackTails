import { InputField } from "../components/InputField.jsx";
import { Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import axios from "axios";
import { baseUrl } from "../consts";

export default function RegisterPet() {
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm();

    const MAX_FILE_SIZE_MB = 5;
    const ALLOWED_FORMATS = ['image/png', 'image/jpeg', 'image/jpg', 'image/heic', 'image/heif'];

    const onSubmit = async (data) => {
        try {
            const token = localStorage.getItem('authToken');
            if (!token) {
                // alert('No authentication token found. Please log in.');
                return;
            }

            console.log('Form data:', data);

            // Validate image file size and format before uploading
            const file = data.image[0];
            if (file) {
                if (file.size > MAX_FILE_SIZE_MB * 1024 * 1024) {
                    alert(`File size exceeds the maximum limit of ${MAX_FILE_SIZE_MB}MB.`);
                    return;
                }
                if (!ALLOWED_FORMATS.includes(file.type)) {
                    alert('Invalid file format. Only PNG, JPEG, JPG, HEIC, and HEIF are allowed.');
                    return;
                }
            }

            // Register pet (without image)
            const petResponse = await axios.post(
                `${baseUrl}/animals`,
                data,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            const savedPet = petResponse.data;
            console.log('Pet registered successfully:', savedPet);

            // Upload image if selected
            if (file) {
                const formData = new FormData();
                formData.append('image', file);

                const uploadResponse = await axios.post(
                    `${baseUrl}/animals/${savedPet.id}/upload`,
                    formData,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            'Content-Type': 'multipart/form-data',
                        },
                    }
                );

                console.log('Image uploaded successfully:', uploadResponse.data);
            }

            // Send height and weight to AnimalDataController
            const animalData = {
                animalId: savedPet.id,
                weight: data.weight ? parseFloat(data.weight) : null,
                height: data.height ? parseFloat(data.height) : null,
                timestamp: new Date().toISOString(),
            };

            const animalDataResponse = await axios.post(
                `${baseUrl}/animaldata`,
                animalData,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            console.log('Animal data saved successfully:', animalDataResponse.data);

            // alert('Pet, image, and data registered successfully!');
            window.location.href = '/mypets';
        } catch (error) {
            console.error('Error registering pet, uploading image, or saving data:', error.response?.data || error.message);
        }
    };

    const speciesOptions = [
        { value: 'dog', label: 'Dog' },
        { value: 'cat', label: 'Cat' },
    ];

    const sexOptions = [
        { value: 'm', label: 'Male' },
        { value: 'f', label: 'Female' },
    ];

    return (
        <div className="bg-primary h-screen w-full flex flex-col overflow-hidden">
            {/* Logo Section */}
            <div className="h-1/6 w-full flex justify-center items-center bg-primary">
                <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
            </div>

            {/* Formulary Section */}
            <div className="bg-white w-full rounded-t-3xl p-8 flex flex-col items-center h-5/6 overflow-hidden">
                <div className="flex items-center justify-between w-full relative mb-6">
                    <Link to={"/mypets"} className="text-primary font-bold text-lg absolute left-0">← Back</Link>
                    <h2 className="text-2xl font-bold text-primary mx-auto">Add pet</h2>
                </div>

                <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-2 h-full w-full">
                    {/* Disclaimer */}
                    <div
                        className="text-sm text-secondary mb-4 p-3 border border-dashed border-primary rounded-md bg-gray-50">
                        <strong>Note:</strong> Filling in the optional fields especially weight and birthday can help better determine if
                        an health parameter is out of the ordinary.
                    </div>
                    {/* Scrollable Content */}
                    <div className="flex-grow overflow-y-auto space-y-4 px-4">
                        <label className="form-control w-full">
                            <div className="label">
                                <span className="label-text text-secondary font-bold">Photo*</span>
                            </div>
                            <input
                                type="file"
                                {...register('image', {
                                    required: {
                                        value: false,
                                        message: 'Image is required',
                                    },
                                })}
                                className="file-input file-input-bordered file-input-primary w-full"
                            />
                            {errors.image && <p className="text-error text-sm">{errors.image.message}</p>}
                        </label>

                        {/* Pet Name Input */}
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

                        <div className="flex flex-line gap-2">
                            {/* Species Dropdown */}
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
                                    ...speciesOptions,
                                ]}
                                error={errors.species && errors.species.message}
                            />

                            {/* Sex Dropdown */}
                            <InputField
                                label="Sex"
                                name="sex"
                                type="select"
                                placeholder="Select the sex"
                                register={register}
                                required={false}
                                options={[
                                    {value: '', label: 'Select the sex'},
                                    ...sexOptions,
                                ]}
                                error={errors.sex && errors.sex.message}
                            />
                        </div>

                        {/* Breed */}
                        <InputField
                            label="Breed"
                            name="breed"
                            type="text"
                            placeholder="Breed"
                            register={register}
                            required={false}
                            error={errors.breed && errors.breed.message}
                        />

                        {/* Weight and Height */}
                        <div className="flex gap-2">
                            <InputField
                                label="Weight"
                                name="weight"
                                type="number"
                                placeholder="Weight (kg)"
                                register={register}
                                required={false}
                                error={errors.weight && errors.weight.message}
                            />
                            <InputField
                                label="Height"
                                name="height"
                                type="number"
                                placeholder="Height (cm)"
                                register={register}
                                required={false}
                                error={errors.height && errors.height.message}
                            />
                        </div>

                        {/* Birthday */}
                        <InputField
                            label="Birthday"
                            name="birthday"
                            type="date"
                            placeholder="Birthday"
                            register={register}
                            required={false}
                            error={errors.birthday && errors.birthday.message}
                        />

                        {/* Device ID */}
                        <InputField
                            label="Device ID*"
                            name="deviceId"
                            type="text"
                            placeholder="Device ID"
                            register={register}
                            required={{
                                value: true,
                                message: 'Device ID is required',
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

                    {/* Register Button */}
                    <div className="py-4 bg-white w-full mb-6">
                        <button type="submit" className="btn btn-primary w-full">
                            Register Pet
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}