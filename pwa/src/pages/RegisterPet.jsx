export function RegisterPet() {
    return (
        <>
            <h2>Register Pet</h2>
            <form className="flex flex-col items-center">
                <label className="form-control w-full max-w-xs">
                    <div className="label">
                        <span className="label-text">Photo</span>
                    </div>
                    <input
                        type="file"
                        className="file-input file-input-bordered file-input-primary w-full max-w-xs"/>
                </label>
                <input type="text" placeholder="Pet Name" className="input w-full max-w-xs bg-secondary"/>
                <input type="text" placeholder="Species" className="input w-full max-w-xs bg-secondary"/>
                <input type="text" placeholder="Sex" className="input w-full max-w-xs bg-secondary"/>
                <input type="text" placeholder="Weight" className="input w-full max-w-xs bg-secondary"/>
                <input type="text" placeholder="Height" className="input w-full max-w-xs bg-secondary"/>
                <input type="text" placeholder="Birthday" className="input w-full max-w-xs bg-secondary"/>
                <input type="text" placeholder="DeviceID" className="input w-full max-w-xs bg-secondary"/>
                <button className="btn btn-primary w-1/2 mt-4">Register</button>
            </form>
        </>
);
}