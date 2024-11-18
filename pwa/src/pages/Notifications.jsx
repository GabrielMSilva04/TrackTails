import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-regular-svg-icons";
import { useEffect, useState } from "react";
import NotificationComponent from "../components/NotificationComponent";

const notificationsBaseUrl = "http://localhost:8083/api/v1/notifications";
const animalsBaseUrl = "http://localhost:8082/api/v1/animals";

export default function Notifications() {
    const [notifications, setNotifications] = useState([]);
    const [markedAsRead, setMarkedAsRead] = useState(false);

    useEffect(() => {
        const fetchNotifications = async () => {
            try {
                const response = await fetch(`${notificationsBaseUrl}`);
                const notificationData = await response.json();

                const enrichedNotifications = await Promise.all(
                    notificationData.map(async (notification) => {
                        const animalResponse = await fetch(`${animalsBaseUrl}/${notification.animalId}`);
                        const animalData = await animalResponse.json();

                        return {
                            ...notification,
                            name: animalData.name || "Unknown",
                            notification: notification.content.replace("{name}", animalData.name || "Unknown"),
                            img: animalData.imagePath || "https://via.placeholder.com/150",
                            highlight: ["Off Limits", "Acceleration"].includes(notification.title),
                        };
                    })
                );

                setNotifications(enrichedNotifications);
                
                setTimeout(() => markNotificationsAsRead(enrichedNotifications), 5000);
            } catch (error) {
                console.error("Error fetching notifications:", error);
            }
        };

        fetchNotifications();
    }, []);

    const markNotificationsAsRead = async (notificationList) => {
        if (markedAsRead) return;
        setMarkedAsRead(true);

        try {
            await Promise.all(
                notificationList
                    .filter((notification) => !notification.read)
                    .map((notification) =>
                        fetch(`${notificationsBaseUrl}/${notification.id}`, {
                            method: "PUT",
                            headers: { "Content-Type": "application/json" },
                            body: JSON.stringify({
                                ...notification,
                                read: true,
                            }),
                        })
                    )
            );

            setNotifications((prev) =>
                prev.map((notification) => ({ ...notification, read: true }))
            );
        } catch (error) {
            console.error("Error marking notifications as read:", error);
        }
    };

    const handleClearAll = async () => {
        try {
            await Promise.all(
                notifications.map((notification) =>
                    fetch(`${notificationsBaseUrl}/${notification.id}`, { method: "DELETE" })
                )
            );
            setNotifications([]);
        } catch (error) {
            console.error("Error clearing notifications:", error);
        }
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

            {notifications.map((notification) => (
                <NotificationComponent
                    key={notification.id}
                    id={notification.id}
                    name={notification.name}
                    notification={notification.notification}
                    image={notification.img}
                    highlight={notification.highlight}
                    onDelete={() => setNotifications((prev) => prev.filter((n) => n.id !== notification.id))}
                />
            ))}
        </div>
    );
}