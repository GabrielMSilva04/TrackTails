import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHeartPulse, faMapLocationDot, faGauge, faSyringe, faLungs } from "@fortawesome/free-solid-svg-icons";
import { faFilePdf } from "@fortawesome/free-regular-svg-icons";
import sleepLogo from "../assets/sleep.png";
import PropTypes from "prop-types";
import { useEffect, useState } from "react";
import { useAnimalContext } from '../contexts/AnimalContext';
import { useNavigate } from "react-router-dom";
import axios from "axios";

const baseUrl = "http://localhost/api/v1";
const animalDataBaseUrl = `${baseUrl}/animaldata`;

const Card = ({ icon, label, value, trigger, image }) => (
    <button onClick={trigger} className="card bg-primary text-primary-content w-40 h-32 rounded-t-2xl shadow-xl">
        <div className="card-body flex flex-col justify-between p-4">
            <h2 className="card-title text-5xl relative text-white">
                {image ? (
                    <img src={image} alt={label} className="w-12 h-12" />
                ) : (
                    <FontAwesomeIcon icon={icon} aria-label={label} />
                )}
            </h2>
            <div className="text-xl font-bold text-white">{value}</div>
        </div>
    </button>
);

Card.propTypes = {
    icon: PropTypes.object,
    label: PropTypes.string.isRequired,
    value: PropTypes.string.isRequired,
    trigger: PropTypes.func,
    image: PropTypes.string,
};

export default function Pet({ onMetricSelect }) {
    const navigate = useNavigate();
    const { selectedAnimal } = useAnimalContext();
    const [animalData, setAnimalData] = useState({
        species: "Unknown",
        breed: "Unknown",
        sex: "Unknown",
        birthday: null,
        age: "Calculating...",
    });

    const [latestData, setLatestData] = useState({
        weight: "Unknown",
        height: "Unknown",
        heartRate: "Unknown",
        breathRate: "Unknown",
        speed: "Unknown",
        batteryPercentage: "Unknown",
        location: { latitude: "Unknown", longitude: "Unknown" },
    });

    useEffect(() => {
        console.log("Selected animal in Pet:", selectedAnimal);
        if (selectedAnimal) {
            const calculateAge = (birthday) => {
                if (!birthday || birthday === "Unknown") return null;
                const birth = new Date(birthday);
                const now = new Date();
                const age = now.getFullYear() - birth.getFullYear();
                const isBeforeBirthday =
                    now.getMonth() < birth.getMonth() ||
                    (now.getMonth() === birth.getMonth() && now.getDate() < birth.getDate());
                return isBeforeBirthday ? age - 1 : age;
            };

            const formatDate = (date) => {
                if (!date) return "Unknown";
                const d = new Date(date);
                return `${String(d.getDate()).padStart(2, "0")}/${String(d.getMonth() + 1).padStart(2, "0")}/${d.getFullYear()}`;
            };

            setAnimalData({
                species: selectedAnimal.species ? selectedAnimal.species.charAt(0).toUpperCase() + selectedAnimal.species.slice(1) : "Unknown",
                breed: selectedAnimal.breed || "Unknown",
                sex: selectedAnimal.sex === "m" ? "Male" : selectedAnimal.sex === "f" ? "Female" : "Unknown",
                birthday: formatDate(selectedAnimal.birthday),
                age: selectedAnimal.birthday ? `${calculateAge(selectedAnimal.birthday)} (${formatDate(selectedAnimal.birthday)})` : "Unknown",
            });

            // Fetch latest data for the animal
            const fetchLatestData = async () => {
                try {
                    const response = await axios.get(`${animalDataBaseUrl}/latest/${selectedAnimal.id}`, {
                        headers: {
                            Authorization: `Bearer ${localStorage.getItem("authToken")}`,
                        },
                    });
                    const data = response.data;

                    setLatestData({
                        weight: data.weight?.toFixed(2) || "Unknown",
                        height: data.height?.toFixed(2) || "Unknown",
                        heartRate: data.heartRate?.toFixed(0) || "Unknown",
                        breathRate: data.breathRate?.toFixed(0) || "Unknown",
                        speed: data.speed?.toFixed(2) || "Unknown",
                        batteryPercentage: data.batteryPercentage?.toFixed(0) || "Unknown",
                        location: {
                            latitude: data.latitude?.toFixed(6) || "Unknown",
                            longitude: data.longitude?.toFixed(6) || "Unknown",
                        },
                    });
                } catch (error) {
                    console.error("Error fetching latest animal data:", error);
                }
            };

            fetchLatestData();
        }
    }, [selectedAnimal]);

    const onGenerateReport = async (animalId) => {
        try {
            const reportUrl = `/api/v1/reports`;

            const reportRequestBody = {
                animalId: animalId,
                fileName: `${animalId}_report.pdf`
            };

            // TODO: Define the report generation parameters
            const params = {
                start: '-1d',
                end: 'now()',
                interval: '15m'
            };

            const response = await axios.post(reportUrl, reportRequestBody, { params });

            if (response.status === 201) {
                console.log('Report generation request successful', response.data);

                // Download the generated report
                const reportId = response.data.id;
                const downloadUrl = `${reportUrl}/${reportId}/download`;

                const pdfResponse = await axios.get(downloadUrl, {
                    responseType: 'blob',
                });

                const blob = new Blob([pdfResponse.data], { type: 'application/pdf' });
                const link = document.createElement('a');
                link.href = window.URL.createObjectURL(blob);
                link.download = `${animalId}_report.pdf`;
                link.click();

                alert('Report downloaded successfully!');
            } else {
                console.error('Failed to generate report:', response);
                alert('Failed to generate report. Please try again.');
            }
        } catch (error) {
            console.error('Error during report generation:', error);
            alert('An error occurred while generating the report.');
        }
    };

    const onLocationSelect = () => {
        navigate("/map/details");
    }

    const stats = [
        { icon: faHeartPulse, label: "Heart Rate", value: `${latestData.heartRate} BPM`, trigger: (() => onMetricSelect("heartRate")), image: null },
        { icon: null, label: "Sleep", value: "Sleep", trigger: (() => null), image: sleepLogo},
        { icon: faGauge, label: "Speed", value: `${latestData.speed} KM/H`, trigger: (() => onMetricSelect("speed")), image: null },
        { icon: faLungs, label: "Breathing", value: `${latestData.breathRate} Breaths/M`, trigger: (() => onMetricSelect("breathRate")), image: null },
        { icon: faMapLocationDot, label: "Location", value: "Location", trigger: (() => onLocationSelect()), image: null },
        { icon: faFilePdf, label: "Generate Report", value: "Generate", trigger: (() => onGenerateReport(selectedAnimal.id)), image: null },
    ];

    return (
        <div>
            <div className="mt-2 text-secondary font-bold text-xs text-center">
                {[
                    animalData.age !== "Unknown" ? `Age: ${animalData.age}` : null,
                    animalData.species !== "Unknown" ? `${animalData.species}${animalData.breed !== "Unknown" ? `: ${animalData.breed}` : ""}` : null,
                    animalData.sex !== "Unknown" ? `${animalData.sex}` : null,
                ]
                    .filter(Boolean)
                    .join(", ")}
            </div>
            <div className="mt-2 text-secondary font-bold text-xs text-center">
                {latestData.weight !== "Unknown" && (
                    <button onClick={() => onMetricSelect("weight")}>Last Weight: {latestData.weight} kg</button>
                )}
                {latestData.weight !== "Unknown" && latestData.height !== "Unknown" && ", "}
                {latestData.height !== "Unknown" && (
                    <button onClick={() => onMetricSelect("height")}>Last Height: {latestData.height} cm</button>
                )}
            </div>
            <div className="mt-2 text-center">
                {latestData.batteryPercentage !== "Unknown" && (
                    <div
                        className={`text-lg font-bold px-4 py-2 rounded-lg inline-block ${
                            latestData.batteryPercentage > 20
                                ? "bg-primary text-white"
                                : "bg-warning text-white"
                        }`}
                    >
                        Battery:{" "}
                        <span className="text-white font-extrabold">
                {latestData.batteryPercentage}%
            </span>
                    </div>
                )}
            </div>
            <div className="flex flex-wrap justify-between px-4 mt-4 gap-4">
                {stats.map((stat, index) => (
                    <Card
                        key={index}
                        trigger={stat.trigger}
                        icon={stat.icon}
                        label={stat.label}
                        value={stat.value}
                        image={stat.image}
                    />
                ))}
            </div>
        </div>
    );
}