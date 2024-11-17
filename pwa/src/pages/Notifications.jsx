import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from '@fortawesome/free-regular-svg-icons';
import { useState } from "react";
import NotificationComponent from "../components/NotificationComponent";

export default function Notifications() {
    const initialNotifications = [
        {
            name: "Cookie",
            img: "https://placedog.net/500x500",
            notification: "Cookie has trespassed his area",
            warning: 1
        },
        {
            name: "Milu",
            img: "https://placecats.com/500/500",
            notification: "Milu's device QR code was scanned by someone",
            warning: 0
        },
        {
            name: "Jack",
            img: "https://ilovemydogsomuch.com/wp-content/uploads/2023/02/323839-1600x1066-border-collie-breed-1400x933.jpg",
            notification: "An abnormal acceleration was detected by Jack's device",
            warning: 1
        }
    ];

    const [notifications, setNotifications] = useState(initialNotifications);

    const handleClearAll = () => {
        setNotifications([]);
    };

    return (
        <div className="items-center">
            <div className="mt-10 flex justify-between">
                <h1 className="text-primary text-2xl font-extrabold">
                    {notifications.length > 0 ? "Notifications" : "No notifications to show"}
                </h1>
                {notifications.length > 0 && (
                    <button className="text-primary text-2xl" onClick={handleClearAll}>
                        <FontAwesomeIcon icon={faCircleCheck} />
                    </button>
                )}
            </div>

            {notifications.map((animal, index) => (
                <NotificationComponent
                    key={index}
                    name={animal.name}
                    notification={animal.notification}
                    image={animal.img}
                    warning={animal.warning}
                />
            ))}
        </div>
    );
}