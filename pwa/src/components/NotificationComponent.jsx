import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import PropTypes from "prop-types";
import { useState } from "react";

export default function NotificationComponent({ name, notification, image, warning }) {
    NotificationComponent.propTypes = {
        name: PropTypes.string.isRequired,
        notification: PropTypes.string.isRequired,
        image: PropTypes.string.isRequired,
        warning: PropTypes.number
    };

    const [isVisible, setIsVisible] = useState(true);

    const handleClear = () => {
        setIsVisible(false);
    };

    if (!isVisible) return null;

    return (
        <div
            className={`relative card mt-5 p-4 flex items-start rounded-xl h-20 ${
                warning === 1 ? "bg-warning" : "bg-secondary"
            } text-white`}
        >
            <button className="absolute top-2 right-3 text-white" onClick={handleClear}>
                <FontAwesomeIcon icon={faXmark} />
            </button>
            <div className="flex">
                <img src={image} alt={name} className="rounded-full h-12 w-12 mr-4" />

                <div className="flex flex-col justify-center">
                    <p className="text-white font-bold text-sm">{notification}</p>
                </div>
            </div>
        </div>
    );
}