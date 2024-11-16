import {InputField} from "../components/InputField.jsx";
import {Link} from "react-router-dom";
import {useForm} from "react-hook-form";

export function RegisterPet() {
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm();

    const onSubmit = (data) => {
        console.log('Form Data:', data);
        alert('Pet registered successfully!');
    };

    const speciesOptions = [
        { value: 'dog', label: 'Dog' },
        { value: 'cat', label: 'Cat' },
    ];

    const sexOptions = [
        { value: 'male', label: 'Male' },
        { value: 'female', label: 'Female' },
    ];

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
                    <form className="flex flex-col gap-2 px-6 mb-10" onSubmit={handleSubmit(onSubmit)}>
                        <label className="form-control w-full">
                            <div className="label">
                                <span className="label-text text-secondary font-bold">Photo</span>
                            </div>
                            <input
                                type="file"
                                className="file-input file-input-bordered file-input-primary w-full"/>
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
                                    { value: '', label: 'Select a species' },
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
                                    { value: '', label: 'Select the sex' },
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

                        {/* Register Button */}
                        <button type="submit" className="btn btn-primary">
                            Register Pet
                        </button>
                    </form>
                </div>
            </div>
        </>
    );
}