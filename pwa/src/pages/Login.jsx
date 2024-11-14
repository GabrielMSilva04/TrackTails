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
                <form className="w-3/4 mt-8 flex flex-col">
                    <label className="text-secondary text-sm font-bold">Email</label>
                    <input
                        type="email"
                        placeholder="Email"
                        className="input h-10 bg-base-200 text-primary mb-2"
                    />
                    <label className="text-secondary text-sm font-bold">Password</label>
                    <input
                        type="password"
                        placeholder="Password"
                        className="input h-10 bg-base-200 text-primary mb-2"
                    />
                    <button className="btn btn-primary text-white w-full mt-6">Log in</button>
                </form>
                <p className="text-sm mt-32">
                    Not have an account?{" "}
                    <a href="#" className="text-secondary font-bold">
                        Sign Up
                    </a>
                </p>
            </div>
        </div>
    );

}