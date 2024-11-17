import {InputField} from "../components/InputField.jsx";

export default function Login() {
    return (
        <div className="bg-primary h-screen flex flex-col">
            {/* Logo Section */}
            <div className="w-full flex justify-center items-center mt-12">
                <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
            </div>

            {/* Formulary Section */}
            <div className="bg-white w-full h-3/4 rounded-t-3xl p-8 flex flex-col items-center absolute bottom-0">
                <h2 className="text-2xl font-bold text-primary mb-4">Welcome Back</h2>
                <form className="w-3/4 mt-8 flex flex-col gap-2">
                    <InputField
                        label="Email"
                        type="email"
                        placeholder="Email"
                    />
                    <InputField
                        label="Password"
                        type="password"
                        placeholder="********"
                    />
                    <button className="btn btn-primary text-white w-full mt-6">Log in</button>
                </form>
                <p className="text-sm mt-32">
                    Not have an account?{" "}
                    <a href="/register" className="text-secondary font-bold">
                        Sign Up
                    </a>
                </p>
            </div>
        </div>
    );

}
