import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faHeartPulse, faMapLocationDot, faGauge, faSyringe, faLungs} from "@fortawesome/free-solid-svg-icons";
import { faFilePdf } from "@fortawesome/free-regular-svg-icons";
import sleepLogo from "../assets/sleep.png";
import PropTypes from "prop-types";

const Card = ({ icon, label, value, to, image }) => (
    <Link to={to} className="card bg-primary text-primary-content w-40 h-32 rounded-t-2xl shadow-xl">
        <div className="card-body flex flex-col justify-between p-4">
            <h2 className="card-title text-5xl relative">
                {image ? (
                    <img src={image} alt={label} className="w-12 h-12" />
                ) : (
                    <FontAwesomeIcon icon={icon} aria-label={label} />
                )}
            </h2>
            <div className="text-xl font-bold">{value}</div>
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

export default function LayoutAnimal() {
    const animal = {
        bpm: 120,
        sleep: "2H20MIN",
        speed: 5,
        breathing: 50
    };

    const stats = [
        { icon: faHeartPulse, label: "Heart Rate", value: `${animal.bpm} BPM`, to: "/", image: null },
        { icon: null, label: "Sleep", value: animal.sleep, to: "/", image: sleepLogo },
        { icon: faGauge, label: "Speed", value: `${animal.speed} HM/H`, to: "/", image: null },
        { icon: faLungs, label: "Breathing", value: `${animal.breathing} Breaths/M`, to: "/", image: null },
        { icon: faMapLocationDot, label: "Location", value: "Location", to: "/", image: null },
        { icon: faSyringe, label: "Vaccines", value: "Vaccines", to: "/", image: null },
        { icon: faFilePdf, label: "Generate Report", value: "Generate", to: "/", image: null },
    ];

    return (
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
    );
}