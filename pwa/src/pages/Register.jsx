import {InputField} from "../components/InputField.jsx";
import {useForm} from "react-hook-form";

export default function Register() {
    const {
        register,
        handleSubmit,
        watch,
        formState: { errors },
    } = useForm();

    const onSubmit = (data) => {
        console.log('Form Data:', data);
        alert('Pet registered successfully!');
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
