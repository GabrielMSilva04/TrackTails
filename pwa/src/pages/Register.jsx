const InputField = ({ label, type, placeholder, value, onChange }) => {
    return (
        <div className="mb-2">
            <label className="text-secondary text-sm font-bold">{label}</label>
            <input
                type={type}
                placeholder={placeholder}
                className="input h-10 bg-base-200 text-primary w-full"
            />
        </div>
    );
};


export default function Home() {
    return (
        <div className="bg-primary h-screen flex flex-col">
            {/* Logo Section */}
            <div className="w-full flex justify-center items-center mt-12">
                <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
            </div>

            {/* Formulary Section */}
            <div className="bg-white w-full h-3/4 rounded-t-3xl p-8 flex flex-col items-center absolute bottom-0">
                <h2 className="text-2xl font-bold text-primary mb-2">Create your account</h2>
                <form className="w-3/4 mt-2 flex flex-col">
                    <InputField
                        label="Display Name"
                        type="text"
                        placeholder="johndoe"
                    />
                    <InputField
                        label="Email"
                        type="email"
                        placeholder="johndoe@ua.pt"
                    />
                    <InputField
                        label="Password"
                        type="password"
                        placeholder="********"
                    />
                    <InputField
                        label="Confirm Password"
                        type="password"
                        placeholder="********"
                    />
                    <button className="btn btn-primary text-white  mt-4">Register</button>
                </form>
                <p className="text-sm mt-3">
                    Already have an account?{" "}
                    <a href="/login" className="text-secondary font-bold">
                        Sign in
                    </a>
                </p>
            </div>
        </div>
    );
}