import { Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import axios from "axios";
import { baseUrl } from "../consts";
import {InputField} from "../components/InputField.jsx";

export default function GeneratePdfPage() {
    const {
        register,
        handleSubmit,
        formState: { errors },
        watch,
    } = useForm();

    const onSubmit = async (data) => {
        try {
            const token = localStorage.getItem('authToken');
            if (!token) {
                alert('No authentication token found. Please log in.');
                return;
            }

            console.log('Form data:', data);

            const selectedMetrics = Object.keys(data.metrics).filter((key) => data.metrics[key]);
            console.log('Selected Metrics:', selectedMetrics);

            // Make API call or handle data
            const savedPdf = null; // Replace with actual save logic
            console.log('Saved PDF:', savedPdf);
        } catch (error) {
            console.error('Error generating PDF:', error.response?.data || error.message);
        }
    };

    const metricOptions = [
        { value: 'weight', label: 'Weight' },
        { value: 'height', label: 'Height' },
        { value: 'heartRate', label: 'Heart Rate' },
        { value: 'respiratoryRate', label: 'Respiratory Rate' },
        { value: 'speed', label: 'Speed' },
    ];

    return (
        <div className="bg-primary h-screen w-full flex flex-col overflow-hidden">
            {/* Logo Section */}
            <div className="h-1/6 w-full flex justify-center items-center bg-primary">
                <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
            </div>

            {/* Form Section */}
            <div className="bg-white w-full rounded-t-3xl p-8 flex flex-col items-center h-5/6 overflow-hidden">
                <div className="flex items-center justify-between w-full relative mb-6">
                    <Link to={"/"} className="text-primary font-bold text-lg absolute left-0">← Back</Link>
                    <h2 className="text-2xl font-bold text-primary mx-auto">Generate</h2>
                </div>

                <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-2 h-full w-full">

                    {/* Metric Checkboxes */}
                    <div className="flex-grow overflow-y-auto space-y-4 px-4">
                        <div className="text-lg font-semibold text-primary">Select Metrics</div>
                        {metricOptions.map((metric) => (
                            <label key={metric.value} className="flex items-center space-x-3">
                                <input
                                    type="checkbox"
                                    {...register(`metrics.${metric.value}`)}
                                    className="checkbox checkbox-primary"
                                />
                                <span className="text-base">{metric.label}</span>
                            </label>
                        ))}
                    </div>

                    {/* Start Date */}
                    <InputField
                        label="Start Date"
                        name="startDate"
                        type="date"
                        placeholder="Start Date"
                        register={register}
                        required={true}
                        error={errors.startDate?.message}
                    />

                    {/* End Date */}
                    <InputField
                        label="End Date"
                        name="endDate"
                        type="date"
                        placeholder="End Date"
                        register={register}
                        required={true}
                        error={errors.endDate?.message}
                    />

                    {/* Submit Button */}
                    <div className="py-4 bg-white w-full mb-6">
                        <button type="submit" className="btn btn-primary w-full">
                            Generate PDF
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}
