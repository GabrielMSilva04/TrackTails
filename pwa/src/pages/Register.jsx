import {InputField} from "../components/InputField.jsx";
import {useForm} from "react-hook-form";
import axios from "axios";
import {baseUrl} from "../consts";

const register_url = `${baseUrl}/users/register`;
const login_url = `${baseUrl}/users/login`;

export default function Register() {
    const {
        register,
        handleSubmit,
        watch,
        formState: { errors },
    } = useForm();

    const onSubmit = async (data) => {
        const payload = {
            displayName: data.displayName,
            email: data.email,
            phoneNumber: data.phoneNumber,
            password: data.password,
        };

        console.log("Payload to send:", payload);

        try {
            const response = await axios.post(register_url, payload, {
                headers: {
                    "Content-Type": "application/json",
                },
            });
            alert("Registration successful!");
            console.log("Registration Response:", response.data);
        } catch (error) {
            console.error("Registration Error:", error);
            alert("Registration failed. Please try again.");
        }

        // Login user after registration
        try {
            const response = await axios.post(login_url, {
                email: data.email,
                password: data.password,
            });

            const token = response.data.token;
            localStorage.setItem("authToken", token);
            alert("Login successful!");

            console.log("Token:", token);

            // Redirect user to mypets page
            window.location.href = "/mypets";
        } catch (error) {
            console.error("Login Error:", error);
            alert("Login failed. Please try again.");
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
                <h2 className="text-2xl font-bold text-primary mb-2">Create your account</h2>
                <form
                    onSubmit={handleSubmit(onSubmit)}
                    className="w-3/4 mt-2 flex flex-col gap-2">
                    <InputField
                        label="Display Name"
                        name="displayName"
                        type="text"
                        placeholder="johndoe"
                        register={register}
                        required={{
                            value: true,
                            message: "This field is required"
                        }}
                        error={errors.displayName?.message}
                    />
                    <InputField
                        label="Email"
                        name="email"
                        type="email"
                        placeholder="johndoe@ua.pt"
                        register={register}
                        required={{
                            value: true,
                            message: "This field is required"
                        }}
                        error={errors.email?.message}
                    />
                    <InputField
                        label="Phone Number"
                        name="phoneNumber"
                        type="text"
                        placeholder="123456789"
                        register={register}
                        required={{
                            value: true,
                            message: "This field is required",
                        }}
                        validate={(value) =>
                            /^\d{5,15}$/.test(value) || "Phone number must be 5-15 digits"
                        }
                        error={errors.phoneNumber?.message}
                    />
                    <InputField
                        label="Password"
                        name="password"
                        type="password"
                        placeholder="********"
                        register={register}
                        required={{
                            value: true,
                            message: "This field is required"
                        }}
                        error={errors.password?.message}
                    />
                    <InputField
                        label="Confirm Password"
                        name="confirmPassword"
                        type="password"
                        placeholder="********"
                        register={register}
                        required={{
                            value: true,
                            message: "This field is required"
                        }}
                        error={errors.confirmPassword?.message}
                        validate={(value) =>
                            value === watch("password") || "Passwords do not match"
                        }
                    />
                    <button className="btn btn-primary text-white mt-4">Register</button>
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
