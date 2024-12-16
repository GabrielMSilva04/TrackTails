import { Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import axios from "axios";
import { baseUrl } from "../consts";
import { InputField } from "../components/InputField.jsx";
import { convertToInfluxDBFormat } from "../utils";
import { useState } from "react";

export default function GeneratePdfPage({ animal }) {
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm();

    const [modalVisible, setModalVisible] = useState(false);
    const [reportLink, setReportLink] = useState("");
    const [tooltipVisible, setTooltipVisible] = useState(false);

    const onSubmit = async (data) => {
        try {
            const token = localStorage.getItem("authToken");
            if (!token) {
                alert("No authentication token found. Please log in.");
                return;
            }

            const metrics = Object.keys(data.metrics || {}).filter((key) => data.metrics[key]);

            if (metrics.length === 0) {
                alert("Please select at least one metric.");
                return;
            }

            const url = `${baseUrl}/reports`;

            const params = {
                start: convertToInfluxDBFormat(data.startDate) || "-1d",
                end: convertToInfluxDBFormat(data.endDate) || "now()",
                interval: "5m",
                include: metrics.join(","),
            };

            const reportPayload = {
                animalId: animal,
                fileName: data.reportName || "Report.pdf",
            };

            const headers = {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            };

            const response = await axios.post(url, reportPayload, { params, headers });

            const reportUUID = response.data.id;
            const reportUrl = `${baseUrl}/reports/${reportUUID}/download`;
            setReportLink(reportUrl);
            setModalVisible(true);
        } catch (error) {
            console.error("Error creating report:", error.response?.data || error.message);
            alert("An error occurred while creating the report.");
        }
    };

    const copyToClipboard = () => {
        navigator.clipboard.writeText(reportLink)
            .then(() => {
                setTooltipVisible(true);
                setTimeout(() => setTooltipVisible(false), 2000); // Hide tooltip after 2 seconds
            })
            .catch(() => alert("Failed to copy link."));
    };

    const downloadReport = () => {
        window.open(reportLink, "_blank");
    };

    const metricOptions = [
        { value: "weight", label: "Weight" },
        { value: "height", label: "Height" },
        { value: "heartRate", label: "Heart Rate" },
        { value: "breathRate", label: "Respiratory Rate" },
        { value: "speed", label: "Speed" },
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
                        type="datetime-local"
                        placeholder="Start Date"
                        register={register}
                        required={true}
                        error={errors.startDate?.message}
                    />

                    {/* End Date */}
                    <InputField
                        label="End Date"
                        name="endDate"
                        type="datetime-local"
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

            {/* Modal Section */}
            {modalVisible && (
                <div className="modal modal-open">
                    <div className="modal-box">
                        <h3 className="font-bold text-lg">Report Generated Successfully!</h3>
                        <p className="py-4">Your report has been generated. Use the link below to download it.</p>
                        <div className="form-control">
                            <label className="label">
                                <span className="label-text">Report URL</span>
                            </label>
                            <input
                                type="text"
                                value={reportLink}
                                readOnly
                                className="input input-bordered"
                            />
                        </div>
                        <div className="modal-action space-x-2">
                            <div
                                className={`tooltip tooltip-top ${tooltipVisible ? "tooltip-open" : ""}`}
                                data-tip="Link copied!"
                            >
                                <button
                                    className="btn btn-secondary"
                                    onClick={copyToClipboard}
                                >
                                    Copy Link
                                </button>
                            </div>
                            <button
                                onClick={downloadReport}
                                className="btn btn-primary text-white"
                            >
                                Download PDF
                            </button>
                            <button
                                onClick={() => setModalVisible(false)}
                                className="btn"
                            >
                                Close
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
