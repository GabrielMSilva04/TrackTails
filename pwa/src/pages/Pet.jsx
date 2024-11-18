import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHeartPulse, faMapLocationDot, faGauge, faSyringe, faLungs, faPlus } from "@fortawesome/free-solid-svg-icons";
import { faFilePdf } from "@fortawesome/free-regular-svg-icons";
import sleepLogo from "../assets/sleep.png";
import PropTypes from "prop-types";
import { useEffect, useState } from "react";
import { useOutletContext } from "react-router-dom";

const Card = ({ icon, label, value, to, image }) => (
    <Link to={to} className="card bg-primary text-primary-content w-40 h-32 rounded-t-2xl shadow-xl">
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
    </Link>
);

Card.propTypes = {
    icon: PropTypes.object,
    label: PropTypes.string.isRequired,
    value: PropTypes.string.isRequired,
    to: PropTypes.string.isRequired,
    image: PropTypes.string,
};

export default function Pet() {
    const { animal } = useOutletContext();
    const [animalData, setAnimalData] = useState({
        species: "Unknown",
        sex: "Unknown",
        birthDate: null,
        age: "Calculating...",
    });

    useEffect(() => {
        if (animal) {
            const calculateAge = (birthDate) => {
                if (!birthDate) return "Unknown";
                const birth = new Date(birthDate);
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
                species: animal.species || "Unknown",
                sex: animal.sex === "m" ? "Male" : "Female",
                birthDate: formatDate(animal.birthDate),
                age: `${calculateAge(animal.birthDate)} (${formatDate(animal.birthDate)})`,
            });
        }
    }, [animal]);

    const stats = [
        { icon: faHeartPulse, label: "Heart Rate", value: "120 BPM", to: "/", image: null },
        { icon: null, label: "Sleep", value: "2H20MIN", to: "/", image: sleepLogo },
        { icon: faGauge, label: "Speed", value: "5 HM/H", to: "/", image: null },
        { icon: faLungs, label: "Breathing", value: "50 Breaths/M", to: "/", image: null },
        { icon: faMapLocationDot, label: "Location", value: "Location", to: "/", image: null },
        { icon: faSyringe, label: "Vaccines", value: "Vaccines", to: "/", image: null },
        { icon: faFilePdf, label: "Generate Report", value: "Generate", to: "/", image: null },
    ];

    return (
        <div>
            <div className="mt-2 text-secondary font-bold text-xs text-center">
                Age: {animalData.age}, {animalData.species}, Sex: {animalData.sex}
            </div>
            <div className="mt-2 text-secondary font-bold text-xs text-center">
                Last Weight: 30 kg, Last Height: 50 cm
            </div>
            <div className="flex flex-wrap justify-between px-4 mt-4 gap-4">
                {stats.map((stat, index) => (
                    <Card
                        key={index}
                        icon={stat.icon}
                        label={stat.label}
                        value={stat.value}
                        to={stat.to}
                        image={stat.image}
                    />
                ))}
            </div>
        </div>
    );
}