import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-regular-svg-icons";
import { useEffect, useState } from "react";
import NotificationComponent from "../components/NotificationComponent";
import axios from "axios";

const baseUrl = "http://localhost/api/v1";
const notificationsBaseUrl = `${baseUrl}/notifications`;
const animalsBaseUrl = `${baseUrl}/animals`;

export default function Notifications() {
    const [notifications, setNotifications] = useState([]);
    const [markedAsRead, setMarkedAsRead] = useState(false);

    const authHeaders = {
        Authorization: `Bearer ${localStorage.getItem("authToken")}`,
    };

    useEffect(() => {
        const fetchNotifications = async () => {
            try {
                // Fetch notifications
                const response = await axios.get(`${notificationsBaseUrl}`, {
                    headers: authHeaders,
                });
                const notificationData = response.data;
                console.log("Notification Data:", notificationData);

                // Enrich notifications with animal data
                const enrichedNotifications = await Promise.all(
                    notificationData.map(async (notification) => {
                        try {
                            const animalResponse = await axios.get(`${animalsBaseUrl}/${notification.animalId}`, {
                                headers: authHeaders,
                            });
                            const animalData = animalResponse.data;

                            return {
                                ...notification,
                                name: animalData.name || "Unknown",
                                notification: notification.content.replace("{name}", animalData.name || "Unknown"),
                                img: animalData.imagePath || "https://via.placeholder.com/150",
                                highlight: ["Off Limits", "Acceleration"].includes(notification.title),
                            };
                        } catch (error) {
                            console.error(`Error fetching animal data for ID ${notification.animalId}:`, error);
                        }
                    })
                );

                setNotifications(enrichedNotifications);

                // Simulate marking notifications as read after a delay
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
                    .filter((notification) => !notification.read) // Filter unread notifications
                    .map((notification) =>
                        axios.put(
                            `${notificationsBaseUrl}/${notification.id}`,
                            {
                                ...notification,
                                read: true,
                            },
                            { headers: authHeaders }
                        )
                    )
            );

            // Update state to mark notifications as read
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
                    axios.delete(`${notificationsBaseUrl}/${notification.id}`, {
                        headers: authHeaders,
                    })
                )
            );

            // Clear notifications from state
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
                <div key={notification.id}>
                    <NotificationComponent
                        id={notification.id}
                        name={notification.name}
                        notification={notification.notification}
                        image={notification.img}
                        highlight={notification.highlight}
                        onDelete={() => setNotifications((prev) => prev.filter((n) => n.id !== notification.id))}
                    />
                </div>
            ))}
        </div>
    );
}