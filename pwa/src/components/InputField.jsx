const InputField = ({
                        label,
                        name,
                        type,
                        placeholder,
                        value,
                        onChange,
                        error,
                        options,
                    }) => {
    return (
        <>
            <label className="form-control w-full">
                <span className="label-text text-secondary font-bold">{label}</span>
                {type === 'select' ? (
                    <select
                        name={name}
                        value={value}
                        onChange={(e) => onChange(e.target.value)}
                        className="select select-bordered w-full"
                    >
                        <option value="" disabled>
                            {placeholder || `Select ${label}`}
                        </option>
                        {options?.map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                ) : (
                    <input
                        type={type}
                        name={name}
                        placeholder={placeholder}
                        value={value}
                        onChange={(e) => onChange(e.target.value)}
                        className="input input-bordered w-full"
                    />
                )}
                {error && <p className="text-error text-sm">{error}</p>}
            </label>
        </>
    )
}

export { InputField }