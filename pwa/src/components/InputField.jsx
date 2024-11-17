import React from 'react';

const InputField = ({
                        label,
                        name,
                        type = 'text',
                        placeholder,
                        value,
                        onChange,
                        options,
                        register,
                        required = false,
                        validate,
                        error,
                    }) => {
    // Return null if 'name' or 'register' is missing, as they are critical for React Hook Form
    if (!name || !register) {
        return null;
    }

    return (
        <>
            <label className="form-control w-full">
                <span className="label-text text-secondary font-bold">{label}</span>
                {type === 'select' ? (
                    <select
                        {...register(name, { required, validate })} // Pass validate here
                        value={value}
                        onChange={(e) => onChange?.(e.target.value)}
                        className="select select-bordered w-full"
                    >
                        <option value="" disabled>
                            {placeholder || 'Select an option'}
                        </option>
                        {options?.map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                ) : (
                    <input
                        {...register(name, { required, validate })} // Pass validate here
                        type={type}
                        placeholder={placeholder}
                        value={value}
                        onChange={(e) => onChange?.(e.target.value)}
                        className="input input-bordered w-full"
                    />
                )}
                {error && <p className="text-error text-sm">{error}</p>}
            </label>
        </>
    );
};

export { InputField };