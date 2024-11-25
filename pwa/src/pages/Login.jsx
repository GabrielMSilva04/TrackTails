import {InputField} from "../components/InputField.jsx";
import {useForm} from "react-hook-form";
import axios from "axios";
// import {navigate} from "@reach/router";

const base_url = "http://localhost/api/v1";
const login_url = `${base_url}/users/login`;

export default function Login() {
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm();

    const onSubmit = async (data) => {
        try {
            // Send login request
            const response = await axios.post(login_url, {
                email: data.email,
                password: data.password,
            });

            // Store token in localStorage
            const token = response.data.token;
            localStorage.setItem("authToken", token);
            alert("Login successful!");

            // Fetch user information
            const userResponse = await fetchUserInfo(token);
            console.log("User Info:", userResponse);

            // navigate("/dashboard");
        } catch (error) {
            console.error("Login Error:", error);
            alert("Login failed. Please try again.");
        }
    };

    // Fetch user information using the token
    const fetchUserInfo = async (token) => {
        try {
            const response = await axios.get(user_info_url, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            return response.data; // Return user info
        } catch (error) {
            console.error("Error fetching user information:", error);
            throw error;
        }
    };

    return (
        <div className="bg-primary h-screen flex flex-col">
            {/* Logo Section */}
            <div className="w-full flex justify-center items-center mt-12">
                <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
            </div>

            {/* Formulary Section */}
            <div className="bg-white w-full h-3/4 rounded-t-3xl p-8 flex flex-col items-center absolute bottom-0">
                <h2 className="text-2xl font-bold text-primary mb-4">Welcome Back</h2>
                <form
                    onSubmit={handleSubmit(onSubmit)}
                    className="w-3/4 mt-8 flex flex-col gap-2">
                    <InputField
                        label="Email"
                        name={"email"}
                        type="email"
                        placeholder="Email"
                        register={register}
                        required={{
                            value: true,
                            message: "This field is required"
                        }}
                        error={errors.email?.message}
                    />
                    <InputField
                        label="Password"
                        name={"password"}
                        type="password"
                        placeholder="********"
                        register={register}
                        required={{
                            value: true,
                            message: "This field is required"
                        }}
                        error={errors.password?.message}
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
