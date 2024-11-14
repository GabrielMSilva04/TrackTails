export function InputField({ label, type, placeholder, value, onChange }) {
    return (
        <>
            <label className="form-control w-full">
                <span className="label-text text-secondary font-bold">{label}</span>
                <input
                    type={type}
                    placeholder={placeholder}
                    className="input h-10 bg-base-200 text-primary w-full"
                />
            </label>
        </>
    )
}